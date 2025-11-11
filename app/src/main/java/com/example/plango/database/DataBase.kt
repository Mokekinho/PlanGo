package com.example.plango.database

import androidx.room.*
import com.example.plango.model.DocumentInfo
import com.example.plango.model.Expense
import com.example.plango.model.Flight
import com.example.plango.model.Hotel
import com.example.plango.model.Travel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }
    }
}


@Entity
data class TravelEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,

    val name : String,
    val destination: String,           // City or country
    val isInternational: Boolean,      // Domestic vs international

    val startDate: LocalDate,
    val endDate: LocalDate,
    val purpose: String,               // "Vacation", "Work", "Family visit"

    val budget: Double,                // Total available budget
    //val expenses: List<Expense> = emptyList(), // List of expenses

    //val flights: List<Flight> = emptyList(),   // Flight info
    //val hotels: List<Hotel> = emptyList(),     // Hotel bookings

    //val documentInfo: DocumentInfo? = null,    // Passport/CPF etc.
    val notes: String? = null                  // Extra notes user wants to keep

)


@Entity
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val travelId: Int,
    val description: String,           // "Dinner at restaurant"
    val amount: Double,
    val category: String,              // "Food", "Transport", "Hotel"
    val date: LocalDate
){
    fun toDomainModel(): Expense{
        return Expense(
            id = id,
            description = description,
            amount = amount,
            category = category,
            date = date
        )
    }
}

@Entity
data class FlightEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val travelId: Int,
    val airline: String,
    val flightNumber: String,
    val departure: String,             // e.g. "GRU Airport"
    val arrival: String,               // e.g. "JFK Airport"
    val departureDate: LocalDate,
    val arrivalDate: LocalDate,
    val bookingReference: String? = null
){
    fun toDomainModel(): Flight{
        return Flight(
            id = id,
            airline = airline,
            flightNumber = flightNumber,
            departure = departure,
            arrival = arrival,
            departureDate = departureDate,
            arrivalDate = arrivalDate,
            bookingReference = bookingReference
        )
    }
}

@Entity
data class HotelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val travelId: Int,
    val name: String,
    val address: String,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
    val bookingReference: String? = null
){
    fun toDomainModel(): Hotel{
        return Hotel(
            id = id,
            name = name,
            address = address,
            checkIn = checkIn,
            checkOut = checkOut,
            bookingReference = bookingReference
        )
    }
}

@Entity
data class DocumentInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val travelId: Int,
    val passportNumber: String? = null,
    val rgOrCpf: String? = null
){
    fun toDomainModel(): DocumentInfo{
        return DocumentInfo(
            passportNumber = passportNumber,
            rgOrCpf = rgOrCpf
        )
    }
}


data class TravelWithList(
    @Embedded //basicamente é como se eu tivesse colocando as colunas de travel aqui
    val travelEntity: TravelEntity,// alem disso o Room sabe pro conta do Embedded que essa é a pai

    @Relation(
        parentColumn = "id", // aqui ele vai procurar a coluna id do pai. podeira ser outra
        entityColumn = "travelId" // e vai associar a essa coluna do filho
    )
    val expenses: List<ExpenseEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "travelId"
    )
    val  flights: List<FlightEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "travelId"
    )
    val  hotels: List<HotelEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "travelId"
    )
    val documentInfoEntity: DocumentInfoEntity?,

    ){
    fun toDomainModel(): Travel {
        return Travel(
            id = travelEntity.id,
            name = travelEntity.name,
            destination = travelEntity.destination,
            isInternational = travelEntity.isInternational,
            startDate = travelEntity.startDate,
            endDate = travelEntity.endDate,
            purpose = travelEntity.purpose,
            budget = travelEntity.budget,
            notes = travelEntity.notes,

            expenses = expenses.map { expenseEntity ->
                expenseEntity.toDomainModel()
            },

            flights = flights.map { flightEntity ->
                flightEntity.toDomainModel()
            },

            hotels = hotels.map { hotelEntity ->
                hotelEntity.toDomainModel()
            },

            documentInfo = documentInfoEntity?.toDomainModel()
        )
    }

}

@Dao
interface TravelDao{

    // Inserir ou atualizar uma viagem
    @Upsert
    suspend fun upsertTravel(travel: TravelEntity)
    //Insere cada tabela
    @Upsert
    suspend fun upsertExpense(expenses: ExpenseEntity)
    @Transaction
    suspend fun upsertExpenses(expenses: List<ExpenseEntity>) {
        expenses.forEach { upsertExpense(it) }
    }

    @Upsert
    suspend fun upsertFlight(flights: FlightEntity)
    @Transaction
    suspend fun upsertFlights(expenses: List<FlightEntity>) {
        expenses.forEach { upsertFlight(it) }
    }
    @Upsert
    suspend fun upsertHotel(hotels: HotelEntity)
    @Transaction
    suspend fun upsertHotels(expenses: List<HotelEntity>) {
        expenses.forEach { upsertHotel(it) }
    }

    @Upsert
    suspend fun upsertDocumentInfo(documentInfo: DocumentInfoEntity)

    // Buscar todas as viagens com suas listas associadas
    @Transaction // fala que é um busca em multiplas tabelas             
    @Query("SELECT * FROM TravelEntity")
    fun getAllTravels(): Flow<List<TravelWithList>> //Dessa forma toda vez que houver mudanças no bancos de dados o Room vai observar essa mudança e chamar essa função

    // Buscar uma viagem específica pelo ID, com todos os dados relacionados
    @Transaction
    @Query("SELECT * FROM TravelEntity WHERE id = :travelId")
    fun observeTravelById(travelId: Int): Flow<TravelWithList?>

    @Transaction
    @Query("SELECT * FROM TravelEntity WHERE id = :travelId")
    suspend fun loadTravelById(travelId: Int): TravelWithList?

    @Query("SELECT * FROM ExpenseEntity WHERE  id = :expenseId")
    suspend fun loadExpenseById(expenseId: Int): ExpenseEntity?

}


@Database(
    entities = [
        TravelEntity::class,
        ExpenseEntity::class,
        FlightEntity::class,
        HotelEntity::class,
        DocumentInfoEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class) // vai converter os LocalDate na hora de guardar pra string
abstract class AppDatabase : RoomDatabase() {
    abstract fun travelDao(): TravelDao
}

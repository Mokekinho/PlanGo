package com.example.plango.model

import com.example.plango.database.DocumentInfoEntity
import com.example.plango.database.ExpenseEntity
import com.example.plango.database.FlightEntity
import com.example.plango.database.HotelEntity
import com.example.plango.database.TravelEntity
import com.example.plango.database.TravelWithList
import java.time.LocalDate

data class Travel(
    val id: Int,
    val name: String,                  // "Summer in Paris"
    val destination: String,           // City or country
    val isInternational: Boolean,      // Domestic vs international

    val startDate: LocalDate,
    val endDate: LocalDate,
    val purpose: String,               // "Vacation", "Work", "Family visit"

    val budget: Double,                // Total available budget
    val expenses: List<Expense> = emptyList(), // List of expenses

    val flights: List<Flight> = emptyList(),   // Flight info
    val hotels: List<Hotel> = emptyList(),     // Hotel bookings

    val documentInfo: DocumentInfo? = null,    // Passport/CPF etc.
    val notes: String? = null                  // Extra notes user wants to keep
){

    fun toEntitySet(): TravelWithList {
        val travelEntity = TravelEntity(
            id = id,
            name = name,
            destination = destination,
            isInternational = isInternational,
            startDate = startDate,
            endDate = endDate,
            purpose = purpose,
            budget = budget,
            notes = notes
        )

        val expenseEntities = expenses.map { expense ->
            expense.toEntitySet(id)
        }

        val flightEntities = flights.map { flight ->
            flight.toEntitySet(id)
        }

        val hotelEntities = hotels.map { hotel ->
            hotel.toEntitySet(id)
        }

        val documentInfoEntity = documentInfo?.toEntitySet(id)

        return TravelWithList(
            travelEntity = travelEntity,
            expenses = expenseEntities,
            flights = flightEntities,
            hotels = hotelEntities,
            documentInfoEntity = documentInfoEntity
        )
    }

}

data class Expense(
    val id: Int,
    val description: String,           // "Dinner at restaurant"
    val amount: Double,
    val category: String,              // "Food", "Transport", "Hotel"
    val date: LocalDate
) {
    fun toEntitySet(
        travelId: Int
    ): ExpenseEntity {
        return ExpenseEntity(
            id = id,
            travelId = travelId,
            description = description,
            amount = amount,
            category = category,
            date = date
        )
    }
}


data class Flight(
    val id: Int,
    val airline: String,
    val flightNumber: String,
    val departure: String,             // e.g. "GRU Airport"
    val arrival: String,               // e.g. "JFK Airport"
    val departureDate: LocalDate,
    val arrivalDate: LocalDate,
    val bookingReference: String? = null
){
    fun toEntitySet(
        travelId: Int
    ): FlightEntity {
        return FlightEntity(
            id = id,
            travelId = travelId,
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

data class Hotel(
    val id: Int,
    val name: String,
    val address: String,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
    val bookingReference: String? = null
){
    fun toEntitySet(
        travelId: Int
    ): HotelEntity {
        return HotelEntity(
            id = id,
            travelId = travelId,
            name = name,
            address = address,
            checkIn = checkIn,
            checkOut = checkOut,
            bookingReference = bookingReference
        )
    }
}

data class DocumentInfo(
    val id: Int = 0,
    val passportNumber: String? = null,
    val rgOrCpf: String? = null
){
    fun toEntitySet(
        travelId: Int
    ): DocumentInfoEntity {
        return DocumentInfoEntity(
            id = id,
            travelId = travelId,
            passportNumber = passportNumber,
            rgOrCpf = rgOrCpf
        )
    }
}
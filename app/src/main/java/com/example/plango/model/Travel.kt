package com.example.plango.model

import com.example.plango.database.DocumentInfoEntity
import com.example.plango.database.ExpenseEntity
import com.example.plango.database.FlightEntity
import com.example.plango.database.HotelEntity
import com.example.plango.database.TravelEntity
import com.example.plango.database.TravelWithList
import java.time.LocalDate
import kotlin.text.category

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
            ExpenseEntity(
                id = expense.id,
                travelId = id,
                description = expense.description,
                amount = expense.amount,
                category = expense.category,
                date = expense.date
            )
        }

        val flightEntities = flights.map { flight ->
            FlightEntity(
                id = flight.id,
                travelId = id,
                airline = flight.airline,
                flightNumber = flight.flightNumber,
                departure = flight.departure,
                arrival = flight.arrival,
                departureDate = flight.departureDate,
                arrivalDate = flight.arrivalDate,
                bookingReference = flight.bookingReference
            )
        }

        val hotelEntities = hotels.map { hotel ->
            HotelEntity(
                id = hotel.id,
                travelId = id,
                name = hotel.name,
                address = hotel.address,
                checkIn = hotel.checkIn,
                checkOut = hotel.checkOut,
                bookingReference = hotel.bookingReference
            )
        }

        val documentInfoEntity = documentInfo?.let {
            DocumentInfoEntity(
                id = 0, // será autogerado pelo Room
                travelId = id,
                passportNumber = it.passportNumber,
                rgOrCpf = it.rgOrCpf
            )
        }

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
)

data class Flight(
    val id: Int,
    val airline: String,
    val flightNumber: String,
    val departure: String,             // e.g. "GRU Airport"
    val arrival: String,               // e.g. "JFK Airport"
    val departureDate: LocalDate,
    val arrivalDate: LocalDate,
    val bookingReference: String? = null
)

data class Hotel(
    val id: Int,
    val name: String,
    val address: String,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
    val bookingReference: String? = null
)

data class DocumentInfo(
    val passportNumber: String? = null,
    val rgOrCpf: String? = null
)
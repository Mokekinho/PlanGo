package com.example.plango.model

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
)

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
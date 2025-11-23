package com.example.plango.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.plango.database.TravelRepository
import com.example.plango.model.Flight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class AddEditFlightState(
    val airline: String = "",
    val flightNumber: String = "",
    val departure: String = "",             // e.g. "GRU Airport"
    val arrival: String = "",               // e.g. "JFK Airport"
    val departureDate: LocalDate = LocalDate.now(),
    val arrivalDate: LocalDate = LocalDate.now(),
    val bookingReference: String? = null,

    val isSaved: Boolean = false,
    val isSaving: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDepartureDatePicker : Boolean = false,
    val showArrivalDatePicker : Boolean = false,
)


sealed class AddEditFlightEvent{
    data class AirlineChanged(val airline: String): AddEditFlightEvent()
    data class FlightNumberChanged(val flightNumber: String): AddEditFlightEvent()
    data class DepartureChanged(val departure: String): AddEditFlightEvent()
    data class ArrivalChanged(val arrival: String): AddEditFlightEvent()
    data class DepartureDateChanged(val departureDate: LocalDate): AddEditFlightEvent()
    data class ArrivalDateChanged(val arrivalDate: LocalDate): AddEditFlightEvent()
    data class BookingReferenceChanged(val bookingReference: String?): AddEditFlightEvent()
    object ShowDepartureDatePicker: AddEditFlightEvent()
    object ShowArrivalDatePicker: AddEditFlightEvent()
    object Save: AddEditFlightEvent()
}

class AddEditFlightViewModel(
    private val repository: TravelRepository,
    private val travelId: Int,
    private val flightId: Int? = null
): ViewModel(){

    private val _state = MutableStateFlow(AddEditFlightState())
    val state : StateFlow<AddEditFlightState> = _state.asStateFlow()



    init{
        if (flightId != null){
            loadFlight(
                flightId
            )
        }
    }


    fun loadFlight(
        flightId: Int
    ){
        viewModelScope.launch {
            try {

                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null
                    )
                }

                val flight = repository.loadFlightById(flightId)

                _state.update {
                    it.copy(
                        isLoading = false,
                        airline = flight!!.airline,
                        flightNumber = flight.flightNumber,
                        departure = flight.departure,
                        arrival = flight.arrival,
                        departureDate = flight.departureDate,
                        arrivalDate = flight.arrivalDate,
                        bookingReference = flight.bookingReference,
                    )
                }
            }
            catch (e: Exception){
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message,
                    )
                }
            }


        }
    }


    fun onEvent(event: AddEditFlightEvent) {
        when (event) {
            is AddEditFlightEvent.AirlineChanged -> _state.update { it.copy(airline = event.airline) }
            is AddEditFlightEvent.FlightNumberChanged -> _state.update { it.copy(flightNumber = event.flightNumber) }
            is AddEditFlightEvent.DepartureChanged -> _state.update { it.copy(departure = event.departure) }
            is AddEditFlightEvent.ArrivalChanged -> _state.update { it.copy(arrival = event.arrival) }
            is AddEditFlightEvent.DepartureDateChanged -> _state.update { it.copy(departureDate = event.departureDate) }
            is AddEditFlightEvent.ArrivalDateChanged -> _state.update { it.copy(arrivalDate = event.arrivalDate) }
            is AddEditFlightEvent.BookingReferenceChanged -> _state.update { it.copy(bookingReference = event.bookingReference) }
            is AddEditFlightEvent.ShowDepartureDatePicker -> _state.update { it.copy(showDepartureDatePicker = !it.showDepartureDatePicker) }
            is AddEditFlightEvent.ShowArrivalDatePicker -> _state.update { it.copy(showArrivalDatePicker = !it.showArrivalDatePicker) }
            is AddEditFlightEvent.Save -> saveFlight()
        }
    }

    private fun saveFlight(){
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(
                        isSaving = true,
                        isSaved = false,
                        error = null,
                    )
                }

                val flight = Flight(
                    id = flightId ?: 0,
                    airline = _state.value.airline,
                    flightNumber = _state.value.flightNumber,
                    departure = _state.value.departure,
                    arrival = _state.value.arrival,
                    departureDate = _state.value.departureDate,
                    arrivalDate = _state.value.arrivalDate,
                    bookingReference = _state.value.bookingReference
                )

                repository.upsertFlight(flight, travelId)

                _state.update {
                    it.copy(
                        isSaving = false,
                        isSaved = true,
                    )
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        error = e.message
                    )
                }
            }
        }
    }
}


class AddEditFlightViewModelFactory( // O propósito dele é dizer ao sistema Android (ou, mais especificamente, à biblioteca ViewModel) como criar uma instância do seu TravelListViewModel quando ele precisa de um argumento, no caso, o TravelRepository.
    private val repository: TravelRepository,
    private val travelId: Int,
    private val flightId: Int? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditFlightViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddEditFlightViewModel(repository, travelId, flightId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
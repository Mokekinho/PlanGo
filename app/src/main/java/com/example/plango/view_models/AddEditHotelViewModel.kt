package com.example.plango.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.plango.database.TravelRepository
import com.example.plango.model.Hotel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class AddEditHotelState(

    val name: String = "",
    val address: String = "",
    val checkIn: LocalDate = LocalDate.now(),
    val checkOut: LocalDate = LocalDate.now(),
    val bookingReference: String? = null,

    val isSaved: Boolean = false,
    val isSaving: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDateRangePicker : Boolean = false,
)


sealed class AddEditHotelEvent{
    data class NameChanged(val name: String): AddEditHotelEvent()
    data class AddressChanged(val address: String): AddEditHotelEvent()
    data class CheckInChanged(val checkIn: LocalDate): AddEditHotelEvent()
    data class CheckOutChanged(val checkOut: LocalDate): AddEditHotelEvent()
    data class BookingReferenceChanged(val bookingReference: String?): AddEditHotelEvent()
    object ShowDateRangePicker: AddEditHotelEvent()
    object Save: AddEditHotelEvent()
}

class AddEditHotelViewModel(
    private val repository: TravelRepository,
    private val travelId: Int,
    private val hotelId: Int? = null
): ViewModel(){

    private val _state = MutableStateFlow(AddEditHotelState())
    val state : StateFlow<AddEditHotelState> = _state.asStateFlow()



    init{
        if (hotelId != null){
            loadHotel(
                hotelId
            )
        }
    }


    fun loadHotel(
        hotelId: Int
    ){
        viewModelScope.launch {
            try {

                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null
                    )
                }

                val hotel = repository.loadHotelById(hotelId)

                _state.update {
                    it.copy(
                        isLoading = false,
                        name = hotel!!.name,
                        address = hotel.address,
                        checkIn = hotel.checkIn,
                        checkOut = hotel.checkOut,
                        bookingReference = hotel.bookingReference,
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


    fun onEvent(event: AddEditHotelEvent) {
        when (event) {
            is AddEditHotelEvent.NameChanged -> _state.update { it.copy(name = event.name) }
            is AddEditHotelEvent.AddressChanged -> _state.update { it.copy(address = event.address) }
            is AddEditHotelEvent.CheckInChanged -> _state.update { it.copy(checkIn = event.checkIn) }
            is AddEditHotelEvent.CheckOutChanged -> _state.update { it.copy(checkOut = event.checkOut) }
            is AddEditHotelEvent.BookingReferenceChanged -> _state.update { it.copy(bookingReference = event.bookingReference) }
            is AddEditHotelEvent.ShowDateRangePicker -> _state.update { it.copy(showDateRangePicker = !it.showDateRangePicker) }
            is AddEditHotelEvent.Save -> saveHotel()
        }
    }

    private fun saveHotel(){
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(
                        isSaving = true,
                        isSaved = false,
                        error = null,
                    )
                }

                val hotel = Hotel(
                    id = hotelId ?: 0,
                    name = _state.value.name,
                    address = _state.value.address,
                    checkIn = _state.value.checkIn,
                    checkOut = _state.value.checkOut,
                    bookingReference = _state.value.bookingReference
                )

                repository.upsertHotel(hotel, travelId)

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


class AddEditHotelViewModelFactory( // O propósito dele é dizer ao sistema Android (ou, mais especificamente, à biblioteca ViewModel) como criar uma instância do seu TravelListViewModel quando ele precisa de um argumento, no caso, o TravelRepository.
    private val repository: TravelRepository,
    private val travelId: Int,
    private val hotelId: Int? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditHotelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddEditHotelViewModel(repository, travelId, hotelId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
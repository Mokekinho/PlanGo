package com.example.plango.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.plango.database.TravelRepository
import com.example.plango.model.DocumentInfo
import com.example.plango.model.Travel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class AddEditTravelState(
    val name: String = "",
    val destination: String = "",
    val isInternational: Boolean = false,
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val purpose: String = "Vacation",
    val budget: Double = 0.0,
    val budgetInCents: Long = 0,
    val documentInfo: DocumentInfo? = null,
    val notes: String? = null,

    val isSaved: Boolean = false,
    val isSaving: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDatePicker : Boolean = false,

)

sealed class AddEditTravelEvent {
    data class NameChanged(val value: String) : AddEditTravelEvent()
    data class DestinationChanged(val value: String) : AddEditTravelEvent()
    data class IsInternationalChanged(val value: Boolean) : AddEditTravelEvent()
    data class StartDateChanged(val value: LocalDate) : AddEditTravelEvent()
    data class EndDateChanged(val value: LocalDate) : AddEditTravelEvent()
    data class PurposeChanged(val value: String) : AddEditTravelEvent()
    data class BudgetChanged(val value: Long) : AddEditTravelEvent()
    data class NotesChanged(val value: String) : AddEditTravelEvent()
    data class DocumentInfoChanged(val value: DocumentInfo?) : AddEditTravelEvent()
    object ShowDatePicker: AddEditTravelEvent()
    object Save : AddEditTravelEvent()
}

class AddEditTravelViewModel(
    private val repository: TravelRepository,
    private val travelId: Int? = null
): ViewModel(){

    private val _state = MutableStateFlow(AddEditTravelState())
    val state : StateFlow<AddEditTravelState> = _state.asStateFlow()

    init {
        if (travelId != null){
            loadTravels(travelId)
        }
    }

    fun loadTravels(
        travelId: Int
    ){
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null
                    )
                }

                val tempTravel = repository.loadTravelById(travelId)

                _state.update {
                    it.copy(
                        isLoading = false,
                        name = tempTravel!!.name,
                        destination = tempTravel.destination,
                        isInternational = tempTravel.isInternational,
                        startDate = tempTravel.startDate,
                        endDate = tempTravel.endDate,
                        purpose = tempTravel.purpose,
                        budget = tempTravel.budget,
                        notes = tempTravel.notes,
                        documentInfo = tempTravel.documentInfo
                    )
                }
            }
            catch (e: Exception){
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }

        }
    }

    fun onEvent(event: AddEditTravelEvent) {
        when (event) {// is é um type checking, esse objeto event pode assumir varios tipos, o is verifica isso
            is AddEditTravelEvent.NameChanged -> _state.update { it.copy(name = event.value) }
            is AddEditTravelEvent.DestinationChanged -> _state.update { it.copy(destination = event.value) }
            is AddEditTravelEvent.IsInternationalChanged -> _state.update { it.copy(isInternational = event.value) }
            is AddEditTravelEvent.StartDateChanged -> _state.update { it.copy(startDate = event.value) }
            is AddEditTravelEvent.EndDateChanged -> _state.update { it.copy(endDate = event.value) }
            is AddEditTravelEvent.PurposeChanged -> _state.update { it.copy(purpose = event.value) }
            is AddEditTravelEvent.BudgetChanged -> {
                _state.update { it.copy(
                    budget = event.value/100.0,
                    budgetInCents = event.value
                ) }
            }
            is AddEditTravelEvent.DocumentInfoChanged -> {
                val rgOrCpf: String? = event.value?.rgOrCpf?.ifEmpty { null }
                val passportNumber: String? = event.value?.passportNumber?.ifEmpty { null }

                val documentInfo = if (rgOrCpf != null ||  passportNumber != null) DocumentInfo(
                    id = event.value.id,
                    passportNumber = passportNumber,
                    rgOrCpf = rgOrCpf
                ) else null
                Log.d("AddEditTravelViewModel", "rgOrCpf = $rgOrCpf, passportNumber = $passportNumber, documentInfo = $documentInfo")
                _state.update {
                    it.copy(
                        documentInfo = documentInfo
                    )
                }
            }
            is AddEditTravelEvent.NotesChanged -> _state.update { it.copy(notes = event.value.ifEmpty { null }) }
            is AddEditTravelEvent.ShowDatePicker -> _state.update { it.copy(showDatePicker = !it.showDatePicker) }
            is AddEditTravelEvent.Save -> saveTravel()
        }
    }

    private fun saveTravel(){
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(
                        isSaving = true,
                        isSaved = false,
                        error = null,
                    )
                }

                val travel = Travel(
                    id = travelId?: 0,
                    name = _state.value.name,
                    destination = _state.value.destination,
                    isInternational = _state.value.isInternational,
                    startDate = _state.value.startDate,
                    endDate = _state.value.endDate,
                    purpose = _state.value.purpose,
                    budget = _state.value.budget,
                    documentInfo = _state.value.documentInfo,
                    notes = _state.value.notes
                )

                repository.upsertTravelWithList(travel)

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


class AddEditTravelViewModelFactory( // O propósito dele é dizer ao sistema Android (ou, mais especificamente, à biblioteca ViewModel) como criar uma instância do seu TravelListViewModel quando ele precisa de um argumento, no caso, o TravelRepository.
    private val repository: TravelRepository,
    private val travelId: Int? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditTravelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddEditTravelViewModel(repository,travelId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

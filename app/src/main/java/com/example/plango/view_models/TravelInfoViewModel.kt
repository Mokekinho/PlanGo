package com.example.plango.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.plango.database.TravelRepository
import com.example.plango.model.Travel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Async

data class TravelInfoState(
    val travel: Travel? = null,
    val isLoading: Boolean = true, //to know if the data of DB was synchronized
    val error: String? = null,
)


class TravelInfoViewModel(
    private val repository: TravelRepository,
    private val travelId: Int
): ViewModel(){

    private val _state = MutableStateFlow(TravelInfoState())
    val state: StateFlow<TravelInfoState> = _state.asStateFlow()

    init {
        loadTravel(travelId)
    }

    fun loadTravel(
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

                val tempTravel = repository.getTravelById(travelId)

                _state.update {
                    it.copy(
                        isLoading = false,
                        travel = tempTravel,
                        error = null
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
}

class TravelInfoViewModelFactory( // O propósito dele é dizer ao sistema Android (ou, mais especificamente, à biblioteca ViewModel) como criar uma instância do seu TravelListViewModel quando ele precisa de um argumento, no caso, o TravelRepository.
    private val repository: TravelRepository,
    private val travelId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravelInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TravelInfoViewModel(repository,travelId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
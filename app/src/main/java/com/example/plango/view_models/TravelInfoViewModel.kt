package com.example.plango.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.plango.database.TravelRepository
import com.example.plango.model.Travel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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


    val state: StateFlow<TravelInfoState> = repository.observeTravelById(travelId)
        .map { travel -> // essa função map transforma cada valor que o Flow esta emitindo, inicialmente ele esta emitindo um Travel, e ai ele transforma em TravelInfoState.
            TravelInfoState(
                isLoading = false,
                travel = travel,
                error = null
            )
        }

        .onStart { // isso é antes da viagem ser carregada
            emit(TravelInfoState(isLoading = true)) // emit manda valores pro Flow mesmo nao tendo nenhuma alteração no banco, é meio que uma emissão forçada
        }
        .catch { e ->
            emit(TravelInfoState(isLoading = false, error = e.message)) // pegar erros
        }
        .stateIn( // converte um Flow, em StateFlow
            // o StateFlow ele guarda o valor atual, diferente do Flow
            // o Flow somente atualiza os observadores quando é feito uma requisição, ja o StateFlow ele atualiza na hora
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TravelInfoState(isLoading = true)
        )
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
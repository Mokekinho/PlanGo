package com.example.plango.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.plango.database.TravelRepository
import com.example.plango.model.Travel
import com.example.plango.ui.screen.TravelListScreen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// see more on  https://developer.android.com/topic/architecture, https://developer.android.com/topic/libraries/architecture/viewmodel

data class TravelListState( // in the State we define te variables that screen will hold
    val travels: List<Travel> = emptyList(), // List of Travels
    val isLoading: Boolean = true, //to know if the data of DB was synchronized
    val error: String? = null, // shows the error message if an error occurs
)

class TravelListViewModel(
    private val repository: TravelRepository, // this view model will receive just the repository object that holds the access of the DB
) : ViewModel() {

    val state: StateFlow<TravelListState> = repository.getAllTravels()
        .map {
            TravelListState(
                isLoading = false,
                error = null,
                travels = it
            )
        }
        .onStart {
            emit(TravelListState(isLoading = true))
        }
        .catch { e ->
            emit(TravelListState(isLoading = false,error = e.message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TravelListState(isLoading = true)

        )

}

class TravelListViewModelFactory( // O propósito dele é dizer ao sistema Android (ou, mais especificamente, à biblioteca ViewModel) como criar uma instância do seu TravelListViewModel quando ele precisa de um argumento, no caso, o TravelRepository.
    private val repository: TravelRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravelListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TravelListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
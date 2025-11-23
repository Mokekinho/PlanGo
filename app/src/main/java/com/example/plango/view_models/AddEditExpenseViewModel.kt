package com.example.plango.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.util.EMPTY_STRING_ARRAY
import com.example.plango.database.ExpenseEntity
import com.example.plango.database.TravelRepository
import com.example.plango.model.Expense
import com.example.plango.model.Travel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate


data class AddEditExpenseState(
    val description: String = "",           // "Dinner at restaurant"
    val amount: Double = 0.0,
    val category: String = "Food & Drinks",            // "Food", "Transport", "Hotel"
    val date: LocalDate = LocalDate.now(),

    val isSaved: Boolean = false,
    val isSaving: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDatePicker : Boolean = false,
)


sealed class AddEditExpenseEvent{
    data class AmountChanged(val amount: Double): AddEditExpenseEvent()
    data class DescriptionChanged(val description: String): AddEditExpenseEvent()
    data class CategoryChanged(val category: String): AddEditExpenseEvent()
    data class DateChanged(val date: LocalDate): AddEditExpenseEvent()
    object ShowDatePicker: AddEditExpenseEvent()
    object Save: AddEditExpenseEvent()
}

class AddEditExpenseViewModel(
    private val repository: TravelRepository,
    private val travelId: Int,
    private val expenseId: Int? = null
): ViewModel(){

    private val _state = MutableStateFlow(AddEditExpenseState())
    val state : StateFlow<AddEditExpenseState> = _state.asStateFlow()



    init{
        if (expenseId != null){
            loadExpense(
                expenseId
            )
        }
    }


    fun loadExpense(
        expenseId: Int
    ){
        viewModelScope.launch {
            try {

                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null
                    )
                }

                val expense = repository.loadExpenseById(expenseId)

                _state.update {
                    it.copy(
                        isLoading = false,
                        description = expense!!.description,
                        amount = expense.amount,
                        category = expense.category,
                        date = expense.date,
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


    fun onEvent(event: AddEditExpenseEvent) {
        when (event) {
            is AddEditExpenseEvent.DescriptionChanged -> _state.update { it.copy(description = event.description) }
            is AddEditExpenseEvent.AmountChanged -> _state.update { it.copy(amount = event.amount) }
            is AddEditExpenseEvent.CategoryChanged -> _state.update { it.copy(category = event.category) }
            is AddEditExpenseEvent.DateChanged -> _state.update { it.copy(date = event.date) }
            is AddEditExpenseEvent.ShowDatePicker -> _state.update { it.copy(showDatePicker = !it.showDatePicker) }
            is AddEditExpenseEvent.Save -> saveExpense()
        }
    }

    private fun saveExpense(){
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(
                        isSaving = true,
                        isSaved = false,
                        error = null,
                    )
                }

                val expense = Expense(
                    id = expenseId ?: 0,
                    description = _state.value.description,
                    amount = _state.value.amount,
                    date = _state.value.date,
                    category = _state.value.category,
                )

                repository.upsertExpense(expense, travelId)

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


class AddEditExpenseViewModelFactory( // O propósito dele é dizer ao sistema Android (ou, mais especificamente, à biblioteca ViewModel) como criar uma instância do seu TravelListViewModel quando ele precisa de um argumento, no caso, o TravelRepository.
    private val repository: TravelRepository,
    private val travelId: Int,
    private val expenseId: Int? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddEditExpenseViewModel(repository, travelId, expenseId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

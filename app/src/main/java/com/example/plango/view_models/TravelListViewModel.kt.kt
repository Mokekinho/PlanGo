package com.example.plango.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.plango.database.TravelRepository
import com.example.plango.model.Travel
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

    // no tutorial que eu vi sobre view model ele usou uma variavel que era private set, basicamente é isso que as duas fazem, uma serva pra mudar o stado e a outra pra ser observada pela view

    private val _state = MutableStateFlow(TravelListState()) // É onde o ViewModel armazena e modifica o estado interno da tela. O prefixo _ (underscore) é uma convenção para indicar que ela não deve ser acessada ou alterada diretamente de fora do ViewModel.
    val state: StateFlow<TravelListState> = _state.asStateFlow() //É a variável que a View (sua Activity, Fragment ou Composable) observa. Ela expõe o estado atual, mas não permite que a View o modifique, garantindo que apenas o ViewModel possa fazer alterações.


    //MutableStateFlow é um um Flow que  possui um valor inicial (TravelListState()) e que pode ser mudado, ele notifica os receptores somente quando o valor muda para um diferente do anterior.

    // asStateFlow() faz com que essa nova variavel nao seja mudada, apenas vista com .collect()



    init { // inicia a nossa view model ja caregando os dados
        observeTravels() //mudar o nome da Função pq agora nós vamos observar as vigems, ou seja se tiver mudança iremos reagir
    }


    fun observeTravels() {
        viewModelScope.launch { // esse viewModelScope é onde a gente vai lançar nossas coroutinas, isso é util pq nesse caso essa função vai fazer um download, o que é assincrono, além disso, esse escopo faz com que as croutinas sejam cancelas quando nao sao mais uteis, por exemplo, o usuario saiu da tela. O .lauch inicia uma coroutina

            repository.getAllTravels()
                .onStart { // executa antes de coletar, útil pra mostrar um loading.
                    _state.update {
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                }
                .catch { e -> // pega erros
                    _state.update {
                        it.copy(isLoading = false,
                            error = e.message)
                    }
                }
                .collect { travels-> // ainda se refere ao obejto repository.getAllTravels
                    //executa toda vez que os dados no banco mudam (inserção, exclusão, atualização).
                    _state.update {
                        it.copy(
                            isLoading = false,
                            travels = travels,
                            error = null
                        )
                    }
                }
        }
    }
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
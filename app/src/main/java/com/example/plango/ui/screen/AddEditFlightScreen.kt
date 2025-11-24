package com.example.plango.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.plango.database.TravelRepository
import com.example.plango.util.date
import com.example.plango.view_models.AddEditFlightEvent
import com.example.plango.view_models.AddEditFlightViewModel
import com.example.plango.view_models.AddEditFlightViewModelFactory
import java.time.LocalDate

@Composable
@ExperimentalMaterial3Api
fun AddEditFlightScreen(
    navController : NavController,
    repository: TravelRepository,
    travelId : Int,
    flightId: Int? = null
) {


    val viewModel: AddEditFlightViewModel = viewModel(
        factory = AddEditFlightViewModelFactory(repository, travelId, flightId)
    )
    val state by viewModel.state.collectAsState()

    val airline = state.airline
    val flightNumber = state.flightNumber
    val departure = state.departure             // e.g. "GRU Airport"
    val arrival = state.arrival               // e.g. "JFK Airport"
    val departureDate = state.departureDate
    val arrivalDate = state.arrivalDate
    val bookingReference = state.bookingReference



    Scaffold (
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()

                ,
            ) {


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Go Back",
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    navController.popBackStack()
                                }
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(10.dp)
                    )

                    Text(
                        text = if (flightId == null) "New Flight" else "Edit Flight",
                        style = MaterialTheme.typography.headlineLarge,
                    )

                }
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                )
            }

        },
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
            .padding(horizontal = 20.dp)
    ) { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            OutlinedTextField(
                value = airline,
                onValueChange = {
                    viewModel.onEvent(AddEditFlightEvent.AirlineChanged(it))
                },
                label = {
                    Text("Airline")
                },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(
                modifier = Modifier
                    .padding(5.dp)
            )

            OutlinedTextField(
                value = flightNumber,
                onValueChange = {
                    viewModel.onEvent(AddEditFlightEvent.FlightNumberChanged(it))
                },
                label = {
                    Text("Flight Number")
                },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(
                modifier = Modifier
                    .padding(5.dp)
            )

            OutlinedTextField(
                value = departure,
                onValueChange = {
                    viewModel.onEvent(AddEditFlightEvent.DepartureChanged(it))
                },
                label = {
                    Text("Departure")
                },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(
                modifier = Modifier
                    .padding(5.dp)
            )

            OutlinedTextField(
                value = arrival,
                onValueChange = {
                    viewModel.onEvent(AddEditFlightEvent.ArrivalChanged(it))
                },
                label = {
                    Text("Arrival")
                },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(
                modifier = Modifier
                    .padding(5.dp)
            )

            //HORA DE SELECIONAR AS DATAS COM O RANGE DATE PICKER
            val showDepartureDatePicker = state.showDepartureDatePicker

            //mostrar as datas
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = "Departure date: ${date(departureDate)}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                )
                Box(
                    modifier = Modifier.clickable(
                        onClick = {
                            viewModel.onEvent(AddEditFlightEvent.ShowDepartureDatePicker)
                        }
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Change Departure date"
                    )
                }

            }

            Spacer(
                modifier = Modifier
                    .padding(5.dp)
            )


            val departureDateState = rememberDatePickerState()
            if (showDepartureDatePicker) {
                DatePickerDialog(// isso aqui é o componente que faz com que crie um dialogo na tela, ou seja, o nosso calendario ele vai aparecer num dialogo que fica na tela

                    onDismissRequest = { //oq vai acontecer quando o usuario clicar fora
                        viewModel.onEvent(AddEditFlightEvent.ShowDepartureDatePicker)
                    },
                    confirmButton = {// oq vai acontecer quando o usuario confirmar a data
                        // aqui a gente tem que criar o compose do botao
                        TextButton( // é so um texto clicavel basicamente, fica mais bonito
                            onClick = {
                                viewModel.onEvent(AddEditFlightEvent.ShowDepartureDatePicker)
                            }
                        ) {

                            viewModel.onEvent(
                                AddEditFlightEvent.DepartureDateChanged(
                                    departureDateState.selectedDateMillis?.let {
                                        LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000)) // converte milissegundos para dias
                                    } ?: departureDate
                                )
                            )
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                viewModel.onEvent(AddEditFlightEvent.ShowDepartureDatePicker)
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    // aqui a gente coloca o compose do calendario
                    DatePicker(
                        state = departureDateState,
                        title = {
                            //quero nenhum titulo
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)

                    )
                }
            }

            //HORA DE SELECIONAR AS DATAS COM O RANGE DATE PICKER
            val showArrivalDatePicker = state.showArrivalDatePicker

            //mostrar as datas
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = "Arrival date: ${date(arrivalDate)}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                )
                Box(
                    modifier = Modifier.clickable(
                        onClick = {
                            viewModel.onEvent(AddEditFlightEvent.ShowArrivalDatePicker)
                        }
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Change Arrival date"
                    )
                }

            }

            Spacer(
                modifier = Modifier
                    .padding(5.dp)
            )


            val arrivalDateState = rememberDatePickerState()
            if (showArrivalDatePicker) {
                DatePickerDialog(// isso aqui é o componente que faz com que crie um dialogo na tela, ou seja, o nosso calendario ele vai aparecer num dialogo que fica na tela

                    onDismissRequest = { //oq vai acontecer quando o usuario clicar fora
                        viewModel.onEvent(AddEditFlightEvent.ShowArrivalDatePicker)
                    },
                    confirmButton = {// oq vai acontecer quando o usuario confirmar a data
                        // aqui a gente tem que criar o compose do botao
                        TextButton( // é so um texto clicavel basicamente, fica mais bonito
                            onClick = {
                                viewModel.onEvent(AddEditFlightEvent.ShowArrivalDatePicker)
                            }
                        ) {

                            viewModel.onEvent(
                                AddEditFlightEvent.ArrivalDateChanged(
                                    arrivalDateState.selectedDateMillis?.let {
                                        LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000)) // converte milissegundos para dias
                                    } ?: arrivalDate
                                )
                            )
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                viewModel.onEvent(AddEditFlightEvent.ShowArrivalDatePicker)
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    // aqui a gente coloca o compose do calendario
                    DatePicker(
                        state = arrivalDateState,
                        title = {
                            //quero nenhum titulo
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)

                    )
                }
            }



            // Booking Reference
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text(
                    modifier = Modifier,
                    text = "Add Booking Reference",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                )
                Switch(
                    checked = bookingReference != null,
                    onCheckedChange = {
                        viewModel.onEvent(AddEditFlightEvent.BookingReferenceChanged(
                            if(it){
                                ""
                            }
                            else{
                                null
                            }
                            )
                        )
                    }
                )
            }

            if(bookingReference != null){
                OutlinedTextField(
                    value = bookingReference,
                    onValueChange = {
                        viewModel.onEvent(AddEditFlightEvent.BookingReferenceChanged(it))
                    },
                    label = {
                        Text("Booking Reference")
                    },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier
                        .padding(5.dp)
                )
            }

            Button(
                onClick = {
                    viewModel.onEvent(AddEditFlightEvent.Save)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(if (flightId == null) "Add" else "Save")
            }
            if (state.isSaved) {
                LaunchedEffect(Unit) {
                    navController.popBackStack() //volta pro inicio se tiver salvo
                }
            }

        }
    }
}
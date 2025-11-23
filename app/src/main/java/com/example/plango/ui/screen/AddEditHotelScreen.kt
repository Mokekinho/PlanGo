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
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
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
import com.example.plango.util.Date
import com.example.plango.view_models.AddEditHotelEvent
import com.example.plango.view_models.AddEditHotelViewModel
import com.example.plango.view_models.AddEditHotelViewModelFactory
import java.time.LocalDate

@Composable
@ExperimentalMaterial3Api
fun AddEditHotelScreen(
    navController : NavController,
    repository: TravelRepository,
    travelId : Int,
    hotelId: Int? = null
) {


    val viewModel: AddEditHotelViewModel = viewModel(
        factory = AddEditHotelViewModelFactory(repository, travelId, hotelId)
    )
    val state by viewModel.state.collectAsState()

    val name = state.name
    val address = state.address
    val checkIn = state.checkIn
    val checkOut = state.checkOut
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
                        text = if (hotelId == null) "New Hotel" else "Edit Hotel",
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
                value = name,
                onValueChange = {
                    viewModel.onEvent(AddEditHotelEvent.NameChanged(it))
                },
                label = {
                    Text("Name")
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
                value = address,
                onValueChange = {
                    viewModel.onEvent(AddEditHotelEvent.AddressChanged(it))
                },
                label = {
                    Text("Address")
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
            val showDateRangePicker = state.showDateRangePicker

            //mostrar as datas
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = "CheckIn: ${Date(checkIn)}  CheckOut: ${Date(checkOut)}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                )
                Box(
                    modifier = Modifier.clickable(
                        onClick = {
                            viewModel.onEvent(AddEditHotelEvent.ShowDateRangePicker)
                        }
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Change CheckIn and CheckOut Date"
                    )
                }

            }

            Spacer(
                modifier = Modifier
                    .padding(5.dp)
            )


            val dateRangePickerState = rememberDateRangePickerState()
            if (showDateRangePicker) {
                DatePickerDialog(// isso aqui é o componente que faz com que crie um dialogo na tela, ou seja, o nosso calendario ele vai aparecer num dialogo que fica na tela

                    onDismissRequest = { //oq vai acontecer quando o usuario clicar fora
                        viewModel.onEvent(AddEditHotelEvent.ShowDateRangePicker)
                    },
                    confirmButton = {// oq vai acontecer quando o usuario confirmar a data
                        // aqui a gente tem que criar o compose do botao
                        TextButton( // é so um texto clicavel basicamente, fica mais bonito
                            onClick = {
                                viewModel.onEvent(AddEditHotelEvent.ShowDateRangePicker)
                            }
                        ) {

                            viewModel.onEvent(
                                AddEditHotelEvent.CheckInChanged(
                                    dateRangePickerState.selectedStartDateMillis?.let {
                                        LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000)) // converte milissegundos para dias
                                    } ?: checkIn
                                )
                            )
                            viewModel.onEvent(
                                AddEditHotelEvent.CheckOutChanged(
                                    dateRangePickerState.selectedEndDateMillis?.let {
                                        LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000)) // converte milissegundos para dias
                                    } ?: checkOut
                                )
                            )
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                viewModel.onEvent(AddEditHotelEvent.ShowDateRangePicker)
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    // aqui a gente coloca o compose do calendario
                    DateRangePicker(
                        state = dateRangePickerState,
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
                        viewModel.onEvent(AddEditHotelEvent.BookingReferenceChanged(
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
                        viewModel.onEvent(AddEditHotelEvent.BookingReferenceChanged(it))
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
                    viewModel.onEvent(AddEditHotelEvent.Save)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(if (hotelId == null) "Add" else "Save")
            }
            if (state.isSaved) {
                LaunchedEffect(Unit) {
                    navController.popBackStack() //volta pro inicio se tiver salvo
                }
            }

        }
    }
}
package com.example.plango.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.unit.dp
import com.example.plango.model.DocumentInfo
import com.example.plango.model.Expense
import com.example.plango.model.Flight
import com.example.plango.model.Hotel
import com.example.plango.sandbox.DateRangePickerMeu
import com.example.plango.util.Date
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


/**
-----------------------------------------------------
| ← Back        Add/Edit Travel                     |
-----------------------------------------------------
| Name: [___________________________]               |
| Destination: [____________________]               |
| International trip? [ ]                           |
|                                                   |
| Start Date:  [YYYY-MM-DD]  ⏰                     |
| End Date:    [YYYY-MM-DD]  ⏰                     |
|                                                   |
| Purpose: [Vacation / Work / Family Visit / Other] |
| Budget (R$): [__________]                         |
|                                                   |
| Notes:                                            |
| [__________________________________________]      |
|                                                   |
|                   [  Save  ]                      |
-----------------------------------------------------

**/

@Composable
@ExperimentalMaterial3Api
fun AddEditTravelScreen(
    travelId : Int? = null
){

    var name by remember {
        mutableStateOf("")
    }

    var destination by remember {
        mutableStateOf("")
    }          // City or country
    var isInternational by  remember {
        mutableStateOf(false)
    }    // Domestic vs international

    var startDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var endDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var purpose by remember {
        mutableStateOf("")
    }               // "Vacation", "Work", "Family visit"
    var budget by remember {
        mutableDoubleStateOf(0.0)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
            .padding(horizontal = 10.dp),
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text("Name:")
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = destination,
            onValueChange = {
                destination = it
            },
            label = {
                Text("Destination:")
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
            ,
            verticalAlignment = Alignment.CenterVertically,

        ){
            Text(
                modifier = Modifier,
                text = "International",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(
                modifier = Modifier
                    .padding(10.dp)
            )
            Switch(
                checked = isInternational,
                onCheckedChange = {
                    isInternational = it
                }
            )
        }

        //HORA DE SELECIONAR AS DATAS COM O RANGE DATE PICKER
        var showDatePicekr by remember {
            mutableStateOf(false)
        }

        //mostrar as datas
        Row (
            modifier = Modifier
                .fillMaxWidth()
            ,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier,
                text = "Start Date: ${Date(startDate)}  End Date: ${Date(endDate)}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(
                modifier = Modifier
                    .padding(10.dp)
            )
            Box(
                modifier = Modifier.clickable(
                    onClick = {
                        showDatePicekr = true
                    }
                )
            ){
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Change Dates"
                )
            }

        }


        val dateState = rememberDateRangePickerState()
        if(showDatePicekr) {
            DatePickerDialog(// isso aqui é o componente que faz com que crie um dialogo na tela, ou seja, o nosso calendario ele vai aparecer num dialogo que fica na tela

                onDismissRequest = { //oq vai acontecer quando o usuario clicar fora
                    showDatePicekr = false
                },
                confirmButton = {// oq vai acontecer quando o usuario confirmar a data
                    // aqui a gente tem que criar o compose do botao
                    TextButton( // é so um texto clicavel basicamente, fica mais bonito
                        onClick = {
                            //AQUI EU SALVEI AS INFORMAÇÕES DE INICIO E FIM DA VIAGEM NAS VARIAVEIS E FECHEI O DIALOG
                            startDate = dateState.selectedStartDateMillis?.let {
                                LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000)) // converte milissegundos para dias
                            }?: startDate
                            //Salvando as informações do calendario, o que esta no nosso dateState
                            endDate = dateState.selectedEndDateMillis?.let {
                                LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000)) // converte milissegundos para dias
                            } ?: endDate
                            // to transformando isso em LocalDate, pq ele vem em millissegundos desde 1 de janeiro de 1970

                            showDatePicekr = false
                        }
                    ) {
                        Text("Ok")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDatePicekr = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ){
                // aqui a gente coloca o compose do calendario
                DateRangePicker(
                    state = dateState,
                    title = {
                       //quero nenhum titulo
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)

                )
            }
        }

        ExposedDropdownMenuBox() { }
    }
}
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.plango.navigation.HomeNav
import com.example.plango.util.Date
import java.time.LocalDate



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
    navController : NavController,
    travelId : Int? = null,
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
        mutableStateOf("Vacation")
    }               // "Vacation", "Work", "Family visit"
    var budget by remember {
        mutableDoubleStateOf(0.0)
    }
    var notes by remember {
        mutableStateOf("")
    }


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
                        contentDescription = "Change Dates",
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
                        text = "New Travel",
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
                    name = it
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
                value = destination,
                onValueChange = {
                    destination = it
                },
                label = {
                    Text("Destination")
                },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(
                modifier = Modifier
                    .padding(5.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,

                ) {
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

            Spacer(
                modifier = Modifier
                    .padding(5.dp)
            )

            //HORA DE SELECIONAR AS DATAS COM O RANGE DATE PICKER
            var showDatePicker by remember {
                mutableStateOf(false)
            }

            //mostrar as datas
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                            showDatePicker = true
                        }
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Change Dates"
                    )
                }

            }

            Spacer(
                modifier = Modifier
                    .padding(5.dp)
            )


            val dateState = rememberDateRangePickerState()
            if (showDatePicker) {
                DatePickerDialog(// isso aqui é o componente que faz com que crie um dialogo na tela, ou seja, o nosso calendario ele vai aparecer num dialogo que fica na tela

                    onDismissRequest = { //oq vai acontecer quando o usuario clicar fora
                        showDatePicker = false
                    },
                    confirmButton = {// oq vai acontecer quando o usuario confirmar a data
                        // aqui a gente tem que criar o compose do botao
                        TextButton( // é so um texto clicavel basicamente, fica mais bonito
                            onClick = {
                                //AQUI EU SALVEI AS INFORMAÇÕES DE INICIO E FIM DA VIAGEM NAS VARIAVEIS E FECHEI O DIALOG
                                startDate = dateState.selectedStartDateMillis?.let {
                                    LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000)) // converte milissegundos para dias
                                } ?: startDate
                                //Salvando as informações do calendario, o que esta no nosso dateState
                                endDate = dateState.selectedEndDateMillis?.let {
                                    LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000)) // converte milissegundos para dias
                                } ?: endDate
                                // to transformando isso em LocalDate, pq ele vem em millissegundos desde 1 de janeiro de 1970

                                showDatePicker = false
                            }
                        ) {
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDatePicker = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
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


            // TIPOS DE VIAGEM
            var expanded by remember { mutableStateOf(false) } // controla se o dropdown está aberto
            val options = listOf("Vacation", "Work", "Family Visit", "Other")

            // NAO ACHEI MUITA DOCUMENTAÇÃO ENTAO SO COPIEI DO CHAT GPT
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = purpose,
                    onValueChange = {},
                    readOnly = true, // usuário não digita, só seleciona
                    label = { Text("Purpose") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable) // necessário para o dropdown, mostra onde o menu esta fixado, nesse caso no OutlinedtextField
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.exposedDropdownSize()
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                purpose = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .padding(5.dp)
            )

            //BUDGET
            var budgetText by remember {
                mutableStateOf<String>("")
            }

            OutlinedTextField(
                value = budgetText,
                onValueChange = {
                    budgetText = it

                    budget = budgetText.toDoubleOrNull() ?: 0.0
                },
                label = {
                    Text(
                        "Budget"
                    )
                },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(
                modifier = Modifier
                    .padding(5.dp)
            )

            OutlinedTextField(
                value = notes,
                onValueChange = {
                    notes = it
                },
                label = {
                    Text("Notes")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(
                modifier = Modifier
                    .padding(5.dp)
            )

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Add")
            }
        }
    }
}
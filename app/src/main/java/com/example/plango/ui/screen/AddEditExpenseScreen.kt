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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.plango.database.TravelRepository
import com.example.plango.util.date
import com.example.plango.util.money
import com.example.plango.view_models.AddEditExpenseEvent
import com.example.plango.view_models.AddEditExpenseViewModel
import com.example.plango.view_models.AddEditExpenseViewModelFactory
import com.example.plango.view_models.AddEditTravelEvent
import java.time.LocalDate

@Composable
@ExperimentalMaterial3Api
fun AddEditExpenseScreen(
    navController : NavController,
    repository: TravelRepository,
    travelId : Int,
    expenseId: Int? = null
) {


    val viewModel: AddEditExpenseViewModel = viewModel(
        factory = AddEditExpenseViewModelFactory(repository, travelId, expenseId)
    )
    val state by viewModel.state.collectAsState()

    val description = state.description
    val amount = state.amount
    val category = state.category
    val date = state.date
    val budgetInCents = state.budgetInCents


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
                        text = if (expenseId == null) "New Expense" else "Edit Expense",
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
                value = description,
                onValueChange = {
                    viewModel.onEvent(AddEditExpenseEvent.DescriptionChanged(it))
                },
                label = {
                    Text("Description")
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
            val showDatePicker = state.showDatePicker

            //mostrar as datas
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = "date: ${date(date)}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                )
                Box(
                    modifier = Modifier.clickable(
                        onClick = {
                            viewModel.onEvent(AddEditExpenseEvent.ShowDatePicker)
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


            val dateState = rememberDatePickerState()
            if (showDatePicker) {
                DatePickerDialog(// isso aqui é o componente que faz com que crie um dialogo na tela, ou seja, o nosso calendario ele vai aparecer num dialogo que fica na tela

                    onDismissRequest = { //oq vai acontecer quando o usuario clicar fora
                        viewModel.onEvent(AddEditExpenseEvent.ShowDatePicker)
                    },
                    confirmButton = {// oq vai acontecer quando o usuario confirmar a data
                        // aqui a gente tem que criar o compose do botao
                        TextButton( // é so um texto clicavel basicamente, fica mais bonito
                            onClick = {

                                viewModel.onEvent(AddEditExpenseEvent.ShowDatePicker)
                            }
                        ) {

                            viewModel.onEvent(
                                AddEditExpenseEvent.DateChanged(
                                    dateState.selectedDateMillis?.let {
                                        LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000)) // converte milissegundos para dias
                                    } ?: date
                                )
                            )
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                viewModel.onEvent(AddEditExpenseEvent.ShowDatePicker)
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    // aqui a gente coloca o compose do calendario
                    DatePicker(
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
            val options = listOf(
                "Food & Drinks",
                "Transportation",
                "Accommodation",
                "Activities & Entertainment",
                "Shopping",
                "Health & Insurance",
                "Communication",
                "Fees & Miscellaneous"
            )

            // NAO ACHEI MUITA DOCUMENTAÇÃO ENTAO SO COPIEI DO CHAT GPT
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true, // usuário não digita, só seleciona
                    label = { Text("Category") },
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
                                viewModel.onEvent(AddEditExpenseEvent.CategoryChanged(option))
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


//Editar o Budget

            OutlinedTextField(
                value = money((budgetInCents/100.0)),
                onValueChange = { value ->
                    viewModel.onEvent(
                        AddEditExpenseEvent.AmountChanged(
                            value.filter {
                                it.isDigit()
                            }.toLong()
                        )
                    )
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

            Button(
                onClick = {
                    viewModel.onEvent(AddEditExpenseEvent.Save)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(if (expenseId == null) "Add" else "Save")
            }
            if (state.isSaved) {
                LaunchedEffect(Unit) {
                    navController.popBackStack() //volta pro inicio se tiver salvo
                }
            }

        }
    }
}
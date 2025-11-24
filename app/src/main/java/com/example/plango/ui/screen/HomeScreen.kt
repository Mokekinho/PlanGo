package com.example.plango.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.plango.database.TravelRepository
import com.example.plango.navigation.AddEditTravelNav

@Composable
@ExperimentalMaterial3Api
fun HomeScreen(
    navController: NavController,
    repository: TravelRepository
){
    // estrutura da tela
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 30.dp)
            .padding(horizontal = 10.dp)
        ,
        topBar = {
            var expanded by remember { mutableStateOf(false) }
            var query by remember { mutableStateOf("") }

            SearchBar(
                modifier = Modifier
                    .fillMaxWidth(),
                expanded = expanded,
                onExpandedChange = { expanded = it },
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = { query = it },
                        onSearch = {
                            /* não faz nada */
                            expanded = false
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text("Search") }
                    )
                }
            ) {
                // Conteúdo que aparece quando expanded = true
                // Deixa vazio
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AddEditTravelNav(null))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Travel"
                )
            }
        }
    ) { innerPadding ->
        TravelListScreen(
            innerPadding,
            navController,
            repository
        )
    }
}
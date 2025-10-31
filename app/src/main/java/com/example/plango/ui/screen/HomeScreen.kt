package com.example.plango.ui.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.plango.database.TravelRepository
import com.example.plango.model.Travel
import com.example.plango.navigation.Routes
import com.google.gson.Gson

@Composable
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
            Text(
                "BARRA DE CIMA SO PRA VER UMA COISA",
                fontSize = 20.sp
            )
        }
    ) { innerPadding ->
        TravelListScreen(
            innerPadding,
            navController,
            repository
        )
    }
}
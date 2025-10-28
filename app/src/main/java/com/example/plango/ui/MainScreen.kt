package com.example.plango.ui

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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.plango.model.Travel
import com.example.plango.navigation.Routes
import com.example.plango.ui.screen.TravelInfoScreen
import com.example.plango.ui.screen.TravelListScreen
import com.google.gson.Gson

@Composable
fun MainScreen(){
// 1️⃣Cria o controlador de navegação (precisa estar alto na hierarquia)
    val navController = rememberNavController()


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
    innerPadding
        // 3️O NavHost vai dentro do Scaffold, dentro do content
        NavHost(
            navController = navController,
            startDestination = Routes.TRAVELS
        ) {
            // Tela inicial
            composable(Routes.TRAVELS) {
                TravelListScreen(
                    innerPadding =  innerPadding,
                    navController = navController,
                )
            }

            composable(
                route = "${Routes.TRAVELS_INFO}/{travelJson}",
                arguments = listOf(navArgument("travelJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val travelJson = backStackEntry.arguments?.getString("travelJson")
                val travel = Gson().fromJson(Uri.decode(travelJson), Travel::class.java)
                TravelInfoScreen(innerPadding,travel)
            }

            // Outra tela de exemplo
            //composable("details") {
                //DetailsScreen(navController)
            //}
        }

    }




}
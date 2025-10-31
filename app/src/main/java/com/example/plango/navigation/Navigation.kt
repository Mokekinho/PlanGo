package com.example.plango.navigation

import android.net.Uri
import android.net.wifi.hotspot2.pps.HomeSp
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.plango.model.Travel
import com.example.plango.ui.screen.HomeScreen
import com.example.plango.ui.screen.TravelInfoScreen
import com.example.plango.ui.screen.TravelListScreen
import com.google.gson.Gson
import kotlinx.serialization.Serializable


@Composable
fun AppNavigation(){

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeScreenNav
    ) {
        // Tela inicial
        composable<HomeScreenNav> {
            HomeScreen(
                navController
            )
        }

        //por enquanto vai ficar assim, no futuro quando eu terminar o banco de dados eu passo so o id
        composable(
            route = "${Routes.TRAVELS_INFO}/{travelJson}",
            arguments = listOf(navArgument("travelJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val travelJson = backStackEntry.arguments?.getString("travelJson")
            val travel = Gson().fromJson(Uri.decode(travelJson), Travel::class.java)
            TravelInfoScreen(travel)
        }

        // Outra tela de exemplo
        //composable("details") {
        //DetailsScreen(navController)
        //}
    }

}

@Serializable
object HomeScreenNav
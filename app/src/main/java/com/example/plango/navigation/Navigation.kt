package com.example.plango.navigation

import android.net.Uri
import android.net.wifi.hotspot2.pps.HomeSp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.plango.database.TravelRepository
import com.example.plango.model.Travel
import com.example.plango.ui.screen.AddEditExpenseScreen
import com.example.plango.ui.screen.AddEditFlightScreen
import com.example.plango.ui.screen.AddEditHotelScreen
import com.example.plango.ui.screen.AddEditTravelScreen
import com.example.plango.ui.screen.HomeScreen
import com.example.plango.ui.screen.TravelInfoScreen
import com.example.plango.ui.screen.TravelListScreen
import com.example.plango.view_models.TravelInfoViewModel
import com.example.plango.view_models.TravelInfoViewModelFactory
import com.google.gson.Gson
import kotlinx.serialization.Serializable


@Composable
@ExperimentalMaterial3Api
fun AppNavigation(
    repository: TravelRepository
){

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeNav
    ) {
        // Tela inicial
        composable<HomeNav> {
            HomeScreen(
                navController,
                repository
            )
        }

        //por enquanto vai ficar assim, no futuro quando eu terminar o banco de dados eu passo so o id
        composable<TravelInfoNav> {
            val args : TravelInfoNav = it.toRoute()

            val viewModel: TravelInfoViewModel = viewModel(
                factory = TravelInfoViewModelFactory(repository, args.id)
            )

            TravelInfoScreen(
                navController = navController,
                travelId = args.id,
                viewModel = viewModel // Passe como parâmetro

            )
        }

        composable<AddEditTravelNav> {
            val args : AddEditTravelNav = it.toRoute()
            AddEditTravelScreen(
                navController,
                repository,
                args.travelId
            )
        }

        composable<AddEditExpenseNav>{
            val args : AddEditExpenseNav = it.toRoute()

            AddEditExpenseScreen(
                navController,
                repository,
                args.travelId,
                args.expenseId
            )
        }

        composable<AddEditFlightNav>{
            val args : AddEditFlightNav = it.toRoute()

            AddEditFlightScreen(
                navController,
                repository,
                args.travelId,
                args.flightId
            )
        }

        composable<AddEditHotelNav>{
            val args : AddEditHotelNav = it.toRoute()
            AddEditHotelScreen(
                navController,
                repository,
                args.travelId,
                args.hotelId
            )
        }


        // Outra tela de exemplo
        //composable("details") {
        //DetailsScreen(navController)
        //}
    }

}

@Serializable
object HomeNav

@Serializable
data class TravelInfoNav(
    val id : Int
)

@Serializable
data class AddEditTravelNav(
    val travelId: Int? = null
)

@Serializable
data class AddEditExpenseNav(
    val travelId: Int,
    val expenseId: Int? = null
)

@Serializable
data class AddEditFlightNav(
    val travelId: Int,
    val flightId: Int? = null
)

@Serializable
data class AddEditHotelNav(
    val travelId: Int,
    val hotelId: Int? = null
)
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
import com.example.plango.database.TravelRepository
import com.example.plango.model.Travel
import com.example.plango.navigation.AppNavigation
import com.example.plango.navigation.Routes
import com.example.plango.ui.screen.TravelInfoScreen
import com.example.plango.ui.screen.TravelListScreen
import com.google.gson.Gson

@Composable
fun MainScreen(
    repository: TravelRepository
){
    AppNavigation(repository)
}
package com.example.plango

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.compose.PlanGoTheme
import com.example.plango.model.DocumentInfo
import com.example.plango.model.Expense
import com.example.plango.model.Flight
import com.example.plango.model.Hotel
import com.example.plango.model.Travel
import com.example.plango.sandbox.FakeMainScreen
import com.example.plango.ui.MainScreen
import com.example.plango.ui.screen.TravelInfoScreen
import java.time.LocalDate


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlanGoTheme {
                MainScreen()
            }
        }
    }
}

/**
@Composable
fun Teste(){
    TravelInfoScreen(
        innerPadding = //passsasr,
        travel = Travel(
            id = 8,
            name = "Conference in New York",
            destination = "New York, USA",
            isInternational = true,
            startDate = LocalDate.of(2025, 9, 10),
            endDate = LocalDate.of(2025, 9, 15),
            purpose = "Work",
            budget = 4000.0,
            expenses = listOf(
                Expense(11, "Subway card", 40.0, "Transport", LocalDate.of(2025, 9, 11)),
                Expense(12, "Lunch near Times Square", 60.0, "Food", LocalDate.of(2025, 9, 12))
            ),
            flights = listOf(
                Flight(7, "Delta Airlines", "DL105", "GRU Airport", "JFK Airport",
                    LocalDate.of(2025, 9, 9), LocalDate.of(2025, 9, 10), "NY4455")
            ),
            hotels = listOf(
                Hotel(8, "Marriott Marquis", "1535 Broadway, New York",
                    LocalDate.of(2025, 9, 10), LocalDate.of(2025, 9, 15), "NY9988")
            ),
            documentInfo = DocumentInfo(passportNumber = "XP223344"),
            notes = "Conference at Javits Center. Free day to visit Central Park."
        )
    )
}

 **/

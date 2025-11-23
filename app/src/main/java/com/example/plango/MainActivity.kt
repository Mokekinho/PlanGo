package com.example.plango

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.room.Room
import com.example.plango.ui.theme.PlanGoTheme
import com.example.plango.database.AppDatabase
import com.example.plango.database.TravelRepository

import com.example.plango.ui.MainScreen


class MainActivity : ComponentActivity() {


    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "travel_database"
        ).build()
    }

    private val repository by lazy {
        TravelRepository(db.travelDao())
    }

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("onCreate")
        enableEdgeToEdge()
        setContent {
            PlanGoTheme {
                MainScreen(repository)

            }
        }
    }
    // we can override the other functions of the lifecycle
    override fun onStart() {
        // The Start function the user can see, but he can´t interact
        super.onStart()
        //just to see with the logcat
        println("onStart")

    }

    override fun onResume() {
        super.onResume()
        println("onResume")
    }

    override fun onPause() {
        super.onPause()
        println("onPause")
    }

    override fun onStop() {
        super.onStop()
        println("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        println("onRestart")
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

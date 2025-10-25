package com.example.plango.ui.screen

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.plango.model.Travel
import com.example.plango.util.Date
import com.example.plango.util.Money


/**
 * ---------------------------------------------------
 * | ← Back             Summer in Paris              |
 * |               Paris, France 🌍 International    |
 * |              Jun 10 - Jun 20 · Vacation         |
 * ---------------------------------------------------
 * | 💰 Budget Overview                             |
 * |  Total Budget: $2,000                          |
 * |  Spent: $1,450                                 |
 * |  Remaining: $550                               |
 * |  [███████████-----] 72% spent                  |
 * ---------------------------------------------------
 * | 📋 Expenses                                    |
 * |  🍽 Dinner at Le Jules Verne  -  $120 (Food)   |
 * |  🚖 Taxi to hotel            -  $40 (Transport)|
 * |  🛍 Souvenirs                -  $85 (Shopping) |
 * |  ...                                             |
 * |  [ + Add Expense ]                             |
 * ---------------------------------------------------
 * | ✈️ Flights                                     |
 * |  Air France AF1234                             |
 * |  GRU Airport → CDG Airport                     |
 * |  10 Jun - 11 Jun                               |
 * |  Ref: AB12CD                                   |
 * ---------------------------------------------------
 * | 🏨 Hotels                                      |
 * |  Hotel Le Meurice                              |
 * |  Check-in: 11 Jun  |  Check-out: 20 Jun        |
 * |  Ref: HT5678                                    |
 * ---------------------------------------------------
 * | 🧾 Documents                                   |
 * |  Passport: 123456789                           |
 * |  Notes: “Remember to renew passport.”          |
 * ---------------------------------------------------
 * | 🗒 Additional Notes                            |
 * |  “Visit Louvre on Day 2”                       |
 * |  “Try croissants at Du Pain et des Idées”      |
 * ---------------------------------------------------
 * **/




@Composable
fun TravelInfoScreen(
     travel: Travel
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 30.dp)
    ) {
        //Title
        Text(
            text = travel.name,
            style = MaterialTheme.typography.displayLarge,
        )

        // Basic Information
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(
                    text = travel.destination,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(
                    modifier = Modifier
                        .padding(5.dp)
                        .size(20.dp)
                        .background(color = if(travel.isInternational) Color.Green else Color.Blue)
                )
                Text(
                    text = if(travel.isInternational) "International" else "National",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = Date(travel.startDate) + " - " + Date(travel.endDate) + " - " + travel.purpose,
                    style = MaterialTheme.typography.headlineSmall
                )

            }



        }
        // To separe items
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .background(color = MaterialTheme.colorScheme.primary)
                //.background(color = Color.DarkGray)
        )

        //Budget overview
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = "Budget overview",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Total Budget: " + Money(travel.budget),
                style = MaterialTheme.typography.headlineSmall
            )
            val totalSpent = travel.expenses.sumOf { it.amount } //This function sumOf iterates over the list and it is summing the amounts
            Text(
                text = "Spent: " + Money(totalSpent),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Remaining: " + Money(travel.budget - totalSpent),
                style = MaterialTheme.typography.headlineSmall
            )

        }

    }
}
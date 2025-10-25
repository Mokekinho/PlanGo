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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            modifier =
                Modifier
                    .padding(horizontal = 10.dp)
            ,
            text = travel.name,
            style = MaterialTheme.typography.headlineLarge,
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
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(
                    modifier = Modifier
                        .padding(5.dp)
                        .size(20.dp)
                        .background(color = if (travel.isInternational) Color.Green else Color.Blue)
                )
                Text(
                    text = if(travel.isInternational) "International" else "National",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = Date(travel.startDate) + " - " + Date(travel.endDate) + " - " + travel.purpose,
                    style = MaterialTheme.typography.bodyLarge
                )

            }



        }
        // To separe items
        Spacer(
            modifier = Modifier
                .padding(vertical = 10.dp)
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
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Total Budget: " + Money(travel.budget),
                style = MaterialTheme.typography.bodyLarge
            )
            val totalSpent = travel.expenses.sumOf { it.amount } //This function sumOf iterates over the list and it is summing the amounts
            Text(
                text = "Spent: " + Money(totalSpent),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Remaining: " + Money(travel.budget - totalSpent),
                style = MaterialTheme.typography.bodyLarge
            )

        }

        // To separe items
        Spacer(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
                .height(5.dp)
                .background(color = MaterialTheme.colorScheme.primary)
            //.background(color = Color.DarkGray)
        )

        //Expenses
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(travel.expenses){ expenseItem ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxWidth()
                            ,
                            text = expenseItem.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                            ,
                            text = Money(expenseItem.amount),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                            ,
                            text = expenseItem.category,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                }

            }


            Button(
                onClick = {
                    //click
                }
            ) {
                Text(
                    text = "+ item",
                    fontSize = 20.sp,
                )
            }
        }

        // To separe items
        Spacer(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
                .height(5.dp)
                .background(color = MaterialTheme.colorScheme.primary)
            //.background(color = Color.DarkGray)
        )

        // Flights

    }
}
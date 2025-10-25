package com.example.plango.ui.screen

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plango.model.Flight
import com.example.plango.model.Hotel
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
     // ← this is the correct one
    Surface( // isso aqui ajuda nas cores ficar certas, estudar sobre
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
                .verticalScroll(rememberScrollState())// ← this is the correct one

        ) {
            //Title
            Text(
                modifier =
                    Modifier
                        .padding(horizontal = 10.dp),
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
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
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
                        text = if (travel.isInternational) "International" else "National",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
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
                val totalSpent =
                    travel.expenses.sumOf { it.amount } //This function sumOf iterates over the list and it is summing the amounts
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
                Text(
                    text = "Expenses",
                    style = MaterialTheme.typography.headlineSmall
                )

                travel.expenses.forEach { expenseItem -> // nesse caso é necessario usar foreach pq nad da pra por lazycolumn dentro de lazy column se nao da problema

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxWidth(),
                            text = expenseItem.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            text = Money(expenseItem.amount),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            text = expenseItem.category,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }


                Button(
                    onClick = {
                        //click
                    }
                ) {
                    Text(
                        text = "+ Add Expense",
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "Flights",
                    style = MaterialTheme.typography.headlineSmall
                )
                travel.flights.forEach { flightItem ->
                    FlightCard(flightItem)
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

            //Hotels
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {

                Text(
                    text = "Hotels",
                    style = MaterialTheme.typography.headlineSmall
                )
                travel.hotels.forEach { hotelItem ->
                    HotelCard(hotelItem)
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

            //Documents
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                travel.documentInfo?.let { doc -> // Basicamente se documentInfo existir ele vai ser chamado de doc e vai executar alguma coisa, assim que o let funciona, o ?. verifica se é null ou nao
                    Text(
                        text = "Documentation",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    doc.passportNumber?.let {
                        Text(
                            text = "Passport: $it",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    doc.rgOrCpf?.let {
                        Text(
                            text = "RG or CPF: $it",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
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

            //Additional Notes
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {

                travel.notes?.let { note ->
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = note,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

        }
    }
}




@Composable
fun FlightCard(
    flight: Flight
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(10.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = flight.airline + " " + flight.flightNumber,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = flight.departure + " → " + flight.arrival,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = Date(flight.departureDate) + " → " + Date(flight.arrivalDate),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Ref: " + flight.bookingReference,
                style = MaterialTheme.typography.bodyLarge

            )
        }

    }


}


@Composable
fun HotelCard(
    hotel: Hotel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = hotel.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = hotel.address,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Check-in: " + Date(hotel.checkIn) + " | " + "Check-out: " + Date(hotel.checkOut),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Ref: " + hotel.bookingReference,
                style = MaterialTheme.typography.bodyLarge

            )

        }
    }
}
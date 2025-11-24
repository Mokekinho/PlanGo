package com.example.plango.ui.screen

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.plango.R
import com.example.plango.model.Flight
import com.example.plango.model.Hotel
import com.example.plango.model.Travel
import com.example.plango.navigation.AddEditExpenseNav
import com.example.plango.navigation.AddEditFlightNav
import com.example.plango.navigation.AddEditHotelNav
import com.example.plango.navigation.AddEditTravelNav
import com.example.plango.util.date
import com.example.plango.util.money
import com.example.plango.view_models.TravelInfoViewModel


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
    navController: NavController,
    travelId: Int,
    viewModel: TravelInfoViewModel
) {

    val state by viewModel.state.collectAsState()

    when { // TODO colocar esse When dentro do Scaffold
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Text("Error: ${state.error}")
        }

        else -> {

            val travel: Travel = state.travel!!

            Scaffold(
                topBar = {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                    ) {


                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Go Back",
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            navController.popBackStack()
                                        }
                                    )
                            )
                            Spacer(
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                            // TITLE
                            Text(
                                text = travel.name,
                                style = MaterialTheme.typography.headlineLarge,
                            )
                            Spacer(
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Content",
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            navController.navigate(AddEditTravelNav(travelId))
                                        }
                                    )
                            )

                        }
                        Spacer(
                            modifier = Modifier
                                .padding(10.dp)
                        )
                    }

                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp)
                    .padding(horizontal = 20.dp)
            ) { innerPadding ->


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())// ← this is the correct one

                ) {
                    //Title
                    val columnModifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 5.dp,
                            bottom = 5.dp
                        )
                    // Basic Information
                    Column(
                        modifier = columnModifier
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
                            Icon(
                                painter = painterResource(id = if(travel.isInternational) R.drawable.globe else R.drawable.flag),
                                contentDescription = "A Globe for international trips or a flag for national trips"
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
                                text = date(travel.startDate) + " - " + date(travel.endDate) + " - " + travel.purpose,
                                style = MaterialTheme.typography.bodyLarge
                            )

                        }


                    }
                    // To separe items
                    HorizontalDivider()

                    //Budget overview
                    Column(
                        modifier = columnModifier
                    ) {
                        Text(
                            text = "Budget overview",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Total Budget: " + money(travel.budget),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        val totalSpent =
                            travel.expenses.sumOf { it.amount } //This function sumOf iterates over the list and it is summing the amounts
                        Text(
                            text = "Spent: " + money(totalSpent),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Remaining: " + money(travel.budget - totalSpent),
                            style = MaterialTheme.typography.bodyLarge
                        )

                    }

                    // To separe items
                    HorizontalDivider()


                    //Expenses
                    Column(
                        modifier = columnModifier
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
                                    text = money(expenseItem.amount),
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


                        TextButton(
                            onClick = {
                                navController.navigate(
                                    AddEditExpenseNav(
                                        travelId
                                    )
                                )
                            }
                        ) {
                            Text(
                                text = "+ Add Expense",
                                fontSize = 15.sp,
                            )
                        }
                    }

                    // To separe items
                    HorizontalDivider()

                    // Flights
                    Column(
                        modifier = columnModifier
                    ) {
                        Text(
                            text = "Flights",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        travel.flights.forEach { flightItem ->
                            FlightCard(flightItem)
                        }

                        TextButton(
                            onClick = {
                                navController.navigate(
                                    AddEditFlightNav(
                                        travelId
                                    )
                                )
                            }
                        ) {
                            Text(
                                text = "+ Add Flight",
                                fontSize = 15.sp,
                            )
                        }


                    }

                    // To separe items
                    HorizontalDivider()

                    //Hotels
                    Column(
                        modifier = columnModifier
                    ) {

                        Text(
                            text = "Hotels",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        travel.hotels.forEach { hotelItem ->
                            HotelCard(hotelItem)
                        }

                        TextButton(
                            onClick = {
                                navController.navigate(
                                    AddEditHotelNav(
                                        travelId
                                    )
                                )
                            }
                        ) {
                            Text(
                                text = "+ Add Hotel",
                                fontSize = 15.sp,
                            )
                        }


                    }

                    // To separe items
                    HorizontalDivider()

                    //Documents
                    Column(
                        modifier = columnModifier
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
                    HorizontalDivider()

                    //Additional Notes
                    Column(
                        modifier = columnModifier
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
                            Spacer(
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                        }
                    }
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
                text = date(flight.departureDate) + " → " + date(flight.arrivalDate),
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
                text = "Check-in: " + date(hotel.checkIn) + " | " + "Check-out: " + date(hotel.checkOut),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Ref: " + hotel.bookingReference,
                style = MaterialTheme.typography.bodyLarge

            )

        }
    }
}
package com.example.plango.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.plango.model.DocumentInfo
import com.example.plango.model.Expense
import com.example.plango.model.Flight
import com.example.plango.model.Hotel
import com.example.plango.model.Travel
import com.example.plango.util.Date
import com.example.plango.util.Money
import java.nio.file.WatchEvent
import java.time.LocalDate

import android.net.Uri
import com.example.plango.database.TravelRepository
import com.example.plango.navigation.Routes
import com.google.gson.Gson



/**
 *
 * ChatGpt idea for what show in this screen
 *
 * --------------------------------------------------
 * [🌎] Vacation in Paris
 *       Paris, France
 *       Jun 5 – Jun 15, 2025
 *       Budget: $2500   Spent: $180
 *       [progress bar]
 * --------------------------------------------------
 *
 */


@Composable
fun TravelListScreen(
    innerPadding : PaddingValues,
    navController: NavController,//preciso do navController para fazer navegações
    repository: TravelRepository
){
    val travels = listOf(
        Travel(
            id = 1,
            name = "Vacation in Paris",
            destination = "Paris, France",
            isInternational = true,
            startDate = LocalDate.of(2025, 6, 5),
            endDate = LocalDate.of(2025, 6, 15),
            purpose = "Vacation",
            budget = 2500.0,
            expenses = listOf(
                Expense(1, "Eiffel Tower Ticket", 30.0, "Sightseeing", LocalDate.of(2025, 6, 6)),
                Expense(2, "Dinner at Le Jules Verne", 150.0, "Food", LocalDate.of(2025, 6, 7))
            ),
            flights = listOf(
                Flight(1, "Air France", "AF456", "GRU Airport", "CDG Airport",
                    LocalDate.of(2025, 6, 5), LocalDate.of(2025, 6, 6), "BR12345")
            ),
            hotels = listOf(
                Hotel(1, "Hotel Le Meurice", "228 Rue de Rivoli, Paris",
                    LocalDate.of(2025, 6, 5), LocalDate.of(2025, 6, 15), "HT5678")
            ),
            documentInfo = DocumentInfo(passportNumber = "XP123456"),
            notes = "Visit Louvre and Versailles."
        ),

        Travel(
            id = 2,
            name = "Business Trip to São Paulo",
            destination = "São Paulo, Brazil",
            isInternational = false,
            startDate = LocalDate.of(2025, 3, 10),
            endDate = LocalDate.of(2025, 3, 12),
            purpose = "Work",
            budget = 800.0,
            expenses = listOf(
                Expense(3, "Taxi to Meeting", 50.0, "Transport", LocalDate.of(2025, 3, 11))
            ),
            flights = emptyList(),
            hotels = listOf(
                Hotel(2, "Hilton São Paulo", "Av. das Nações Unidas, 12901",
                    LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 12), "SP8901")
            ),
            documentInfo = DocumentInfo(rgOrCpf = "123.456.789-10"),
            notes = "Client meeting at Av. Paulista."
        ),

        Travel(
            id = 3,
            name = "Family Holiday in Tokyo",
            destination = "Tokyo, Japan",
            isInternational = true,
            startDate = LocalDate.of(2025, 12, 20),
            endDate = LocalDate.of(2026, 1, 5),
            purpose = "Family Vacation",
            budget = 5000.0,
            expenses = emptyList(),
            flights = listOf(
                Flight(2, "ANA", "NH190", "LAX Airport", "HND Airport",
                    LocalDate.of(2025, 12, 19), LocalDate.of(2025, 12, 20), "JP5566")
            ),
            hotels = listOf(
                Hotel(3, "Shinjuku Granbell Hotel", "2-14-5 Kabukicho, Shinjuku",
                    LocalDate.of(2025, 12, 20), LocalDate.of(2026, 1, 5), "TK1122")
            ),
            documentInfo = DocumentInfo(passportNumber = "AA987654"),
            notes = "Celebrate New Year in Shibuya Crossing."
        ),

        Travel(
            id = 4,
            name = "Beach Weekend in Rio",
            destination = "Rio de Janeiro, Brazil",
            isInternational = false,
            startDate = LocalDate.of(2025, 2, 14),
            endDate = LocalDate.of(2025, 2, 16),
            purpose = "Relax",
            budget = 1200.0,
            expenses = listOf(
                Expense(4, "Beach Kiosk Snacks", 80.0, "Food", LocalDate.of(2025, 2, 15))
            ),
            flights = emptyList(),
            hotels = listOf(
                Hotel(4, "Copacabana Palace", "Avenida Atlântica, 1702",
                    LocalDate.of(2025, 2, 14), LocalDate.of(2025, 2, 16), "RJ4455")
            ),
            documentInfo = DocumentInfo(rgOrCpf = "987.654.321-00"),
            notes = "Plan to visit Sugarloaf Mountain."
        ),
        Travel(
            id = 5,
            name = "Road Trip to Brasília",
            destination = "Brasília, Brazil",
            isInternational = false,
            startDate = LocalDate.of(2025, 5, 1),
            endDate = LocalDate.of(2025, 5, 5),
            purpose = "Tourism",
            budget = 2000.0,
            expenses = listOf(
                Expense(5, "Gasoline", 400.0, "Transport", LocalDate.of(2025, 5, 1)),
                Expense(6, "Dinner at local restaurant", 120.0, "Food", LocalDate.of(2025, 5, 2))
            ),
            flights = emptyList(),
            hotels = listOf(
                Hotel(5, "Hotel Nacional", "Setor Hoteleiro Sul, Brasília",
                    LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 5), "BSB2025")
            ),
            documentInfo = DocumentInfo(rgOrCpf = "123.987.654-00"),
            notes = "Visit Congresso Nacional and Catedral Metropolitana."
        ),

        Travel(
            id = 6,
            name = "Ski Trip to Switzerland",
            destination = "Zermatt, Switzerland",
            isInternational = true,
            startDate = LocalDate.of(2025, 12, 10),
            endDate = LocalDate.of(2025, 12, 20),
            purpose = "Vacation",
            budget = 8000.0,
            expenses = listOf(
                Expense(7, "Ski pass", 500.0, "Entertainment", LocalDate.of(2025, 12, 11)),
                Expense(8, "Fondue dinner", 200.0, "Food", LocalDate.of(2025, 12, 12))
            ),
            flights = listOf(
                Flight(5, "Swiss Air", "LX93", "GRU Airport", "ZRH Airport",
                    LocalDate.of(2025, 12, 9), LocalDate.of(2025, 12, 10), "CH9988")
            ),
            hotels = listOf(
                Hotel(6, "Matterhorn Lodge", "Bahnhofstrasse 30, Zermatt",
                    LocalDate.of(2025, 12, 10), LocalDate.of(2025, 12, 20), "SW5566")
            ),
            documentInfo = DocumentInfo(passportNumber = "XP555222"),
            notes = "Try night skiing and visit Gornergrat."
        ),

        Travel(
            id = 7,
            name = "Carnival in Salvador",
            destination = "Salvador, Brazil",
            isInternational = false,
            startDate = LocalDate.of(2025, 2, 28),
            endDate = LocalDate.of(2025, 3, 5),
            purpose = "Festival",
            budget = 3000.0,
            expenses = listOf(
                Expense(9, "Bloco ticket", 350.0, "Entertainment", LocalDate.of(2025, 3, 1)),
                Expense(10, "Street food", 90.0, "Food", LocalDate.of(2025, 3, 2))
            ),
            flights = listOf(
                Flight(6, "Gol", "G31345", "GRU Airport", "SSA Airport",
                    LocalDate.of(2025, 2, 28), LocalDate.of(2025, 2, 28), "BA2025")
            ),
            hotels = listOf(
                Hotel(7, "Bahia Othon Palace", "Av. Oceânica, 2294",
                    LocalDate.of(2025, 2, 28), LocalDate.of(2025, 3, 5), "BA7788")
            ),
            documentInfo = DocumentInfo(rgOrCpf = "456.789.123-00"),
            notes = "Join at least two blocos and visit Pelourinho."
        ),

        Travel(
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

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ){
        Text(
            text = "Travels",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(travels) { travelItem ->

                Spacer(
                    modifier = Modifier
                        .height(5.dp)
                )
                TravelCard(travelItem){ selected ->
                    val travelJson = Uri.encode(Gson().toJson(selected)) // transforma  objeto em um json pq ele é muito complexo pra passar noramal
                    navController.navigate("${Routes.TRAVELS_INFO}/$travelJson")
                }


            }


        }
    }






}

@Composable
fun TravelCard(
    travel : Travel,
    onClick: (Travel) -> Unit
    ,
){

    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(150.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(15.dp)
            )
            .clickable(
                onClick = {
                    //do it
                    onClick(travel)
                }
            )
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(15.dp)
                        .background(
                            color = if (travel.isInternational) Color.Green else Color.Blue
                        )
                )
                Spacer(
                    modifier = Modifier
                        .width(5.dp)
                )
                Text(
                    travel.name,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }

            Text(
                text = travel.destination,
                style = MaterialTheme.typography.headlineSmall
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = Date(travel.startDate),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = " - ",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = Date(travel.endDate),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = "Budget: ${Money(travel.budget)} ",
                    style = MaterialTheme.typography.headlineSmall
                )
                /**
                Text(
                    text = "Spent: ${Money(travel.expenses)}",
                    style = MaterialTheme.typography.headlineSmall
                )
                **/
            }




        }



    }


}
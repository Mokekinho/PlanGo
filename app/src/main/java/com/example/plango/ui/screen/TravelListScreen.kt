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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.plango.model.Travel
import com.example.plango.util.date
import com.example.plango.util.money
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plango.database.TravelRepository
import com.example.plango.navigation.TravelInfoNav
import com.example.plango.view_models.TravelListViewModel
import com.example.plango.view_models.TravelListViewModelFactory



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

    val viewModel: TravelListViewModel = viewModel(
        factory = TravelListViewModelFactory(repository)
    )

    val state by viewModel.state.collectAsState() // o estado da minha view model, a variavel que vai observar as mudanças

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Text(
            text = "Travels",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
        )
        if (state.isLoading) { // se estiver carregando
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        } else if (state.error != null) { // se der algum erro
            Text("Error: ${state.error}")
        } else { //quando tiver carregado a lista

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(state.travels) { travelItem ->

                    Spacer(
                        modifier = Modifier
                            .height(5.dp)
                    )
                    TravelCard(travelItem) { selected ->
                        val travelId = selected.id
                        navController.navigate(TravelInfoNav(travelId))
                    }
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
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = date(travel.startDate),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = " - ",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = date(travel.endDate),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = "Budget: ${money(travel.budget)} ",
                    style = MaterialTheme.typography.headlineSmall
                )
                /**
                Text(
                    text = "Spent: ${money(travel.expenses)}",
                    style = MaterialTheme.typography.headlineSmall
                )
                **/
            }




        }



    }


}
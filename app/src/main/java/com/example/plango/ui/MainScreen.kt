package com.example.plango.ui

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
import com.example.plango.ui.screen.TravelListScreen

@Composable
fun MainScreen(){

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 30.dp)
            .padding(horizontal = 10.dp)
        ,
        topBar = {
            Text(
                "BARRA DE CIMA SO PRA VER UMA COISA",
                fontSize = 20.sp
            )
        }
    ) { innerPadding ->

        innerPadding
        TravelListScreen(innerPadding)
    }




}
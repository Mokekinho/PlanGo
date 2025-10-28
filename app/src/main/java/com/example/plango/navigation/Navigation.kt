package com.example.plango.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController


@Composable
fun Navigation(){
    val navController = rememberNavController() //o NavController ele controla para onde o aplicativo deve ir e para onde ele passou, ele é criado com a função rememberNavController() -- É IMPORTANTE COLOCAR ELE ALTO NA HIERARQUIA PARA PODER PASSA-LO ADIANTE.

    //O Navhost é onde a tela esta, o que deve ser mostrado, ele recebe como parametro um navcontroler, é possivel apssar um graph, mas aqui iremos passar um start Destination, ou seja a tela inicial. é similar com url de sites
    //NavHost(navController = navController, startDestination = ) { }




}
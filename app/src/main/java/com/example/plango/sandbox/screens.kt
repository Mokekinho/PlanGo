package com.example.plango.sandbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable


// agora vou tentar fazer com Type safe

@Serializable
object FakeHomeScreen
@Serializable
data class FakeGreetingScreenRoute(
    val name : String,
    val age : Int
)


@Composable
fun FakeMainScreen() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = FakeHomeScreen
    ){
        composable<FakeHomeScreen> {
            FakeHomeScreen(navController)
        }
        composable<FakeGreetingScreenRoute> {

            val args : FakeGreetingScreenRoute = it.toRoute() //basicamente essa função recria o objeto serializado, a serialização basicamente é um obejto que pode ser tranformado em outra coisa e depois destransformado

            FakeGreetingScreen(args.name,args.age)


        }


    }



}





//@Composable
//fun FakeMainScreen(){
//    // irei implementar a mesma coisa que a de baixo so que agora com o campo arguments
//
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = "home"
//    ){
//       // aqui irei definir o grafico
//
//        composable(
//            route = "home"
//        ) {
//            FakeHomeScreen(navController)
//        }
//
//        composable(
//            route = "greeting/{name}/{age}", //ate aqui continua igual,
//            arguments = listOf( //aqui agora o tipo de retorno é definido, antes era string, so que dessa forma o tipo é informado
//                navArgument("name") { type = NavType.StringType },
//                navArgument("age") { type = NavType.IntType }
//
//            )
//        ) {entry ->
//
//            val name = entry.arguments?.getString("name") ?: ""
//            val age = entry.arguments?.getInt("age") ?: 0
//
//            FakeGreetingScreen(name,age)
//
//
//
//        }
//
//
//    }
//
//
//
//}



//@Composable
//fun FakeMainScreenFirstImplementation(){
//    val navController = rememberNavController()
//
//
//    NavHost(
//        navController = navController,
//        startDestination = "home"
//    ) {
//        composable("home") {
//            FakeHomeScreen(navController)
//        }
//        composable("greeting/{name}/{age}") { entry -> // ao colocar esse /{name} eu to falando que esse composable pode receber um argumento
//
//            // entry agora é a nossa entrada
//            entry.arguments?.getString("name")?.let{// aqui a gente ta falando que quer pegar uma sntring com a chave name e o let vai rodar o codigo so se nao for nulo
//                val age = entry.arguments?.getString("age") ?: "0" // esse ?: é basicamente "se for nulo é igual a 0"
//
//                FakeGreetingScreen(it, age.toInt())
//            }
//        }
//
//    }
//
//
//
//
//}



@Composable
fun FakeHomeScreen(
    navController : NavController
){

    Column(
      modifier = Modifier
          .fillMaxSize()
          .padding(30.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        var name by remember {
            mutableStateOf("")
        }
        TextField(
            value = name,
            label = {
                Text("Name")
            },
            onValueChange = {
                name = it
            }

        )
        val age :Int = 20
        Button(
            onClick = {
                navController.navigate(
                    FakeGreetingScreenRoute(
                        name,
                        20
                    )
                )
            }
        ) {
            Text("OK")
        }
    }
}

@Composable
fun FakeGreetingScreen(
    name : String,
    age : Int
){

    Text(
        text = "Olá $name, seja muito bem vindo, você tem $age",
        fontSize = 25.sp,
        modifier = Modifier
            .padding(30.dp)

    )

}
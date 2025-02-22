package com.example.phonecodeconfirm

import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import land.sendy.pfe_sdk.api.API

class MainActivity : ComponentActivity() {
    private val SERVER_URL = "https://testwallet.sendy.land/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }

        val api = API.getInsatce(SERVER_URL, "sendy")
        val lp = Looper.getMainLooper()
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen {
                navController.navigate("login")
            }
        }
        composable("login") {
            LoginScreen { phoneNumber ->
                // Переходим на экран SMS, передав номер телефона
                navController.navigate("sms/$phoneNumber")
            }
        }
        // Маршрут для SMS: передаем номер телефона
        composable(
            route = "sms/{phoneNumber}",
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            SmsCodeScreen(
                phoneNumber = phoneNumber,
                onValidationSuccess = { navController.navigate("home") }
            )
        }
        composable("home") {
            HomeScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    AppNavigation()
}

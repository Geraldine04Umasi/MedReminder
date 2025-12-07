package com.example.medreminder.ui

import androidx.compose.runtime.Composable
import com.example.medreminder.ui.screens.addmedicine.AddMedicineScreen
import com.example.medreminder.ui.screens.home.HomeScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onAddMedicineClick = {
                    navController.navigate("addMedicine")
                }
            )
        }

        composable("addMedicine") {
            AddMedicineScreen(
                onSaveClick = {
                    navController.popBackStack() // Regresar al home
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

package com.example.foodmitra.NavigationBar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foodmitra.Screens.AddPage
import com.example.foodmitra.Screens.HomePage
import com.example.foodmitradonar.Screens.LoginPage
import com.example.foodmitra.Screens.ProfilePage
import com.example.foodmitra.Screens.SearchPage
import com.example.foodmitra.SplashScreen
import com.example.new_hoe.NavigationBar.BottomNav
import com.example.new_hoe.Routes.Routes


@Composable
fun Navgraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Routes.SplashScreen.route) {

        composable(Routes.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(Routes.HomePage.route) {
            HomePage()
        }
        composable(Routes.SearchPage.route) {
            SearchPage()
        }
        composable(Routes.ProfilePage.route) {
            ProfilePage()
        }
        composable(Routes.AddPage.route) {
            AddPage()
        }
        composable(Routes.BottomNav.route) {
            BottomNav(navController)
        }
        composable(Routes.LoginPage.route) {
            LoginPage(navController)
        }


    }

}
package com.example.new_hoe.NavigationBar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodmitra.Screens.AddPage
import com.example.foodmitra.Screens.HomePage
import com.example.foodmitra.Screens.ProfilePage
import com.example.foodmitra.Screens.SearchPage
import com.example.new_hoe.NavigationBar.BottomNavItem
import com.example.new_hoe.Routes.Routes


@Composable
fun BottomNav(navController: NavHostController) {

    val navController1 = rememberNavController()

    Scaffold(bottomBar = { MyBottomBar(navController = navController1) }) { innerPadding ->

        NavHost(navController = navController1, startDestination = Routes.HomePage.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Routes.HomePage.route){
                HomePage()
            }
            composable(Routes.SearchPage.route) {
                SearchPage()
            }

            composable(Routes.AddPage.route) {
                AddPage()
            }
            composable(Routes.ProfilePage.route) {
                ProfilePage()
            }
        }

    }

}

@Composable
fun MyBottomBar(navController: NavHostController){

    val backStackEntry = navController.currentBackStackEntry
    val currentRoute = backStackEntry?.destination?.route

    val list = listOf(

        BottomNavItem(
            "home",
            Routes.HomePage.route,
            Icons.Rounded.Home
        ),
        BottomNavItem(
            "Search",
            Routes.SearchPage.route,
            Icons.Rounded.Search
        ),
        BottomNavItem(
            "Add Threads",
            Routes.AddPage.route,
            Icons.Rounded.Add
        ),
        BottomNavItem(
            "Profile",
            Routes.ProfilePage.route,
            Icons.Rounded.Person
        )
    )

    BottomAppBar {
        list.forEach{
            val selected  = it.route == backStackEntry?.destination?.route

            NavigationBarItem(selected = selected,
                onClick = {
                    navController.navigate(it.route){
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = it.icon, contentDescription = it.title)
                })
        }
    }
}

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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState


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
                ProfilePage(navController)
            }
        }

    }

}

@Composable
fun MyBottomBar(navController: NavHostController) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    val items = listOf(
        BottomNavItem("Home", Routes.HomePage.route, Icons.Rounded.Home),
        BottomNavItem("Search", Routes.SearchPage.route, Icons.Rounded.Search),
        BottomNavItem("Add", Routes.AddPage.route, Icons.Rounded.Add),
        BottomNavItem("Profile", Routes.ProfilePage.route, Icons.Rounded.Person)
    )

    BottomAppBar(
        tonalElevation = 8.dp,
    ) {
        items.forEach { item ->
            val selected = item.route == currentRoute

            val iconSize by animateDpAsState(if (selected) 28.dp else 24.dp)
            val iconTint by animateColorAsState(
                if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            val textAlpha by animateFloatAsState(if (selected) 1f else 0f)

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = iconTint,
                            modifier = Modifier.size(iconSize)
                        )
                        AnimatedVisibility(visible = selected) {
                            Text(
                                text = item.title,
                                fontSize = 12.sp,
                                color = iconTint,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            )
        }
    }
}

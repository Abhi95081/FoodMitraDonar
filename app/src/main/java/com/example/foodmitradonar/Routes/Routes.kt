package com.example.new_hoe.Routes

sealed class Routes(val route: String) {
    object SplashScreen : Routes("splash")
    object  LoginPage: Routes("login")
    object HomePage : Routes("home")
    object SearchPage : Routes("search")
    object AddPage : Routes("add")
    object ProfilePage : Routes("profile")
    object BottomNav : Routes("bottom_nav")
}
package id.ac.pnm.decisionroom.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Room : Screen("room")
    object History : Screen("history")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register : Screen("register")
}
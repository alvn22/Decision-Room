package id.ac.pnm.decisionroom.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

enum class BottomNavItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
) {
    HOME("HOME", Icons.Outlined.Home, "dashboard"),
    ROOM("ROOM", Icons.Outlined.MeetingRoom, "room"),
    HISTORY("HISTORY", Icons.Outlined.History, "history"),
    PROFILE("PROFILE", Icons.Default.Person, "profile")
}

@Composable
fun BottomNavBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        BottomNavItem.entries.forEach { item ->
            NavigationBarItem(
                // Jika rute aktif sama dengan rute item menu, maka otomatis menyala biru
                selected = currentRoute == item.route,
                onClick = {
                    // Mencegah reload/pindah halaman jika user menekan menu yang sedang aktif
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(item.icon, contentDescription = item.title)
                },
                label = {
                    Text(item.title)
                }
            )
        }
    }
}
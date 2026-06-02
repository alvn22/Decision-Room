package id.ac.pnm.decisionroom.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

enum class BottomNavItem {
    HOME,
    ROOM,
    HISTORY,
    PROFILE,
}

@Composable
fun BottomNavBar(
    selected: BottomNavItem,
    onHomeClick: () -> Unit,
    onRoomClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit
) {

    NavigationBar {

        NavigationBarItem(
            selected = selected == BottomNavItem.HOME,
            onClick = onHomeClick,
            icon = {
                Icon(Icons.Outlined.Home, null)
            },
            label = {
                Text("HOME")
            }
        )

        NavigationBarItem(
            selected = selected == BottomNavItem.ROOM,
            onClick = onRoomClick,
            icon = {
                Icon(Icons.Outlined.MeetingRoom, null)
            },
            label = {
                Text("ROOM")
            }
        )

        NavigationBarItem(
            selected = selected == BottomNavItem.HISTORY,
            onClick = onHistoryClick,
            icon = {
                Icon(Icons.Outlined.History, null)
            },
            label = {
                Text("HISTORY")
            }
        )

        NavigationBarItem(
            selected = selected == BottomNavItem.PROFILE,
            onClick = onProfileClick,
            icon = {
                Icon(Icons.Default.Person, null)
            },
            label = {
                Text("PROFILE")
            }
        )
    }
}
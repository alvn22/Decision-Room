package id.ac.pnm.decisionroom.ui.room.join

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.pnm.decisionroom.PrimaryNavy
import id.ac.pnm.decisionroom.components.BottomNavBar
import id.ac.pnm.decisionroom.components.BottomNavItem
import id.ac.pnm.decisionroom.components.HeaderBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinRoomScreen(
    username: String,
    onCreateRoom: () -> Unit,
    onJoinSuccess: (String) -> Unit
) {

    val viewModel: JoinRoomViewModel = viewModel()

    val context = LocalContext.current

    var roomCode by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F2D5C)),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Decision Room",
                    color = Color.White,
                    fontSize = 24.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            "Join Session",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            "Enter the 6-digit room code"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = roomCode,
            onValueChange = {

                if (it.length <= 6) {
                    roomCode = it
                }
            },

            label = {
                Text("Room Code")
            },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),

            onClick = {

                if (roomCode.length != 6) {

                    Toast.makeText(
                        context,
                        "Kode room harus 6 digit",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@Button
                }

                viewModel.joinRoom(
                    roomCode,
                    username,

                    onSuccess = {

                        Toast.makeText(
                            context,
                            "Berhasil masuk room",
                            Toast.LENGTH_SHORT
                        ).show()

                        onJoinSuccess(roomCode)
                    },

                    onError = {

                        Toast.makeText(
                            context,
                            it,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        ) {

            Text("Enter Room")
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text("OR")

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Outlined.AddBox,
                        null
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        "New Session",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    "Start a democratic voting session"
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCreateRoom
                ) {

                    Text("Create Room")
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row {

            Card(
                modifier = Modifier.weight(1f)
            ) {

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {

                    Icon(
                        Icons.Outlined.Lock,
                        null
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        "Privacy First",
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Sessions are encrypted and results are anonymized."
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Card(
                modifier = Modifier.weight(1f)
            ) {

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {

                    Icon(
                        Icons.Outlined.FlashOn,
                        null
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        "Real-Time",
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Live feedback\nloops for faster collective decision-making."
                    )
                }
            }
        }
    }
}
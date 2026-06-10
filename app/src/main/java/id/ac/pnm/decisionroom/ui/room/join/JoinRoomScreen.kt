package id.ac.pnm.decisionroom.ui.room.join

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Lock
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
import id.ac.pnm.decisionroom.*
import id.ac.pnm.decisionroom.components.BottomNavBar
import id.ac.pnm.decisionroom.components.BottomNavItem

@Composable
fun JoinRoomScreen(
    onNavigateCreateRoom: () -> Unit,
    onJoinSuccess: (String) -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateHistory: () -> Unit,
    onNavigateProfile: () -> Unit,
    onNavigateRoom: () -> Unit
) {

    val context = LocalContext.current

    val viewModel: JoinRoomViewModel =
        viewModel()

    var roomCode by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = PrimaryNavy)
                Text(text = "Decision Room", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
                Icon(Icons.Outlined.AccountCircle, contentDescription = "Profile", tint = PrimaryNavy)
            }
        },
        bottomBar = {
            BottomNavBar(
                selected = BottomNavItem.ROOM,
                onHomeClick = onNavigateHome,
                onRoomClick = onNavigateRoom,
                onHistoryClick = onNavigateHistory,
                onProfileClick = onNavigateProfile
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Spacer(
                Modifier.height(16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape =
                    RoundedCornerShape(16.dp),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 6.dp
                ),
            ) {
                Box(
                    modifier =
                        Modifier.fillMaxSize()
                ) {
                    Surface(
                        modifier =
                            Modifier.fillMaxSize(),
                        color =
                            PrimaryNavy.copy(
                                alpha = 0.75f
                            )
                    ) {}
                    Text(
                        text =
                            "Empowering democratic transparency in high-stakes collaboration.",
                        color =
                            Color.White,
                        modifier =
                            Modifier
                                .align(
                                    Alignment.BottomStart
                                )
                                .padding(16.dp),
                        fontWeight =
                            FontWeight.Medium
                    )
                }
            }

            Spacer(
                Modifier.height(20.dp)
            )

            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BackgroundLight
                ),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 6.dp
                ),
            ) {
                Column(
                    modifier =
                        Modifier.padding(16.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Join Session",
                        fontSize = 22.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Enter the 6-digit code provided by your host",
                        color =
                            TextGray
                    )

                    Spacer(
                        Modifier.height(16.dp)
                    )

                    OutlinedTextField(
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryNavy,
                            focusedContainerColor = PrimaryNavy.copy(alpha = 0.15f)
                        ),
                        value = roomCode,
                        onValueChange = {
                            if (it.length <= 6) {
                                roomCode = it
                            }
                        },
                        modifier =
                            Modifier.fillMaxWidth(),
                        singleLine = true,
                    )

                    Spacer(
                        Modifier.height(16.dp)
                    )

                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = BlueButton),
                        onClick = {
                            viewModel.joinRoom(
                                roomId = roomCode,
                                username = "",
                                onSuccess = {
                                    Toast
                                        .makeText(
                                            context,
                                            "Berhasil masuk room",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    onJoinSuccess(
                                        roomCode
                                    )
                                },
                                onError = {
                                    Toast
                                        .makeText(
                                            context,
                                            it,
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            )
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                    ) {
                        Icon(
                            Icons.Default.Login,
                            null
                        )
                        Spacer(
                            Modifier.width(8.dp)
                        )
                        Text(
                            "Enter Room"
                        )
                    }
                }
            }

            Spacer(
                Modifier.height(20.dp)
            )

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier =
                        Modifier.weight(1f)
                )
                Text(
                    " OR ",
                    color = TextGray
                )
                HorizontalDivider(
                    modifier =
                        Modifier.weight(1f)
                )
            }

            Spacer(
                Modifier.height(20.dp)
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = BackgroundLight
                ),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 6.dp
                ),
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier =
                        Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.AddBox,
                            null,
                            tint = PrimaryNavy
                        )

                        Spacer(
                            Modifier.width(12.dp)
                        )

                        Column {
                            Text(
                                "New Session",
                                fontWeight =
                                    FontWeight.Bold
                            )
                            Text(
                                "Start a democratic voting session for your team.",
                                color =
                                    TextGray
                            )
                        }
                    }

                    Spacer(
                        Modifier.height(16.dp)
                    )

                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                        onClick =
                            onNavigateCreateRoom,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text(
                            "Create Room"
                        )
                    }
                }
            }

            Spacer(
                Modifier.height(20.dp)
            )

            Row {
                Card(
                    modifier =
                        Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = BackgroundLight
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 6.dp
                    ),
                ) {
                    Column(
                        modifier =
                            Modifier.padding(
                                12.dp
                            )
                    ) {
                        Icon(
                            Icons.Outlined.Lock,
                            null,
                            tint =
                                PrimaryNavy
                        )
                        Spacer(
                            Modifier.height(
                                8.dp
                            )
                        )
                        Text(
                            "PRIVACY FIRST",
                            fontWeight =
                                FontWeight.Bold
                        )
                        Text(
                            "Sessions are encrypted and results anonymized.",
                            color =
                                TextGray,
                            fontSize =
                                12.sp
                        )
                    }
                }

                Spacer(
                    Modifier.width(12.dp)
                )

                Card(
                    modifier =
                        Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = BackgroundLight
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 6.dp
                    ),
                ) {
                    Column(
                        modifier =
                            Modifier.padding(
                                12.dp
                            )
                    ) {
                        Icon(
                            Icons.Outlined.Bolt,
                            null,
                            tint = PrimaryNavy
                        )
                        Spacer(
                            Modifier.height(
                                8.dp
                            )
                        )
                        Text(
                            "REAL-TIME",
                            fontWeight =
                                FontWeight.Bold
                        )
                        Text(
                            "Live feedback loops for faster collective decisions.",
                            color =
                                TextGray,
                            fontSize =
                                12.sp
                        )
                    }
                }
            }

            Spacer(
                Modifier.height(20.dp)
            )
        }
    }
}
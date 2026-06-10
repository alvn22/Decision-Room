package id.ac.pnm.decisionroom.ui.room.create

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.pnm.decisionroom.*
import id.ac.pnm.decisionroom.components.BottomNavBar
import id.ac.pnm.decisionroom.components.BottomNavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomScreen(
    onRoomCreated: (String) -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateRoom: () -> Unit,
    onNavigateHistory: () -> Unit,
    onNavigateProfile: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: CreateRoomViewModel =
        viewModel()
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
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
                            color = PrimaryNavy
                        ) {}
                        Text(
                            text =
                                "Create New Session",
                            color = Color.White,
                            fontWeight =
                                FontWeight.Bold,
                            style =
                                MaterialTheme
                                    .typography
                                    .headlineSmall,
                            modifier =
                                Modifier
                                    .align(
                                        Alignment.BottomStart
                                    )
                                    .padding(16.dp)
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
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
                    ) {
                        Text(
                            text =
                                "PRIMARY TOPIC",
                            color =
                                PrimaryNavy,
                            fontWeight =
                                FontWeight.Bold,
                        )
                        Spacer(
                            Modifier.height(12.dp)
                        )
                        OutlinedTextField(
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryNavy,
                                focusedContainerColor = PrimaryNavy.copy(alpha = 0.15f)
                            ),
                            value =
                                viewModel.title,
                            onValueChange = {
                                viewModel.title =
                                    it
                            },
                            placeholder = {
                                Text(
                                    "ex: lokasi gathering kantor"
                                )
                            },
                            modifier =
                                Modifier.fillMaxWidth(),
                            shape =
                                RoundedCornerShape(
                                    12.dp
                                )
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BackgroundLight
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 6.dp
                    ),
                ) {
                    Column(
                        modifier =
                            Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {
                            Text(
                                "VOTING OPTIONS",
                                color =
                                    PrimaryNavy,
                                fontWeight =
                                    FontWeight.Bold,
                            )
                            Surface(
                                color = PrimaryNavy.copy(alpha = 0.15f),
                                shape =
                                    RoundedCornerShape(
                                        50
                                    )
                            ) {
                                Text(
                                    "2+ Required",
                                    modifier =
                                        Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        ),
                                    color = PrimaryNavy
                                )
                            }
                        }
                        Spacer(
                            Modifier.height(16.dp)
                        )
                        viewModel.options.forEachIndexed {
                                index,
                                option ->
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            bottom = 8.dp
                                        )
                                        .clip(
                                            RoundedCornerShape(
                                                12.dp
                                            )
                                        )
                                        .background(
                                            Color(
                                                0xFFF8F8FC
                                            )
                                        )
                                        .padding(
                                            12.dp
                                        ),
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.DragIndicator,
                                    null,
                                    tint =
                                        TextGray
                                )
                                Spacer(
                                    Modifier.width(
                                        8.dp
                                    )
                                )
                                OutlinedTextField(
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PrimaryNavy,
                                        focusedContainerColor = PrimaryNavy.copy(alpha = 0.15f)
                                    ),
                                    value =
                                        option,
                                    onValueChange = {
                                        viewModel.options[
                                            index
                                        ] = it
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.addOption()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Default.AddCircleOutline,
                                null,
                                tint = TextGray
                            )
                            Spacer(
                                Modifier.width(8.dp)
                            )
                            Text(
                                "Add another option",
                                color = TextGray,
                            )
                        }
                    }
                }
            }

            item {
                ElevatedButton(
                    colors = ButtonDefaults.buttonColors(containerColor = BlueButton),
                    onClick = {
                        viewModel.createRoom(
                            username = "",
                            onSuccess = {
                                Toast
                                    .makeText(
                                        context,
                                        "Room berhasil dibuat",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                onRoomCreated(it)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        null
                    )
                    Spacer(
                        Modifier.width(8.dp)
                    )
                    Text(
                        "Start Room"
                    )
                }

                Spacer(
                    Modifier.height(24.dp)
                )
            }
        }
    }
}
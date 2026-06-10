package id.ac.pnm.decisionroom.ui.room.waiting

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.pnm.decisionroom.*

@Composable
fun WaitingRoomScreen(
    roomId: String,
    navigateToVoting: () -> Unit
) {
    val viewModel: WaitingRoomViewModel =
        viewModel()

    val context = LocalContext.current

    val clipboard =
        LocalClipboardManager.current

    var messageText by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        viewModel.observeRoom(roomId)
        viewModel.observeChat(roomId)
    }

    val message = viewModel.messages
    val room = viewModel.room
    val listState = rememberLazyListState()

    LaunchedEffect(message.size) {
        if (message.isNotEmpty()) {
            listState.animateScrollToItem(
                message.lastIndex
            )
        }
    }

    LaunchedEffect(room?.status) {
        if (room?.status == "voting") {
            navigateToVoting()
        }
    }

    if (room == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        containerColor = BackgroundLight

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = PrimaryNavy.copy(alpha = 0.15f),
            ) {
                Text(
                    "WAITING FOR PARTICIPANTS",
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    ),
                    fontWeight = FontWeight.Medium,
                    color = PrimaryNavy
                )
            }

            Spacer(
                Modifier.height(12.dp)
            )

            Text(
                room.title,
                textAlign = TextAlign.Center,
                style =
                    MaterialTheme.typography
                        .headlineSmall,
                fontWeight = FontWeight.Bold
            )

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
            ) {
                Column(
                    modifier =
                        Modifier.padding(16.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {
                    Row {
                        room.roomId.forEach {
                            Card(
                                modifier =
                                    Modifier
                                        .padding(4.dp)
                                        .size(48.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = PrimaryNavy.copy(50f)
                                ),
                            ) {
                                Box(
                                    modifier =
                                        Modifier.fillMaxSize(),
                                    contentAlignment =
                                        Alignment.Center
                                ) {
                                    Text(
                                        it.toString(),
                                        color = BackgroundLight,
                                        fontWeight =
                                            FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(
                        Modifier.height(12.dp)
                    )

                    TextButton(
                        onClick = {
                            clipboard.setText(
                                androidx.compose.ui.text.AnnotatedString(
                                    room.roomId
                                )
                            )
                            Toast
                                .makeText(
                                    context,
                                    "Kode disalin",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            null,
                            tint = PrimaryNavy
                        )
                        Spacer(
                            Modifier.width(8.dp)
                        )
                        Text(
                            "COPY ROOM CODE",
                            color = PrimaryNavy
                        )
                    }
                }
            }

            Spacer(
                Modifier.height(16.dp)
            )

            Text(
                "Participants",
                fontWeight = FontWeight.Medium,
                style =
                    MaterialTheme.typography
                        .titleLarge
            )

            Spacer(
                Modifier.height(8.dp)
            )

            LazyColumn(
                modifier =
                    Modifier.weight(1f)
            ) {
                items(
                    room.participants.values.toList()
                ) { participant ->
                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = BackgroundLight
                        ),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 6.dp
                        ),
                    ) {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            Text(
                                participant.name,
                                fontWeight = FontWeight.Bold,
                                modifier =
                                    Modifier.weight(1f)
                            )
                            Text(
                                if (participant.host)
                                    "Host"
                                else
                                    "Ready",
                                color =
                                    if (participant.host)
                                        CyanBadge
                                    else
                                        TextGray
                            )
                        }
                    }
                }
            }

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier =
                        Modifier.weight(1f)
                )
                Text(
                    " Room Chat ",
                    color = TextGray
                )
                HorizontalDivider(
                    modifier =
                        Modifier.weight(1f)
                )
            }

            Spacer(
                Modifier.height(8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BackgroundLight
                ),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 6.dp
                ),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    LazyColumn(
                        state = listState,
                        modifier =
                            Modifier.weight(1f)
                    ) {
                        items(message) { chat ->
                            Card(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = PrimaryNavy.copy(15f)
                                ),
                            ) {
                                Column(
                                    modifier =
                                        Modifier.padding(10.dp)
                                ) {
                                    Text(
                                        chat.senderName,
                                        color = BackgroundLight,
                                        style =
                                            MaterialTheme
                                                .typography
                                                .labelMedium
                                    )
                                    Spacer(
                                        Modifier.height(2.dp)
                                    )
                                    Text(
                                        chat.message,
                                        color = BackgroundLight,
                                    )
                                }
                            }
                        }
                    }

                    Row {
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = {
                                messageText = it
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryNavy,
                            ),
                            modifier =
                                Modifier.weight(1f),
                            placeholder = {
                                Text("Message...")
                            }
                        )

                        Spacer(
                            Modifier.width(8.dp)
                        )

                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                            onClick = {
                                viewModel.sendMessage(
                                    roomId,
                                    messageText
                                )
                                messageText = ""
                            }
                        ) {
                            Text("Send")
                        }
                    }
                }
            }

            Spacer(
                Modifier.height(12.dp)
            )

            if (
                FirebaseManager.currentUid() == room.hostId
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                    onClick = {
                        viewModel.startVoting(
                            roomId
                        )
                        navigateToVoting()
                    },
                    modifier =
                        Modifier.fillMaxWidth()

                ) {
                    Text(
                        "START VOTING"
                    )
                }
            }
        }
    }
}
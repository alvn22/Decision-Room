package id.ac.pnm.decisionroom.ui.room.waiting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.pnm.decisionroom.FirebaseManager

@Composable
fun WaitingRoomScreen(
    roomId: String,
    navigateToVoting: () -> Unit
) {

    val viewModel:
            WaitingRoomViewModel =
        viewModel()

    var message by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        viewModel.observeRoom(roomId)
        viewModel.observeChat(roomId)
    }

    val room = viewModel.room

    LaunchedEffect(room?.status) {
        if (room?.status == "voting") {
            navigateToVoting()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Waiting For Participants",
            style =
                MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Card {

            Column(
                modifier =
                    Modifier.padding(16.dp)
            ) {

                Text(
                    room?.title ?: ""
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    roomId
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text("Participants")

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        LazyColumn {
            room?.participants
                ?.values
                ?.toList()
                ?.let { participants ->
                    items(participants) {
                        ListItem(
                            headlineContent = {
                                Text(it.name)
                            },
                            supportingContent = {
                                Text(
                                    if (it.host)
                                        "Host"
                                    else
                                        "Ready"
                                )
                            }
                        )
                    }
                }
        }
        Spacer(
            modifier = Modifier.weight(1f)
        )

        Text(
            "Room Chat",
            style =
                MaterialTheme.typography.titleMedium
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(
                viewModel.messages
            ) { msg ->
                val isMine =
                    msg.senderId ==
                            FirebaseManager.currentUid()
                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        if (isMine)
                            Arrangement.End
                        else
                            Arrangement.Start
                ) {
                    Card(
                        modifier =
                            Modifier.padding(
                                vertical = 4.dp
                            )
                    ) {
                        Column(
                            modifier =
                                Modifier.padding(
                                    12.dp
                                )
                        ) {
                            if (!isMine) {
                                Text(
                                    msg.senderName,
                                    style =
                                        MaterialTheme
                                            .typography
                                            .labelSmall
                                )
                            }
                            Text(
                                msg.message
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = {
                    message = it
                },
                modifier =
                    Modifier.weight(1f),
                placeholder = {
                    Text("Type message...")
                }
            )

            Spacer(
                modifier =
                    Modifier.width(8.dp)
            )

            Button(
                onClick = {
                    viewModel.sendMessage(
                        roomId,
                        message,
                    )
                    message = ""
                }
            ) {
                Text("Send")
            }
        }

        Button(
            modifier =
                Modifier.fillMaxWidth(),
            onClick = {
                viewModel.startVoting(roomId)
            }
        ) {
            Text("START VOTING")
        }
    }
}
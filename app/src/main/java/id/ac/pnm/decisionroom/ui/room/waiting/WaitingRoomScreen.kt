package id.ac.pnm.decisionroom.ui.room.waiting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WaitingRoomScreen(
    roomId: String
) {

    val viewModel:
            WaitingRoomViewModel =
        viewModel()

    LaunchedEffect(Unit) {

        viewModel.observeRoom(
            roomId
        )
    }

    val room = viewModel.room

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
                                    if (it.isHost)
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

        Button(
            modifier =
                Modifier.fillMaxWidth(),

            onClick = {

            }
        ) {
            Text("START VOTING")
        }
    }
}
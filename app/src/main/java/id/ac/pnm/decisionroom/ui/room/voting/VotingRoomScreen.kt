package id.ac.pnm.decisionroom.ui.room.voting

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.pnm.decisionroom.BackgroundLight
import id.ac.pnm.decisionroom.FirebaseManager
import id.ac.pnm.decisionroom.PrimaryNavy

@Composable
fun VotingRoomScreen(
    roomId: String,
    navigateToResult: () -> Unit

) {
    val viewModel: VotingRoomViewModel =
        viewModel()

    val context =
        LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.observeRoom(
            roomId
        )
    }

    val room =
        viewModel.room

    if (room == null) {
        Box(
            modifier =
                Modifier.fillMaxSize(),
            contentAlignment =
                Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    LaunchedEffect(room.status
    ) {
        if (
            room.status ==
            "finished"
        ) {
            navigateToResult()
        }
    }

    val totalParticipant =
        room.participants.size

    val totalVote =
        room.votes.size

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
    ) {
        Text(
            room.title,
            style =
                MaterialTheme
                    .typography
                    .headlineSmall,
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            Modifier.height(8.dp)
        )

        Text(
            "$totalVote / $totalParticipant voted"
        )

        Spacer(
            Modifier.height(20.dp)
        )

        LazyColumn(
            modifier =
                Modifier.weight(1f)
        ) {
            itemsIndexed(
                room.options
            ) { index, option ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = PrimaryNavy
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 6.dp
                            ),
                    onClick = {
                        viewModel.vote(
                            roomId,
                            index,
                            onSuccess = {

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
                    Column(
                        modifier =
                            Modifier
                                .padding(16.dp)
                    ) {
                        Text(
                            option.text,
                            fontWeight = FontWeight.Bold,
                            color = BackgroundLight
                        )

                        Spacer(
                            Modifier.height(
                                8.dp
                            )
                        )

                        if (viewModel.selectedOption == index
                        ) {
                            AssistChip(
                                onClick = {},
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = BackgroundLight
                                ),
                                label = {
                                    Text(
                                        "Selected",
                                        color = PrimaryNavy
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        if (FirebaseManager.currentUid() == room.hostId
        ) {

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                onClick = {
                    if (
                        room.votes.size
                        <
                        room.participants.size
                    ) {

                        Toast
                            .makeText(
                                context,
                                "Masih ada peserta yang belum memilih",
                                Toast.LENGTH_SHORT
                            )
                            .show()

                        return@Button
                    }

                    viewModel.endVoting(
                        roomId,
                        onSuccess = {
                            Toast.makeText(
                                context,
                                "Voting selesai",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onError = {
                            Toast.makeText(
                                context,
                                it,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                },
                modifier =
                    Modifier.fillMaxWidth()

            ) {
                Text(
                    "END VOTING"
                )
            }
        }
    }
}
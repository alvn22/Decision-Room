package id.ac.pnm.decisionroom.ui.room.result

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.pnm.decisionroom.BackgroundLight
import id.ac.pnm.decisionroom.PrimaryNavy
import id.ac.pnm.decisionroom.TextGray

@Composable
fun ResultScreen(
    roomId: String,
    onBackHome: () -> Unit
) {
    val viewModel: ResultViewModel =
        viewModel()

    LaunchedEffect(Unit) {
        viewModel.observeRoom(roomId)
    }

    val room = viewModel.room
    if (room == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val totalVotes =
        room.votes.size

    val voteCountMap =
        mutableMapOf<Int, Int>()

    room.votes.values.forEach {
        voteCountMap[it] =
            (voteCountMap[it] ?: 0) + 1
    }

    val maxVote =
        voteCountMap.maxOfOrNull {
            it.value
        } ?: 0

    val winners =
        voteCountMap.filter {
            it.value == maxVote
        }

    val isTie =
        winners.size > 1

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
                    .headlineMedium,
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            Modifier.height(20.dp)
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = PrimaryNavy
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 6.dp
            ),
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Column(
                modifier =
                    Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally,
            ) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = BackgroundLight
                )
                Spacer(
                    Modifier.height(12.dp)
                )

                if (isTie) {
                    Text(
                        "DRAW RESULT",
                        color = BackgroundLight,
                        style =
                            MaterialTheme
                                .typography
                                .headlineSmall
                    )

                    Spacer(
                        Modifier.height(8.dp)
                    )

                    winners.keys.forEach {
                        Text(
                            room.options[it].text,
                            color = BackgroundLight,
                        )
                    }

                } else {
                    val winnerIndex =
                        winners.keys.first()

                    Text(
                        "WINNER",
                        color = BackgroundLight,
                    )

                    Spacer(
                        Modifier.height(8.dp)
                    )

                    Text(
                        room.options[winnerIndex].text,
                        color = BackgroundLight,
                        fontWeight = FontWeight.Bold,
                        style =
                            MaterialTheme
                                .typography
                                .headlineSmall
                    )
                }
            }
        }

        Spacer(
            Modifier.height(16.dp)
        )

        Text(
            "Total Votes : $totalVotes",
            Modifier.fillMaxWidth(),
        )

        Spacer(
            Modifier.height(16.dp)
        )

        LazyColumn(
            modifier =
                Modifier.weight(1f)
        ) {
            itemsIndexed(
                room.options
            ) { index, option ->
                val count =
                    voteCountMap[index] ?: 0
                val percentage =
                    if (totalVotes == 0)
                        0f
                    else
                        count.toFloat() /
                                totalVotes.toFloat()

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = BackgroundLight
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 6.dp
                            )
                ) {
                    Column(
                        modifier =
                            Modifier.padding(
                                16.dp
                            )
                    ) {
                        Text(
                            option.text,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            Modifier.height(
                                8.dp
                            )
                        )

                        LinearProgressIndicator(
                            progress = {
                                percentage
                            },
                            color = PrimaryNavy,
                            modifier =
                                Modifier.fillMaxWidth()
                        )

                        Spacer(
                            Modifier.height(
                                8.dp
                            )
                        )

                        Text(
                            "$count votes (${(percentage * 100).toInt()}%)"
                        )
                    }
                }
            }
        }

        Button(
            onClick = onBackHome,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text(
                "BACK TO DASHBOARD",
                color = BackgroundLight
            )
        }
    }
}
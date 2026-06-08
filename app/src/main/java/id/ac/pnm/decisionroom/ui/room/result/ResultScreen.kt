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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ResultScreen(
    roomId: String
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

    val winnerIndex =
        voteCountMap.maxByOrNull {
            it.value
        }?.key ?: -1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Voting Result",
            style =
                MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Column(
                modifier =
                    Modifier.padding(20.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Text(
                    "Winner"
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    room.options
                        .getOrNull(winnerIndex)
                        ?.text ?: "-",

                    style =
                        MaterialTheme
                            .typography
                            .headlineSmall
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            "Total Votes : $totalVotes"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyColumn {

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
                                totalVotes.toFloat() * 100f

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Column(
                        modifier =
                            Modifier.padding(16.dp)
                    ) {

                        Text(
                            option.text
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        LinearProgressIndicator(
                            progress = {
                                percentage / 100f
                            },

                            modifier =
                                Modifier.fillMaxWidth()
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            "$count votes (${percentage.toInt()}%)"
                        )
                    }
                }
            }
        }
    }
}
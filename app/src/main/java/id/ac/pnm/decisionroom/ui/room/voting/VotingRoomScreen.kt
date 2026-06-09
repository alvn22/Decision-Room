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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.pnm.decisionroom.FirebaseManager

@Composable
fun VotingRoomScreen(
    roomId: String,
    navigateToResult: () -> Unit
) {
    val context = LocalContext.current
    val viewModel:
            VotingRoomViewModel =
        viewModel()

    LaunchedEffect(Unit) {

        viewModel.observeRoom(
            roomId
        )
    }

    val room = viewModel.room

    if (room == null) {

        Box(
            Modifier.fillMaxSize()
        ) {

            CircularProgressIndicator()
        }

        return
    }

    val uid =
        FirebaseManager.currentUid()

    val alreadyVote =
        room.votes.containsKey(uid)

    LaunchedEffect(room.status) {

        if (room.status == "finished") {

            navigateToResult()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            room.title,
            style =
                MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            if (alreadyVote) "Vote berhasil dikirim"
            else "Silakan pilih satu opsi"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            itemsIndexed(
                room.options
            ) { index, option ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected =
                                    room.votes[uid] == index,

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
                            )

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            Text(option.text)
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        if (uid == room.hostId) {

            Button(

                modifier =
                    Modifier.fillMaxWidth(),

                onClick = {

                    viewModel.finishVoting(
                        roomId
                    )
                }
            ) {

                Text(
                    "END VOTING"
                )
            }
        }
    }
}
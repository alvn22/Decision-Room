package id.ac.pnm.decisionroom.ui.room.create

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateRoomScreen(
    username: String,
    onRoomCreated: (String) -> Unit
) {

    val viewModel: CreateRoomViewModel =
        viewModel()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Create New Session",
            style =
                MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = viewModel.title,

            onValueChange = {
                viewModel.title = it
            },

            label = {
                Text(
                    "Apa yang ingin diputuskan?"
                )
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text("Voting Options")

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        LazyColumn {
            itemsIndexed(
                viewModel.options
            ) { index, option ->
                OutlinedTextField(
                    value = option,
                    onValueChange = {
                        viewModel.options[index] =
                            it
                    },
                    label = {
                        Text(
                            "Opsi ${index + 1}"
                        )
                    },
                    modifier =
                        Modifier.fillMaxWidth()
                )
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }
        }

        OutlinedButton(
            onClick = {
                viewModel.addOption()
            }

        ) {
            Icon(
                Icons.Outlined.Add,
                null
            )
            Spacer(
                modifier = Modifier.width(8.dp)
            )
            Text("Add Option")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row {
            Text(
                "Anonymous Voting"
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked =
                    viewModel.anonymousVoting,
                onCheckedChange = {
                    viewModel.anonymousVoting =
                        it
                }
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            modifier =
                Modifier.fillMaxWidth(),
            onClick = {
                viewModel.createRoom(
                    username,
                    onSuccess = {
                        Toast.makeText(
                            context,
                            "Room Created",
                            Toast.LENGTH_SHORT
                        ).show()
                        onRoomCreated(it)
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

            Text("Start Room")
        }
    }
}
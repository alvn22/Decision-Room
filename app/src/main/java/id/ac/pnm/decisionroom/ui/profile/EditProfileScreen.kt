package id.ac.pnm.decisionroom.ui.profile // Pastikan nama package ini sama dengan file Anda yang lain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.pnm.decisionroom.BackgroundLight
import id.ac.pnm.decisionroom.PrimaryNavy
import id.ac.pnm.decisionroom.TextFieldBackground
import id.ac.pnm.decisionroom.TextGray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onSaveClick: (String, String, String) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var konfirmasiPasswordVisible by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = PrimaryNavy)
                }
                Text(text = "Edit Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Full Name
                    Text("FULL NAME", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = TextFieldBackground,
                            focusedContainerColor = TextFieldBackground,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("EMAIL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = TextFieldBackground,
                            focusedContainerColor = TextFieldBackground,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Current Password
                    Text("CURRENT PASSWORD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = null, tint = TextGray)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = TextFieldBackground,
                            focusedContainerColor = TextFieldBackground,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // New Password
                    Text("NEW PASSWORD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        placeholder = { Text("New password", fontSize = 14.sp) },
                        trailingIcon = {
                            val image = if (konfirmasiPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { konfirmasiPasswordVisible = !konfirmasiPasswordVisible }) {
                                Icon(imageVector = image, contentDescription = null, tint = TextGray)
                            }
                        },
                        visualTransformation = if (konfirmasiPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = TextFieldBackground,
                            focusedContainerColor = TextFieldBackground,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Kirimkan data yang sedang diketik pengguna ke MainActivity saat diklik
                    onSaveClick(fullName, email, newPassword)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Simpan Perubahan")
            }
        }
    }
}
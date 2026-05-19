package id.ac.pnm.decisionroom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ikon dan Judul Utama
        Icon(
            imageVector = Icons.Default.AccountBalance, // Gunakan ikon yang mirip atau dari drawable
            contentDescription = "Logo",
            tint = Color.White,
            modifier = Modifier
                .size(64.dp)
                .background(PrimaryNavy, RoundedCornerShape(16.dp))
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Decision Room",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryNavy
        )
        Text(
            text = "Kolaborasi transparan untuk keputusan tepat.",
            fontSize = 12.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card Form Login
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                // Input Email
                Text("EMAIL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("nama@email.com", fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TextGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TextFieldBackground,
                        unfocusedContainerColor = TextFieldBackground,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = PrimaryNavy
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input Kata Sandi dengan Lupa Password
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("KATA SANDI", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Text(
                        text = "Lupa Password?",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy,
                        modifier = Modifier.clickable { /* Handle Lupa Password */ }
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("********", fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TextGray) },
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
                        focusedContainerColor = TextFieldBackground,
                        unfocusedContainerColor = TextFieldBackground,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = PrimaryNavy
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Masuk
                Button(
                    onClick = { onLoginClick(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
                ) {
                    Text("Masuk ke Akun", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Divider ATAU LANJUTKAN DENGAN
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                    Text(" ATAU LANJUTKAN DENGAN ", fontSize = 10.sp, color = TextGray)
                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Google (Membutuhkan asset logo google di res/drawable)
                OutlinedButton(
                    onClick = { /* Handle Google Login */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    // Gunakan painterResource(id = R.drawable.ic_google) jika ada logonya
                    Text("Masuk dengan Google", color = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Teks Bawah
        Row {
            Text("Belum punya akun? ", color = TextGray, fontSize = 12.sp)
            Text(
                text = "Daftar Sekarang",
                color = PrimaryNavy,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }
}
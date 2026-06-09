package id.ac.pnm.decisionroom.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.pnm.decisionroom.BackgroundLight
import id.ac.pnm.decisionroom.BlueButton
import id.ac.pnm.decisionroom.CyanBadge
import id.ac.pnm.decisionroom.PrimaryNavy
import id.ac.pnm.decisionroom.RedDot
import id.ac.pnm.decisionroom.TextGray
import id.ac.pnm.decisionroom.components.BottomNavBar
import id.ac.pnm.decisionroom.components.BottomNavItem
import id.ac.pnm.decisionroom.components.HeaderBar


@Composable
fun DashboardScreen(
    username: String = "username",
    onCreateRoomClick: () -> Unit = {},
    onNavigateHome: () -> Unit = {},
    onNavigateRoom: () -> Unit = {},
    onNavigateHistory: () -> Unit = {},
    onNavigateProfile: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            HeaderBar(
                title = "Decision Room",
                onMenuClick = { /* Handle Menu */ },
                onProfileClick = onNavigateProfile
            )
        },
        bottomBar = {
            BottomNavBar(
                selected = BottomNavItem.HOME,
                onHomeClick = onNavigateHome,
                onRoomClick = onNavigateRoom,
                onHistoryClick = onNavigateHistory,
                onProfileClick = onNavigateProfile
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // 1. HEADER SECTION
                Text(
                    text = "DASHBOARD",
                    color = BlueButton,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Welcome back, $username",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Your democratic insights are ready for today's\nsessions.",
                    color = TextGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                // 2. ACTIVE ROOMS SECTION
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.MeetingRoom,
                            contentDescription = null,
                            tint = PrimaryNavy
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Active Rooms",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(CyanBadge, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "3 LIVE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // ACTIVE ROOM CARD
                ActiveRoomCard()

                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                // 3. CREATE ROOM BUTTON
                Button(
                    onClick = onCreateRoomClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueButton)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.AddCircleOutline,
                                contentDescription = null,
                                tint = PrimaryNavy
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Create New Room",
                                color = PrimaryNavy,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = PrimaryNavy
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                // 4. RECENT HISTORY SECTION
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.History, contentDescription = null, tint = PrimaryNavy)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Recent History",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Text(
                        text = "VIEW ALL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy,
                        modifier = Modifier.clickable { onNavigateHistory() }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                // HISTORY LIST
                HistoryItemCard(icon = Icons.Outlined.Checklist, title = "Budget Allocation 2024", status = "Approved", time = "12 hours ago")
                Spacer(modifier = Modifier.height(8.dp))
                HistoryItemCard(icon = Icons.Outlined.PersonSearch, title = "Lead Candidate Hiring", status = "Tie Breaker", time = "Yesterday")
                Spacer(modifier = Modifier.height(8.dp))
                HistoryItemCard(icon = Icons.Outlined.Campaign, title = "Marketing Campaign Pivot", status = "Rejected", time = "2 days ago")

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ActiveRoomCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Board of Directors Q3", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
                Box(modifier = Modifier.size(10.dp).background(RedDot, CircleShape))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Room Code: QX-402", fontSize = 14.sp, color = TextGray)

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("VOTING PROGRESS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGray)
                Text("84%", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 0.84f },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = BlueButton,
                trackColor = BackgroundLight
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dummy Avatars Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(24.dp).background(Color.LightGray, CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.size(24.dp).background(Color.Gray, CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier.size(24.dp).background(PrimaryNavy, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+12", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, status: String, time: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(BackgroundLight, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = BlueButton)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("$status • $time", fontSize = 12.sp, color = TextGray)
                }
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextGray)
        }
    }
}
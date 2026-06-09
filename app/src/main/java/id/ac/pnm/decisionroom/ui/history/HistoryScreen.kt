package id.ac.pnm.decisionroom.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
fun HistoryScreen()
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 1. HEADER SECTION
        Text(
            text = "History",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Review your previous boardroom decisions and outcomes.",
            softWrap = true,
            color = TextGray,
            fontSize = 16.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))
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
        }

        Spacer(modifier = Modifier.height(12.dp))

        // HISTORY LIST

        Spacer(modifier = Modifier.height(24.dp)) // Jarak ekstra di bawah sebelum bottom bar
    }
}
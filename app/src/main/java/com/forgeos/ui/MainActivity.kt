package com.forgeos.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forgeos.data.SessionMode
import com.forgeos.ranked.RankedModeService
import com.forgeos.ui.theme.ForgeColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForgeOSTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun ForgeOSTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(ForgeColors.Primary),
            secondary = Color(ForgeColors.Secondary),
            background = Color(ForgeColors.Background),
            surface = Color(ForgeColors.Surface),
            onPrimary = Color(ForgeColors.OnPrimary),
            onBackground = Color(ForgeColors.TextPrimary),
            onSurface = Color(ForgeColors.TextPrimary)
        ),
        content = content
    )
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var isSessionRunning by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(ForgeColors.Background))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "FORGEOS",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(ForgeColors.Primary)
        )

        Text(
            text = "Phone ko gaming device bana do",
            fontSize = 14.sp,
            color = Color(ForgeColors.TextSecondary)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Rank card placeholder
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(ForgeColors.Surface))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Current Rank", color = Color(ForgeColors.TextSecondary), fontSize = 12.sp)
                Text(
                    text = "Iron",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(ForgeColors.RankIron)
                )
                Text("0 XP", color = Color(ForgeColors.TextSecondary), fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Ranked Mode button
        Button(
            onClick = {
                if (isSessionRunning) {
                    RankedModeService.stop(context)
                    isSessionRunning = false
                } else {
                    RankedModeService.start(
                        context = context,
                        mode = SessionMode.RANKED,
                        game = "BGMI"
                    )
                    isSessionRunning = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSessionRunning) Color(ForgeColors.Error) else Color(ForgeColors.Primary)
            )
        ) {
            Text(
                text = if (isSessionRunning) "STOP RANKED SESSION" else "START RANKED SESSION",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Free Fire + BGMI focused foundation",
            color = Color(ForgeColors.TextDisabled),
            fontSize = 12.sp
        )
    }
}

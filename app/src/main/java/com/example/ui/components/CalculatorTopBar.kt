package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppMode
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightTextPrimary

@Composable
fun CalculatorTopBar(
    currentMode: AppMode,
    isDarkMode: Boolean,
    onMenuClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconColor = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val btnBg = if (isDarkMode) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.45f)
    val borderColor = if (isDarkMode) DarkBorder else LightBorder

    val modeTitle = when (currentMode) {
        AppMode.STANDARD -> "Calculator"
        AppMode.SCIENTIFIC -> "Scientific"
        AppMode.CURRENCY -> "Currency"
        AppMode.MASS -> "Mass Converter"
        AppMode.TIP -> "Tip Calculator"
        AppMode.DISCOUNT -> "Discount"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Menu Button
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(btnBg)
                .border(1.dp, borderColor, CircleShape)
                .testTag("menu_button")
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Open Navigation Menu",
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        // Header Title (matches HTML headerTitle)
        Text(
            text = if (currentMode == AppMode.STANDARD) "" else modeTitle.uppercase(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = iconColor
        )

        // History Button (only visible for Standard and Scientific)
        if (currentMode == AppMode.STANDARD || currentMode == AppMode.SCIENTIFIC) {
            IconButton(
                onClick = onHistoryClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(btnBg)
                    .border(1.dp, borderColor, CircleShape)
                    .testTag("history_button")
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Open Calculation History",
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            Spacer(modifier = Modifier.size(40.dp))
        }
    }
}

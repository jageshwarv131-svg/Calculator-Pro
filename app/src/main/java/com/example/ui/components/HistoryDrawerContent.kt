package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CalcHistoryItem
import com.example.ui.AppMode
import com.example.ui.theme.*

@Composable
fun HistoryDrawerContent(
    currentMode: AppMode,
    historyList: List<CalcHistoryItem>,
    isDarkMode: Boolean,
    onClearHistory: () -> Unit,
    onCloseDrawer: () -> Unit,
    onSelectHistoryItem: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val drawerBg = if (isDarkMode) Color(0xF00F172A) else Color(0xF5FFFFFF)
    val borderColor = if (isDarkMode) DarkBorder else LightBorder
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val accentRed = if (isDarkMode) DarkCoral else CoralRed

    val heading = if (currentMode == AppMode.SCIENTIFIC) "Scientific History" else "Standard History"

    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(310.dp)
            .border(1.dp, borderColor, RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)),
        color = drawerBg,
        shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = heading,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )

                IconButton(onClick = onCloseDrawer) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close History",
                        tint = textSecondary
                    )
                }
            }

            HorizontalDivider(color = borderColor)

            // History list
            if (historyList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No history yet",
                        color = textSecondary,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 8.dp)
                ) {
                    items(historyList, key = { it.id }) { item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectHistoryItem(item.result) }
                                .padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = item.expression,
                                fontSize = 13.sp,
                                color = textSecondary,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.End
                            )
                            Text(
                                text = "= ${item.result}",
                                fontSize = 17.sp,
                                color = accentRed,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.End,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        HorizontalDivider(color = borderColor.copy(alpha = 0.5f))
                    }
                }
            }

            // Clear button
            Button(
                onClick = onClearHistory,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkMode) DarkBtnOpBg else LightBtnOpBg,
                    contentColor = accentRed
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, accentRed.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                    .testTag("clear_history_btn")
            ) {
                Text(
                    text = "Clear History",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

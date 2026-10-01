package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppMode
import com.example.ui.theme.*

@Composable
fun MenuDrawerContent(
    currentMode: AppMode,
    isDarkMode: Boolean,
    onSelectMode: (AppMode) -> Unit,
    onToggleDarkMode: () -> Unit,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isSettingsView by remember { mutableStateOf(false) }

    val drawerBg = if (isDarkMode) Color(0xF00F172A) else Color(0xF5FFFFFF)
    val borderColor = if (isDarkMode) DarkBorder else LightBorder
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(310.dp)
            .border(1.dp, borderColor, RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)),
        color = drawerBg,
        shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
    ) {
        AnimatedContent(
            targetState = isSettingsView,
            label = "drawer_view_transition"
        ) { inSettings ->
            if (inSettings) {
                // Settings View
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { isSettingsView = false }
                                .padding(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to Menu",
                                tint = textPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Settings",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                        }

                        IconButton(onClick = onCloseDrawer) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Menu",
                                tint = textSecondary
                            )
                        }
                    }

                    HorizontalDivider(color = borderColor)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Dark Mode Toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Dark Mode",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textPrimary
                                )
                                Text(
                                    text = "Switch between light & dark",
                                    fontSize = 12.sp,
                                    color = textSecondary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { onToggleDarkMode() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = CoralRed,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = if (isDarkMode) Color(0x33FFFFFF) else Color(0x33000000)
                                ),
                                modifier = Modifier.testTag("dark_mode_switch")
                            )
                        }

                        HorizontalDivider(color = borderColor)

                        // App Version Item
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "App Version",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimary
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isDarkMode) DarkBtnOpBg else LightBtnOpBg)
                                    .border(1.dp, CoralRed.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "1.0.0",
                                    color = if (isDarkMode) DarkCoral else CoralRed,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        HorizontalDivider(color = borderColor)
                    }
                }
            } else {
                // Main Menu View
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Menu",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )

                        IconButton(onClick = onCloseDrawer) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Menu",
                                tint = textSecondary
                            )
                        }
                    }

                    HorizontalDivider(color = borderColor)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        MenuItem(
                            title = "Standard Calculator",
                            icon = Icons.Default.Calculate,
                            isSelected = currentMode == AppMode.STANDARD,
                            isDarkMode = isDarkMode,
                            onClick = {
                                onSelectMode(AppMode.STANDARD)
                                onCloseDrawer()
                            }
                        )

                        MenuItem(
                            title = "Scientific Calculator",
                            icon = Icons.Default.Functions,
                            isSelected = currentMode == AppMode.SCIENTIFIC,
                            isDarkMode = isDarkMode,
                            onClick = {
                                onSelectMode(AppMode.SCIENTIFIC)
                                onCloseDrawer()
                            }
                        )

                        MenuItem(
                            title = "Currency Converter",
                            icon = Icons.Default.AttachMoney,
                            isSelected = currentMode == AppMode.CURRENCY,
                            isDarkMode = isDarkMode,
                            onClick = {
                                onSelectMode(AppMode.CURRENCY)
                                onCloseDrawer()
                            }
                        )

                        MenuItem(
                            title = "Mass Converter",
                            icon = Icons.Default.Scale,
                            isSelected = currentMode == AppMode.MASS,
                            isDarkMode = isDarkMode,
                            onClick = {
                                onSelectMode(AppMode.MASS)
                                onCloseDrawer()
                            }
                        )

                        MenuItem(
                            title = "Tip Calculator",
                            icon = Icons.Default.ReceiptLong,
                            isSelected = currentMode == AppMode.TIP,
                            isDarkMode = isDarkMode,
                            onClick = {
                                onSelectMode(AppMode.TIP)
                                onCloseDrawer()
                            }
                        )

                        MenuItem(
                            title = "Discount Calculator",
                            icon = Icons.Default.LocalOffer,
                            isSelected = currentMode == AppMode.DISCOUNT,
                            isDarkMode = isDarkMode,
                            onClick = {
                                onSelectMode(AppMode.DISCOUNT)
                                onCloseDrawer()
                            }
                        )

                        // Share App
                        MenuItem(
                            title = "Share App",
                            icon = Icons.Default.Share,
                            isSelected = false,
                            isDarkMode = isDarkMode,
                            onClick = {
                                shareApp(context)
                                onCloseDrawer()
                            }
                        )

                        // Settings Item with Chevron
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isSettingsView = true }
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = textPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                Text(
                                    text = "Settings",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textPrimary
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = textSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        HorizontalDivider(color = borderColor)
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isDarkMode) DarkBorder else LightBorder
    val activeColor = if (isDarkMode) DarkCoral else CoralRed
    val activeBg = if (isDarkMode) DarkBtnOpBg else LightBtnOpBg
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isSelected) {
                    Modifier
                        .background(activeBg)
                        .drawBehind {
                            drawRect(
                                color = activeColor,
                                size = androidx.compose.ui.geometry.Size(4.dp.toPx(), size.height)
                            )
                        }
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) activeColor else textPrimary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) activeColor else textPrimary
        )
    }

    HorizontalDivider(color = borderColor)
}

private fun shareApp(context: Context) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Check out this awesome Calculator Pro app with Scientific, Currency, Mass, Tip & Discount tools!"
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Calculator Pro")
    context.startActivity(shareIntent)
}

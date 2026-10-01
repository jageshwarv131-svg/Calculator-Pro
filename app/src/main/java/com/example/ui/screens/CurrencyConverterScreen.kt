package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DEFAULT_CURRENCIES
import com.example.ui.CalculatorViewModel
import com.example.ui.components.ButtonType
import com.example.ui.components.CalcButton
import com.example.ui.theme.*

@Composable
fun CurrencyConverterScreen(
    viewModel: CalculatorViewModel,
    curr1: String,
    curr2: String,
    val1: String,
    val2: String,
    activeBox: Int,
    rates: Map<String, Double>,
    updateTime: String,
    isDarkMode: Boolean,
    onOpenPicker: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val cardBg = if (isDarkMode) Color.White.copy(alpha = 0.04f) else Color.White.copy(alpha = 0.5f)
    val activeBg = if (isDarkMode) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.9f)
    val activeBorder = if (isDarkMode) DarkCoral else CoralRed
    val normalBorder = if (isDarkMode) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.6f)

    // Cursor blink animation
    val infiniteTransition = rememberInfiniteTransition(label = "cursor_blink")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursor_alpha"
    )

    val c1 = DEFAULT_CURRENCIES.find { it.code == curr1 } ?: DEFAULT_CURRENCIES[0]
    val c2 = DEFAULT_CURRENCIES.find { it.code == curr2 } ?: DEFAULT_CURRENCIES[2]

    val r1 = rates[curr1] ?: 1.0
    val r2 = rates[curr2] ?: 1.0
    val singleRate = if (r1 != 0.0) r2 / r1 else 1.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Converter Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Box 1
            CurrencyCard(
                currency = c1,
                value = val1,
                isActive = activeBox == 1,
                cursorAlpha = cursorAlpha,
                isDarkMode = isDarkMode,
                cardBg = if (activeBox == 1) activeBg else cardBg,
                activeBorder = activeBorder,
                normalBorder = normalBorder,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                onCardClick = { viewModel.setActiveCurrBox(1) },
                onSelectorClick = { onOpenPicker(1) }
            )

            // Box 2
            CurrencyCard(
                currency = c2,
                value = val2,
                isActive = activeBox == 2,
                cursorAlpha = cursorAlpha,
                isDarkMode = isDarkMode,
                cardBg = if (activeBox == 2) activeBg else cardBg,
                activeBorder = activeBorder,
                normalBorder = normalBorder,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                onCardClick = { viewModel.setActiveCurrBox(2) },
                onSelectorClick = { onOpenPicker(2) }
            )

            // Exchange rate info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "💰 ", fontSize = 14.sp)
                    Text(
                        text = "1 $curr1 ≈ ${String.format(java.util.Locale.US, "%.2f", singleRate)} $curr2",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                }
                Text(
                    text = "Rate Updated: $updateTime",
                    fontSize = 11.sp,
                    color = textSecondary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // Keypad grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1: 7, 8, 9, ⌫
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "7",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currAppendNum("7") }
                )
                CalcButton(
                    text = "8",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currAppendNum("8") }
                )
                CalcButton(
                    text = "9",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currAppendNum("9") }
                )
                CalcButton(
                    text = "del",
                    type = ButtonType.RED_ACTION,
                    isDarkMode = isDarkMode,
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = "Backspace",
                            tint = if (isDarkMode) DarkCoral else CoralRed,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currDeleteLast() }
                )
            }

            // Row 2: 4, 5, 6, ⇅
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "4",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currAppendNum("4") }
                )
                CalcButton(
                    text = "5",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currAppendNum("5") }
                )
                CalcButton(
                    text = "6",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currAppendNum("6") }
                )
                CalcButton(
                    text = "swap",
                    type = ButtonType.OPERATOR,
                    isDarkMode = isDarkMode,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Swap Currencies",
                            tint = if (isDarkMode) DarkCoral else CoralRed,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currSwapCurrencies() }
                )
            }

            // Row 3: 1, 2, 3, AC
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "1",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currAppendNum("1") }
                )
                CalcButton(
                    text = "2",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currAppendNum("2") }
                )
                CalcButton(
                    text = "3",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currAppendNum("3") }
                )
                CalcButton(
                    text = "AC",
                    type = ButtonType.RED_ACTION,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currClearAll() }
                )
            }

            // Row 4: 00, 0, ., ✓
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "00",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currAppendNum("00") }
                )
                CalcButton(
                    text = "0",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currAppendNum("0") }
                )
                CalcButton(
                    text = ".",
                    fontSize = 28.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.currAppendDot() }
                )
                CalcButton(
                    text = "✓",
                    type = ButtonType.EQUALS,
                    isDarkMode = isDarkMode,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f),
                    onClick = { /* Confirm */ }
                )
            }
        }
    }
}

@Composable
private fun CurrencyCard(
    currency: com.example.data.model.Currency,
    value: String,
    isActive: Boolean,
    cursorAlpha: Float,
    isDarkMode: Boolean,
    cardBg: Color,
    activeBorder: Color,
    normalBorder: Color,
    textPrimary: Color,
    textSecondary: Color,
    onCardClick: () -> Unit,
    onSelectorClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(
                width = if (isActive) 1.5.dp else 1.dp,
                color = if (isActive) activeBorder else normalBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onCardClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column {
            // Currency selector row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onSelectorClick),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = currency.flag, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currency.code,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select",
                        tint = textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = currency.name,
                    fontSize = 11.sp,
                    color = textSecondary,
                    maxLines = 1
                )
            }

            // Value row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    letterSpacing = 0.5.sp
                )
                if (isActive) {
                    Box(
                        modifier = Modifier
                            .padding(start = 3.dp)
                            .width(2.dp)
                            .height(26.dp)
                            .alpha(cursorAlpha)
                            .background(activeBorder)
                    )
                }
            }
        }
    }
}

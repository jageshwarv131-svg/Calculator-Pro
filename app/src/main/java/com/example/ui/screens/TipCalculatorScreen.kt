package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CalculatorViewModel
import com.example.ui.components.ButtonType
import com.example.ui.components.CalcButton
import com.example.ui.theme.*

@Composable
fun TipCalculatorScreen(
    viewModel: CalculatorViewModel,
    activeField: String,
    tipValues: Map<String, String>,
    tipType: String,
    taxType: String,
    noTipOnTax: Boolean,
    tipTotal: String,
    tipPerPerson: String,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val cardBg = if (isDarkMode) Color.White.copy(alpha = 0.04f) else Color.White.copy(alpha = 0.5f)
    val activeBg = if (isDarkMode) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.9f)
    val activeBorder = if (isDarkMode) DarkCoral else CoralRed
    val normalBorder = if (isDarkMode) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.6f)
    val accentRed = if (isDarkMode) DarkCoral else CoralRed

    val infiniteTransition = rememberInfiniteTransition(label = "cursor_blink_tip")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursor_alpha_tip"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Container for scrollable fields
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Bill
            TipFieldBox(
                label = "BILL",
                value = tipValues["bill"] ?: "0",
                unit = "",
                isActive = activeField == "bill",
                cursorAlpha = cursorAlpha,
                cardBg = cardBg,
                activeBg = activeBg,
                activeBorder = activeBorder,
                normalBorder = normalBorder,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                onClick = { viewModel.setActiveTipField("bill") }
            )

            // People
            TipFieldBox(
                label = "PEOPLE",
                value = tipValues["people"] ?: "1",
                unit = "",
                isActive = activeField == "people",
                cursorAlpha = cursorAlpha,
                cardBg = cardBg,
                activeBg = activeBg,
                activeBorder = activeBorder,
                normalBorder = normalBorder,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                onClick = { viewModel.setActiveTipField("people") }
            )

            // Tip Box with % / Val radio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (activeField == "tip") activeBg else cardBg)
                    .border(
                        width = if (activeField == "tip") 1.5.dp else 1.dp,
                        color = if (activeField == "tip") activeBorder else normalBorder,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { viewModel.setActiveTipField("tip") }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TIP",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = textSecondary,
                            letterSpacing = 0.5.sp
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { viewModel.setTipType("percent") }
                            ) {
                                RadioButton(
                                    selected = tipType == "percent",
                                    onClick = { viewModel.setTipType("percent") },
                                    colors = RadioButtonDefaults.colors(selectedColor = accentRed),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { viewModel.setTipType("value") }
                            ) {
                                RadioButton(
                                    selected = tipType == "value",
                                    onClick = { viewModel.setTipType("value") },
                                    colors = RadioButtonDefaults.colors(selectedColor = accentRed),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Val", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tipValues["tip"] ?: "0",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        if (tipType == "percent") {
                            Text(
                                text = " %",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = textPrimary
                            )
                        }
                        if (activeField == "tip") {
                            Box(
                                modifier = Modifier
                                    .padding(start = 3.dp)
                                    .width(2.dp)
                                    .height(22.dp)
                                    .alpha(cursorAlpha)
                                    .background(activeBorder)
                            )
                        }
                    }
                }
            }

            // Tax Box with % / Val radio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (activeField == "tax") activeBg else cardBg)
                    .border(
                        width = if (activeField == "tax") 1.5.dp else 1.dp,
                        color = if (activeField == "tax") activeBorder else normalBorder,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { viewModel.setActiveTipField("tax") }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TAX RATE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = textSecondary,
                            letterSpacing = 0.5.sp
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { viewModel.setTaxType("percent") }
                            ) {
                                RadioButton(
                                    selected = taxType == "percent",
                                    onClick = { viewModel.setTaxType("percent") },
                                    colors = RadioButtonDefaults.colors(selectedColor = accentRed),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { viewModel.setTaxType("value") }
                            ) {
                                RadioButton(
                                    selected = taxType == "value",
                                    onClick = { viewModel.setTaxType("value") },
                                    colors = RadioButtonDefaults.colors(selectedColor = accentRed),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Val", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tipValues["tax"] ?: "0",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        if (taxType == "percent") {
                            Text(
                                text = " %",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = textPrimary
                            )
                        }
                        if (activeField == "tax") {
                            Box(
                                modifier = Modifier
                                    .padding(start = 3.dp)
                                    .width(2.dp)
                                    .height(22.dp)
                                    .alpha(cursorAlpha)
                                    .background(activeBorder)
                            )
                        }
                    }
                }
            }

            // Do not tip on tax switch row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(1.dp, normalBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Do not tip on tax",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textPrimary
                )

                Switch(
                    checked = noTipOnTax,
                    onCheckedChange = { viewModel.toggleNoTipOnTax() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = accentRed
                    )
                )
            }

            // Results Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isDarkMode) Color.White.copy(alpha = 0.05f) else Color.White.copy(alpha = 0.65f))
                    .border(1.dp, normalBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "RESULTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = textSecondary,
                    letterSpacing = 0.5.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total", fontSize = 14.sp, color = textSecondary)
                    Text(text = tipTotal, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = accentRed)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Per person", fontSize = 14.sp, color = textSecondary)
                    Text(text = tipPerPerson, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = accentRed)
                }
            }
        }

        // Keypad: Uniform 4x4 Grid of Circular Buttons
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
                    onClick = { viewModel.tipAppendNum("7") }
                )
                CalcButton(
                    text = "8",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.tipAppendNum("8") }
                )
                CalcButton(
                    text = "9",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.tipAppendNum("9") }
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
                    onClick = { viewModel.tipDeleteLast() }
                )
            }

            // Row 2: 4, 5, 6, +/-
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "4",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.tipAppendNum("4") }
                )
                CalcButton(
                    text = "5",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.tipAppendNum("5") }
                )
                CalcButton(
                    text = "6",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.tipAppendNum("6") }
                )
                CalcButton(
                    text = "+/-",
                    type = ButtonType.RED_ACTION,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.tipToggleSign() }
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
                    onClick = { viewModel.tipAppendNum("1") }
                )
                CalcButton(
                    text = "2",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.tipAppendNum("2") }
                )
                CalcButton(
                    text = "3",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.tipAppendNum("3") }
                )
                CalcButton(
                    text = "AC",
                    type = ButtonType.RED_ACTION,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.tipClearAll() }
                )
            }

            // Row 4: 00, 0, ., ✓ (✓ is uniform circular size, exactly 1 weight!)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "00",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.tipAppendNum("00") }
                )
                CalcButton(
                    text = "0",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.tipAppendNum("0") }
                )
                CalcButton(
                    text = ".",
                    fontSize = 28.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.tipAppendDot() }
                )
                CalcButton(
                    text = "✓",
                    type = ButtonType.EQUALS,
                    isDarkMode = isDarkMode,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f),
                    onClick = { /* Confirmed */ }
                )
            }
        }
    }
}

@Composable
private fun TipFieldBox(
    label: String,
    value: String,
    unit: String,
    isActive: Boolean,
    cursorAlpha: Float,
    cardBg: Color,
    activeBg: Color,
    activeBorder: Color,
    normalBorder: Color,
    textPrimary: Color,
    textSecondary: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isActive) activeBg else cardBg)
            .border(
                width = if (isActive) 1.5.dp else 1.dp,
                color = if (isActive) activeBorder else normalBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = textSecondary,
                letterSpacing = 0.5.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                if (unit.isNotEmpty()) {
                    Text(
                        text = " $unit",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = textPrimary
                    )
                }
                if (isActive) {
                    Box(
                        modifier = Modifier
                            .padding(start = 3.dp)
                            .width(2.dp)
                            .height(22.dp)
                            .alpha(cursorAlpha)
                            .background(activeBorder)
                    )
                }
            }
        }
    }
}

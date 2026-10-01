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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
fun MassConverterScreen(
    viewModel: CalculatorViewModel,
    activeUnit: String,
    massValues: Map<String, String>,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val cardBg = if (isDarkMode) Color.White.copy(alpha = 0.04f) else Color.White.copy(alpha = 0.5f)
    val activeBg = if (isDarkMode) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.9f)
    val activeBorder = if (isDarkMode) DarkCoral else CoralRed
    val normalBorder = if (isDarkMode) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.6f)

    val infiniteTransition = rememberInfiniteTransition(label = "cursor_blink_mass")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursor_alpha_mass"
    )

    val units = listOf(
        "kg" to "Kilogram (kg)",
        "lb" to "Pound (lb)",
        "g" to "Gram (g)",
        "oz" to "Ounce (oz)"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Mass Boxes container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            units.forEach { (unitKey, unitLabel) ->
                val isActive = activeUnit == unitKey
                val value = massValues[unitKey] ?: "0"

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
                        .clickable { viewModel.setActiveMassUnit(unitKey) }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Column {
                        Text(
                            text = unitLabel,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = textSecondary
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
        }

        // Keypad (Uniform 4x4 Grid of Circular Buttons)
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
                    onClick = { viewModel.massAppendNum("7") }
                )
                CalcButton(
                    text = "8",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.massAppendNum("8") }
                )
                CalcButton(
                    text = "9",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.massAppendNum("9") }
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
                    onClick = { viewModel.massDeleteLast() }
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
                    onClick = { viewModel.massAppendNum("4") }
                )
                CalcButton(
                    text = "5",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.massAppendNum("5") }
                )
                CalcButton(
                    text = "6",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.massAppendNum("6") }
                )
                CalcButton(
                    text = "+/-",
                    type = ButtonType.RED_ACTION,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.massToggleSign() }
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
                    onClick = { viewModel.massAppendNum("1") }
                )
                CalcButton(
                    text = "2",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.massAppendNum("2") }
                )
                CalcButton(
                    text = "3",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.massAppendNum("3") }
                )
                CalcButton(
                    text = "AC",
                    type = ButtonType.RED_ACTION,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.massClearAll() }
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
                    onClick = { viewModel.massAppendNum("00") }
                )
                CalcButton(
                    text = "0",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.massAppendNum("0") }
                )
                CalcButton(
                    text = ".",
                    fontSize = 28.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.massAppendDot() }
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

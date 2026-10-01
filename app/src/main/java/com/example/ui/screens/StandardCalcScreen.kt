package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CalculatorViewModel
import com.example.ui.components.ButtonType
import com.example.ui.components.CalcButton
import com.example.ui.theme.*

@Composable
fun StandardCalcScreen(
    viewModel: CalculatorViewModel,
    displayValue: String,
    currentExpression: String,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    val fontSize = when {
        displayValue.length > 10 -> 36.sp
        displayValue.length > 7 -> 46.sp
        else -> 56.sp
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = currentExpression,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Text(
                text = displayValue,
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold,
                color = textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                letterSpacing = (-0.5).sp
            )
        }

        // Keypad grid (4 columns)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1: Memory
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "mc",
                    type = ButtonType.MEMORY,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdMemoryClear() }
                )
                CalcButton(
                    text = "m+",
                    type = ButtonType.MEMORY,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdMemoryAdd() }
                )
                CalcButton(
                    text = "m-",
                    type = ButtonType.MEMORY,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdMemorySubtract() }
                )
                CalcButton(
                    text = "mr",
                    type = ButtonType.MEMORY_ACTIVE,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdMemoryRecall() }
                )
            }

            // Row 2: AC, Backspace, +/-, ÷
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "AC",
                    type = ButtonType.RED_ACTION,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdClearAll() }
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
                    onClick = { viewModel.stdDeleteLast() }
                )
                CalcButton(
                    text = "+/-",
                    type = ButtonType.RED_ACTION,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdToggleSign() }
                )
                CalcButton(
                    text = "÷",
                    type = ButtonType.OPERATOR,
                    isDarkMode = isDarkMode,
                    fontSize = 26.sp,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendOperator("÷") }
                )
            }

            // Row 3: 7, 8, 9, ×
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "7",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendNumber("7") }
                )
                CalcButton(
                    text = "8",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendNumber("8") }
                )
                CalcButton(
                    text = "9",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendNumber("9") }
                )
                CalcButton(
                    text = "×",
                    type = ButtonType.OPERATOR,
                    isDarkMode = isDarkMode,
                    fontSize = 26.sp,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendOperator("×") }
                )
            }

            // Row 4: 4, 5, 6, -
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "4",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendNumber("4") }
                )
                CalcButton(
                    text = "5",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendNumber("5") }
                )
                CalcButton(
                    text = "6",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendNumber("6") }
                )
                CalcButton(
                    text = "-",
                    type = ButtonType.OPERATOR,
                    isDarkMode = isDarkMode,
                    fontSize = 28.sp,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendOperator("-") }
                )
            }

            // Row 5: 1, 2, 3, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "1",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendNumber("1") }
                )
                CalcButton(
                    text = "2",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendNumber("2") }
                )
                CalcButton(
                    text = "3",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendNumber("3") }
                )
                CalcButton(
                    text = "+",
                    type = ButtonType.OPERATOR,
                    isDarkMode = isDarkMode,
                    fontSize = 26.sp,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendOperator("+") }
                )
            }

            // Row 6: %, 0, ., =
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "%",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdCalculatePercentage() }
                )
                CalcButton(
                    text = "0",
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendNumber("0") }
                )
                CalcButton(
                    text = ".",
                    isDarkMode = isDarkMode,
                    fontSize = 28.sp,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdAppendDot() }
                )
                CalcButton(
                    text = "=",
                    type = ButtonType.EQUALS,
                    isDarkMode = isDarkMode,
                    fontSize = 28.sp,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.stdCalculate() }
                )
            }
        }
    }
}

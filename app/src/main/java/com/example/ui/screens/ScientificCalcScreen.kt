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
fun ScientificCalcScreen(
    viewModel: CalculatorViewModel,
    displayValue: String,
    currentExpression: String,
    isRadMode: Boolean,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    val fontSize = when {
        displayValue.length > 12 -> 30.sp
        displayValue.length > 8 -> 40.sp
        else -> 50.sp
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = currentExpression,
                fontSize = 17.sp,
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

        // Scientific Keypad (5 columns)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Row 1: ⇄, Rad/Deg, sin, cos, tan
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CalcButton(
                    text = "⇄",
                    type = ButtonType.SCIENTIFIC,
                    fontSize = 15.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { /* Inversion toggle */ }
                )
                CalcButton(
                    text = if (isRadMode) "Rad" else "Deg",
                    type = ButtonType.BADGE_ACTIVE,
                    fontSize = 14.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciToggleRadDeg() }
                )
                CalcButton(
                    text = "sin",
                    type = ButtonType.SCIENTIFIC,
                    fontSize = 14.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendFunc("sin(") }
                )
                CalcButton(
                    text = "cos",
                    type = ButtonType.SCIENTIFIC,
                    fontSize = 14.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendFunc("cos(") }
                )
                CalcButton(
                    text = "tan",
                    type = ButtonType.SCIENTIFIC,
                    fontSize = 14.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendFunc("tan(") }
                )
            }

            // Row 2: e, π, sinh, cosh, tanh
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CalcButton(
                    text = "e",
                    type = ButtonType.SCIENTIFIC,
                    fontSize = 15.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendConst("e") }
                )
                CalcButton(
                    text = "π",
                    type = ButtonType.SCIENTIFIC,
                    fontSize = 16.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendConst("π") }
                )
                CalcButton(
                    text = "sinh",
                    type = ButtonType.SCIENTIFIC,
                    fontSize = 13.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendFunc("sinh(") }
                )
                CalcButton(
                    text = "cosh",
                    type = ButtonType.SCIENTIFIC,
                    fontSize = 13.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendFunc("cosh(") }
                )
                CalcButton(
                    text = "tanh",
                    type = ButtonType.SCIENTIFIC,
                    fontSize = 13.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendFunc("tanh(") }
                )
            }

            // Row 3: %, AC, ÷, ×, ⌫
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CalcButton(
                    text = "%",
                    type = ButtonType.SCIENTIFIC,
                    fontSize = 16.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendChar("%") }
                )
                CalcButton(
                    text = "AC",
                    type = ButtonType.RED_ACTION,
                    fontSize = 16.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciClearAll() }
                )
                CalcButton(
                    text = "÷",
                    type = ButtonType.OPERATOR,
                    fontSize = 20.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendChar("÷") }
                )
                CalcButton(
                    text = "×",
                    type = ButtonType.OPERATOR,
                    fontSize = 20.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendChar("×") }
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
                            modifier = Modifier.size(19.dp)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciDeleteLast() }
                )
            }

            // Row 4: ( ), 7, 8, 9, -
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CalcButton(
                    text = "( )",
                    type = ButtonType.SCIENTIFIC,
                    fontSize = 15.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciSmartBracket() }
                )
                CalcButton(
                    text = "7",
                    fontSize = 19.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendNum("7") }
                )
                CalcButton(
                    text = "8",
                    fontSize = 19.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendNum("8") }
                )
                CalcButton(
                    text = "9",
                    fontSize = 19.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendNum("9") }
                )
                CalcButton(
                    text = "-",
                    type = ButtonType.OPERATOR,
                    fontSize = 22.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendChar("-") }
                )
            }

            // Row 5: 1/x, 4, 5, 6, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CalcButton(
                    text = "1/x",
                    type = ButtonType.SCIENTIFIC,
                    fontSize = 14.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendFunc("1/(") }
                )
                CalcButton(
                    text = "4",
                    fontSize = 19.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendNum("4") }
                )
                CalcButton(
                    text = "5",
                    fontSize = 19.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendNum("5") }
                )
                CalcButton(
                    text = "6",
                    fontSize = 19.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendNum("6") }
                )
                CalcButton(
                    text = "+",
                    type = ButtonType.OPERATOR,
                    fontSize = 20.sp,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.sciAppendChar("+") }
                )
            }

            // Rows 6 & 7: 4 columns on left, 1 column on right spanning both rows for '='
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Left 4 columns (2 rows)
                Column(
                    modifier = Modifier.weight(4f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Row 6 left: √x, 1, 2, 3
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        CalcButton(
                            text = "√x",
                            type = ButtonType.SCIENTIFIC,
                            fontSize = 14.sp,
                            isDarkMode = isDarkMode,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.sciAppendFunc("√(") }
                        )
                        CalcButton(
                            text = "1",
                            fontSize = 19.sp,
                            isDarkMode = isDarkMode,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.sciAppendNum("1") }
                        )
                        CalcButton(
                            text = "2",
                            fontSize = 19.sp,
                            isDarkMode = isDarkMode,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.sciAppendNum("2") }
                        )
                        CalcButton(
                            text = "3",
                            fontSize = 19.sp,
                            isDarkMode = isDarkMode,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.sciAppendNum("3") }
                        )
                    }

                    // Row 7 left: lg, 00, 0, .
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        CalcButton(
                            text = "lg",
                            type = ButtonType.SCIENTIFIC,
                            fontSize = 14.sp,
                            isDarkMode = isDarkMode,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.sciAppendFunc("lg(") }
                        )
                        CalcButton(
                            text = "00",
                            fontSize = 17.sp,
                            isDarkMode = isDarkMode,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.sciAppendNum("00") }
                        )
                        CalcButton(
                            text = "0",
                            fontSize = 19.sp,
                            isDarkMode = isDarkMode,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.sciAppendNum("0") }
                        )
                        CalcButton(
                            text = ".",
                            fontSize = 22.sp,
                            isDarkMode = isDarkMode,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.sciAppendDot() }
                        )
                    }
                }

                // Right column: '=' spans both rows!
                CalcButton(
                    text = "=",
                    type = ButtonType.EQUALS,
                    isDarkMode = isDarkMode,
                    fontSize = 26.sp,
                    modifier = Modifier.weight(1f),
                    height = 110.dp,
                    onClick = { viewModel.sciCalculate() }
                )
            }
        }
    }
}

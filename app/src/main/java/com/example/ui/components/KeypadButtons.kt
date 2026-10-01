package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class ButtonType {
    NUMBER,
    MEMORY,
    MEMORY_ACTIVE,
    RED_ACTION,
    OPERATOR,
    EQUALS,
    BADGE_ACTIVE,
    SCIENTIFIC
}

@Composable
fun CalcButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.NUMBER,
    icon: (@Composable () -> Unit)? = null,
    isDarkMode: Boolean = false,
    fontSize: TextUnit = 22.sp,
    height: Dp? = null
) {
    val view = LocalView.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1.0f,
        label = "button_scale"
    )

    val textColor = when (type) {
        ButtonType.EQUALS -> Color.White
        ButtonType.RED_ACTION -> if (isDarkMode) DarkCoral else CoralRed
        ButtonType.OPERATOR -> if (isDarkMode) DarkCoral else CoralRed
        ButtonType.BADGE_ACTIVE -> if (isDarkMode) DarkCoral else CoralRed
        ButtonType.MEMORY_ACTIVE -> if (isDarkMode) DarkCoral else CoralRed
        ButtonType.MEMORY -> if (isDarkMode) DarkMemoryText else LightMemoryText
        ButtonType.NUMBER, ButtonType.SCIENTIFIC -> if (isDarkMode) DarkTextPrimary else LightTextPrimary
    }

    val backgroundColor: Color? = when (type) {
        ButtonType.EQUALS -> null // Uses gradient
        ButtonType.OPERATOR, ButtonType.BADGE_ACTIVE -> if (isDarkMode) Color(0xFFF87171).copy(alpha = 0.12f) else Color(0xFFEF4444).copy(alpha = 0.08f)
        ButtonType.MEMORY, ButtonType.MEMORY_ACTIVE, ButtonType.NUMBER, ButtonType.SCIENTIFIC, ButtonType.RED_ACTION ->
            if (isDarkMode) Color.White.copy(alpha = 0.05f) else Color.White.copy(alpha = 0.65f)
    }

    val gradientBrush = if (type == ButtonType.EQUALS) {
        Brush.linearGradient(
            colors = if (isDarkMode) listOf(LightCoral, DarkCoral) else listOf(LightCoral, CoralRed)
        )
    } else null

    val borderColor = if (type == ButtonType.EQUALS) {
        Color.Transparent
    } else if (type == ButtonType.OPERATOR || type == ButtonType.BADGE_ACTIVE) {
        CoralRed.copy(alpha = 0.25f)
    } else {
        if (isDarkMode) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.6f)
    }

    val shape = if (height != null) RoundedCornerShape(26.dp) else CircleShape

    Box(
        modifier = modifier
            .scale(scale)
            .then(
                if (height != null) {
                    Modifier.fillMaxWidth().height(height)
                } else {
                    Modifier.aspectRatio(1f)
                }
            )
            .clip(shape)
            .then(
                if (gradientBrush != null) {
                    Modifier.background(gradientBrush)
                } else if (backgroundColor != null) {
                    Modifier.background(backgroundColor)
                } else {
                    Modifier
                }
            )
            .border(1.dp, borderColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                onClick()
            }
            .testTag("btn_${text.replace(" ", "_")}"),
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            icon()
        } else {
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize,
                fontWeight = if (type == ButtonType.EQUALS || type == ButtonType.OPERATOR || type == ButtonType.RED_ACTION || type == ButtonType.NUMBER) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

package com.example.hellokitty.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle.Default.copy(
        fontFamily = FontFamily.Serif,
        color = Color(0xE5000000),
        fontSize = 16.sp,
    ),
    bodySmall = TextStyle.Default.copy(
        fontFamily = FontFamily.Serif,
        color = Color(0x66000000),
        fontSize = 12.sp,
    ),
    labelSmall = TextStyle.Default.copy(
        fontFamily = FontFamily.Serif,
        color = Color(0x66000000),
        fontSize = 11.sp,
    ),
    headlineLarge = TextStyle.Default.copy(
        fontFamily = FontFamily.Serif,
        color = Color(0xB2000000),
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    ),
)
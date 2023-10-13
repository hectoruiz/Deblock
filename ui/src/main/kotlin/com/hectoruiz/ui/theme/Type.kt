package com.hectoruiz.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hectoruiz.ui.R


// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontFamily = FontFamily(Font(R.font.basis_grotesque_pro)),
        fontWeight = FontWeight(500),
    ),
    bodyLarge = TextStyle(
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontFamily = FontFamily(Font(R.font.basis_grotesque_pro)),
        fontWeight = FontWeight(500),
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = FontFamily(Font(R.font.basis_grotesque_pro)),
        fontWeight = FontWeight(500),
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontFamily = FontFamily(Font(R.font.basis_grotesque_pro)),
        fontWeight = FontWeight(500),
    ),
    labelSmall = TextStyle(
        fontSize = 13.sp,
        lineHeight = 16.sp,
        fontFamily = FontFamily(Font(R.font.basis_grotesque_pro)),
        fontWeight = FontWeight(500),
    )
)
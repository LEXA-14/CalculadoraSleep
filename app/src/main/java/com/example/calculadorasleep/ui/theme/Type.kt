package com.example.calculadorasleep.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.calculadorasleep.R

val PlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold)
)

val BeVietnamPro = FontFamily(
    Font(R.font.be_vietnam_pro_regular, FontWeight.Normal),
    Font(R.font.be_vietnam_pro_medium, FontWeight.Medium)
)

val Typography = Typography(
    // Headlines (Plus Jakarta Sans)
    headlineLarge = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = TextDark
    ),
    headlineMedium = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        color = TextDark
    ),
    bodyLarge = TextStyle(
        fontFamily = BeVietnamPro,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = TextDark
    ),
    bodyMedium = TextStyle(
        fontFamily = BeVietnamPro,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = TextDark
    ),
    labelLarge = TextStyle(
        fontFamily = BeVietnamPro,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)
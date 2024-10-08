package com.gianghv.uniqlo.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.gianghv.uniqlo.theme.Fonts.popinsFontFamily
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.*


object Fonts {
    val popinsFontFamily
        @Composable get() = FontFamily(
            Font(
                Res.font.poppins_regular,
                FontWeight.Normal,
                FontStyle.Normal
            ),
            Font(
                Res.font.poppins_medium,
                FontWeight.Medium,
                FontStyle.Normal
            ),
            Font(
                Res.font.poppins_semibold,
                FontWeight.SemiBold,
                FontStyle.Normal
            ),
            Font(
                Res.font.poppins_bold,
                FontWeight.Bold,
                FontStyle.Normal
            ),

            )
}

val Typography
    @Composable
    get() = Typography(
        displayMedium = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = popinsFontFamily
        ),
        headlineLarge = TextStyle(
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = popinsFontFamily
        ),
        titleMedium = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = popinsFontFamily
        ),
        titleSmall = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = popinsFontFamily
        ),
        bodyLarge = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = popinsFontFamily
        ),
        bodyMedium = TextStyle(
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = popinsFontFamily
        ),
        bodySmall = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.Thin,
            fontFamily = popinsFontFamily
        )
    )

package com.gianghv.uniqlo.presentation.screen.productdetail.components

import androidx.compose.ui.graphics.Color
import com.gianghv.uniqlo.theme.Orange
import com.gianghv.uniqlo.theme.Silver

enum class VariationSize(val size: String) {
    XS("XS"), S("S"), M("M"), L("L"), XL("XL"), XXL("XXL"),
}

enum class VariationColor(val color: Color) {
    BLUE(Color.Blue), GREY(Color.Gray), BLACK(Color.Black), YELLOW(Color.Yellow), RED(Color.Red), WHITE(Color.White), PINK(Color.Magenta), SILVER(Silver), GREEN(
        Color.Green
    ),
    ORANGE(Orange), PURPLE(Color(0xFF800080)), GRAY(Color.Gray), OTHER(Color.White)
}

fun VariationColor.getTextColor(): Color {
    return when (this) {
        VariationColor.BLACK, VariationColor.BLUE, VariationColor.GREY, VariationColor.RED, VariationColor.PURPLE -> Color.White
        else -> Color.Black
    }
}

fun String.toVariationColor(): VariationColor {
    return when (this.lowercase()) {
        "blue" -> VariationColor.BLUE
        "grey" -> VariationColor.GREY
        "black" -> VariationColor.BLACK
        "yellow" -> VariationColor.YELLOW
        "red" -> VariationColor.RED
        "white" -> VariationColor.WHITE
        "ping" -> VariationColor.PINK
        "silver" -> VariationColor.SILVER
        "green" -> VariationColor.GREEN
        "orange" -> VariationColor.ORANGE
        "purple" -> VariationColor.PURPLE
        "gray" -> VariationColor.GRAY
        else -> VariationColor.OTHER
    }
}

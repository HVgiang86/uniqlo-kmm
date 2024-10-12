package com.gianghv.uniqlo.presentation.screen.productdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.domain.ProductVariation

@Composable
fun VariationSizeButton(size: VariationSize, isSelected: Boolean, onSelected: (VariationSize) -> Unit) {
    val selectedColor = MaterialTheme.colorScheme.primary
    val unselectedColor = Color.Gray

    val outlineColor = if (isSelected) selectedColor else unselectedColor
    val textColor = if (isSelected) Color.White else Color.Black
    val backgroundColor = if (isSelected) selectedColor else Color.Transparent

    OutlinedButton(
        onClick = {
            onSelected(size)
        },
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, outlineColor),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor, containerColor = backgroundColor)
    ) {
        Text(text = size.name.uppercase(), color = textColor)
    }
}

@Composable
fun VariationColorButton(variation: ProductVariation, isSelected: Boolean, onSelected: (ProductVariation) -> Unit) {
    if (variation.color == null) return
    val color = variation.color.toVariationColor()

    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF800080), Color(0xFFDA70D6)) // Purple to Orchid gradient
    )
    val unselectedColor = Color.Gray

    val outlineBrush = if (isSelected) gradientBrush else Brush.linearGradient(listOf(unselectedColor, unselectedColor))
    val textColor = color.getTextColor()

    val selectedIndicatorColor = if (color == VariationColor.RED) Color.White else Color.Red
    val selectedIndicatorIconColor = if (color == VariationColor.RED) Color.Red else Color.White

    Box(modifier = Modifier.wrapContentSize().padding(4.dp)) {
        OutlinedButton(
            onClick = {
                onSelected(variation)
            },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, outlineBrush),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor, containerColor = color.color)
        ) {
            Text(text = variation.color.uppercase(), color = textColor)
        }

        if (isSelected) {
            Box(
                modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 2.dp).background(selectedIndicatorColor)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = selectedIndicatorIconColor,
                    modifier = Modifier.size(16.dp).align(Alignment.TopEnd)
                )
            }
        }
    }
}

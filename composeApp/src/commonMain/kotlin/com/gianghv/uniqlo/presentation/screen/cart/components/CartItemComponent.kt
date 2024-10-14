package com.gianghv.uniqlo.presentation.screen.cart.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.domain.CartItem
import com.gianghv.uniqlo.presentation.screen.wishlist.ProductImage
import com.gianghv.uniqlo.theme.Black
import com.gianghv.uniqlo.util.ext.toCurrencyText

@Composable
fun CartItemWidget(
    boxWidth: Dp,
    modifier: Modifier = Modifier,
    cartItem: CartItem,
    isSelected: Boolean,
    onCheckBoxClick: (Boolean) -> Unit,
    onClick: (CartItem) -> Unit,
    onUpdateQuantity: (Int) -> Unit,
    onRequestDelete: (CartItem) -> Unit
) {
    Row(modifier = modifier.padding(8.dp).fillMaxWidth().wrapContentHeight()) {
        Checkbox(
            checked = isSelected, onCheckedChange = onCheckBoxClick, modifier = Modifier.align(Alignment.CenterVertically)
        )

        ProductImage(
            modifier = Modifier.height(boxWidth * 0.25f).aspectRatio(1f).padding(end = 8.dp).align(Alignment.Top).clickable {
                onClick(cartItem)
            }, imageUrl = cartItem.variation?.image ?: cartItem.variation?.product?.defaultImage ?: ""
        )

        Spacer(modifier = Modifier.width(16.dp))

        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(end = 8.dp).align(Alignment.Top)) {
            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                Text(cartItem.variation?.product?.name ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Black,
                    minLines = 1,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable {
                        onClick(cartItem)
                    })
                Text(
                    cartItem.variation?.product?.category?.name ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    minLines = 1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val priceText = ((cartItem.variation?.price ?: 0.0) * 1000.0).toCurrencyText()
                Text(
                    priceText, style = MaterialTheme.typography.titleSmall, color = Color.Red, minLines = 1, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Text(
                    cartItem.size ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    minLines = 1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            QuantityComponent(quantity = cartItem.quantity ?: 1,
                modifier = Modifier.align(Alignment.BottomEnd),
                onQuantityChange = onUpdateQuantity,
                onRequestDelete = {
                    onRequestDelete(cartItem)
                })
        }
    }
}


@Composable
fun QuantityComponent(modifier: Modifier = Modifier, quantity: Int, onQuantityChange: (Int) -> Unit, onRequestDelete: () -> Unit = {}) {
    Row(modifier = modifier.wrapContentSize().clip(RoundedCornerShape(4.dp)).border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(4.dp))) {
        Box(modifier = Modifier.align(Alignment.CenterVertically).clickable {
            if (quantity > 1) onQuantityChange(quantity - 1) else onRequestDelete()
        }) {
            Text(text = "-", modifier = Modifier.padding(8.dp), color = Black, textAlign = TextAlign.Center)
        }

        Text(
            text = quantity.toString(),
            modifier = Modifier.padding(horizontal = 4.dp).align(Alignment.CenterVertically),
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Box(modifier = Modifier.align(Alignment.CenterVertically).clickable {
            onQuantityChange(quantity + 1)
        }) {
            Text(text = "+", modifier = Modifier.padding(8.dp), color = Black, textAlign = TextAlign.Center)
        }
    }
}

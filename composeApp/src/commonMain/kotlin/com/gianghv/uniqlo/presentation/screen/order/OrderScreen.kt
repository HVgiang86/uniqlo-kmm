package com.gianghv.uniqlo.presentation.screen.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.domain.CartItem
import com.gianghv.uniqlo.presentation.screen.cart.components.QuantityComponent
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.presentation.screen.wishlist.ProductImage
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.ext.toCurrencyText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(viewModel: OrderViewModel, carts: List<CartItem>, onBack: () -> Unit, navigateTo: (MainScreenDestination) -> Unit) {
    val state by viewModel.state.asState()

    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), title = {
            Text(text = "Create Order", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        }, actions = {
            IconButton(onClick = {

            }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
        }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        })
    }) {
        val cartList = state.carts
        var boxWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Box(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding(), bottom = 80.dp).onGloballyPositioned { layoutCoordinates ->
            val widthInPx = layoutCoordinates.size.width
            boxWidth = with(density) { widthInPx.toDp() }
        }) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(top = 8.dp).align(Alignment.TopCenter), thickness = 1.dp, color = Color.LightGray)
            OrderConfirmPanel()
        }
        LazyColumn {
            items(carts.size) { index ->
                CartItemComponent(cartItem = carts[index], boxWidth = boxWidth, onClick = {})
            }
        }

    }
}


@Composable
fun OrderConfirmPanel(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().padding(16.dp).background(color = Color.Yellow)) {
        Text(text = "Order Confirm Panel")
    }
}

@Composable
fun CartItemComponent(modifier: Modifier = Modifier, cartItem: CartItem, boxWidth: Dp, onClick: (CartItem) -> Unit) {
    Row(modifier = modifier.padding(8.dp).fillMaxWidth().wrapContentHeight()) {
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

                val discountPercentage = (cartItem.variation?.product?.discountPercentage ?: 0) % 101
                val originalPrice = cartItem.variation?.price ?: 0.0
                val finalPrice = originalPrice - originalPrice * discountPercentage * 1.0 / 100

                val priceText = (finalPrice * 1000.0).toCurrencyText()
                Text(
                    priceText, style = MaterialTheme.typography.titleSmall, color = Color.Red, minLines = 1, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${cartItem.size ?: ""}, ${cartItem.variation?.color?.uppercase() ?: ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    minLines = 1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            QuantityComponent(quantity = cartItem.quantity ?: 1,
                modifier = Modifier.align(Alignment.BottomEnd),
                onQuantityChange = {},
                onRequestDelete = {},
                allowChangeQuantity = false
            )
        }
    }
}

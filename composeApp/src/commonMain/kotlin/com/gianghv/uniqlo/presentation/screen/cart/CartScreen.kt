package com.gianghv.uniqlo.presentation.screen.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.domain.CartItem
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.component.MyAlertDialog
import com.gianghv.uniqlo.presentation.component.RedFilledTextButton
import com.gianghv.uniqlo.presentation.screen.cart.components.CartItemWidget
import com.gianghv.uniqlo.theme.Black
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.ext.toCurrencyText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: CartViewModel, onBack: () -> Unit) {
    val state by viewModel.state.asState()
    val scope = rememberCoroutineScope()

    val confirmDeleteDialog = remember { mutableStateOf<CartItem?>(null) }

    LaunchedEffect(Unit) {
        viewModel.sendEvent(CartUiEvent.LoadOrder)
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        AppErrorDialog(state.error?.throwable, onDismissRequest = {})
    }

    val cartItems = state.cartItems

    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), title = {
            Text(text = "Cart", style = MaterialTheme.typography.titleMedium, color = Color.Black)
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
        var boxWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "No items in cart", style = MaterialTheme.typography.titleMedium, color = Color.Black, modifier = Modifier.align(Alignment.Center))
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (state.selectedItems.isNotEmpty()) {
                Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().align(Alignment.Start)) {
                    Checkbox(checked = state.selectedItems.size == cartItems.size, onCheckedChange = {
                        if (it) viewModel.sendEvent(CartUiEvent.SelectAll) else viewModel.sendEvent(CartUiEvent.UnSelectAll)
                    }, modifier = Modifier.align(Alignment.CenterVertically))

                    Text(text = "${state.selectedItems.size} items selected", color = Color.Black, modifier = Modifier.align(Alignment.CenterVertically))
                }

                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            }

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f).onGloballyPositioned { layoutCoordinates ->
                val widthInPx = layoutCoordinates.size.width
                boxWidth = with(density) { widthInPx.toDp() }
            }) {
                items(cartItems) { item ->
                    val isLastItem = cartItems.last() == item
                    val isSelected = state.selectedItems.contains(item)

                    CartItemWidget(boxWidth = boxWidth, cartItem = item, isSelected = isSelected, onCheckBoxClick = {
                        if (it) viewModel.sendEvent(CartUiEvent.SelectItem(item)) else viewModel.sendEvent(CartUiEvent.UnSelectItem(item))
                    }, onClick = {

                    }, onUpdateQuantity = {
                        viewModel.sendEvent(CartUiEvent.UpdateQuantity(item, it))
                    }, onRequestDelete = {
                        confirmDeleteDialog.value = item
                    })

                    if (!isLastItem) {
                        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                    }
                }
            }

            Box(modifier = Modifier.wrapContentWidth().wrapContentHeight().align(Alignment.End)) {
                Row(modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp)) {
                    Column(modifier = Modifier.weight(1.5f)) {

                        Text("Price", style = MaterialTheme.typography.bodySmall, color = Black)

                        val totalPrice = state.selectedItems.fold(0.0) { acc, cartItem ->
                            val discountPercentage = (cartItem.variation?.product?.discountPercentage ?: 0) % 101
                            val originalPrice = cartItem.variation?.price ?: 0.0
                            val finalPrice = originalPrice - originalPrice * discountPercentage * 1.0 / 100

                            acc + finalPrice
                        }
                        val priceText = (totalPrice*1000.0).toCurrencyText()
                        Text(
                            priceText,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Red,
                            minLines = 1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    RedFilledTextButton(onClick = {

                    }, modifier = Modifier.weight(1f), text = {
                        Text("Checkout", style = MaterialTheme.typography.titleSmall, color = Color.White)
                    })
                }
            }

        }

        val itemToDelete = confirmDeleteDialog.value
        if (itemToDelete != null) {
            MyAlertDialog(title = "Xác nhận xoá", content = "Bạn xác nhận xoá sản phẩm này khỏi giỏ hàng?", rightBtnTitle = "OK", rightBtn = {
                viewModel.sendEvent(CartUiEvent.DeleteItem(itemToDelete))
                confirmDeleteDialog.value = null
            }, leftBtnTitle = "Hủy", leftBtn = {
                confirmDeleteDialog.value = null
            })
        }

    }
}

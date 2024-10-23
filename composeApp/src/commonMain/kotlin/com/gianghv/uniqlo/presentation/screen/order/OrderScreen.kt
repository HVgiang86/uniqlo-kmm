package com.gianghv.uniqlo.presentation.screen.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.core.Menu
import com.composables.core.MenuButton
import com.composables.core.MenuContent
import com.composables.core.MenuItem
import com.composables.core.rememberMenuState
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.domain.CartItem
import com.gianghv.uniqlo.presentation.component.AppOutlinedTextField
import com.gianghv.uniqlo.presentation.component.MyAlertDialog
import com.gianghv.uniqlo.presentation.component.RedFilledTextButton
import com.gianghv.uniqlo.presentation.screen.cart.components.QuantityComponent
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.presentation.screen.wishlist.ProductImage
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.ext.toCurrencyText
import com.gianghv.uniqlo.util.logging.AppLogger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(viewModel: OrderViewModel, carts: List<CartItem>, onBack: () -> Unit, navigateTo: (MainScreenDestination) -> Unit) {
    val state by viewModel.state.asState()
    var backConfirmVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.sendEvent(OrderUiEvent.SaveCartList(carts))
//        viewModel.sendEvent(OrderUiEvent.LoadSavedOrderInfo)
        viewModel.sendEvent(OrderUiEvent.LoadUserDetail(WholeApp.USER_ID))
    }

    if (backConfirmVisible) {
        MyAlertDialog(title = "Huỷ bỏ order?", content = "Bạn có chắc muốn huỷ không?", rightBtn = {
            onBack()
            backConfirmVisible = false
        }, rightBtnTitle = "Hủy", leftBtnTitle = "Thôi", leftBtn = {
            backConfirmVisible = false
        }, onCanceled = {
            backConfirmVisible = false
        })
    }

    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), title = {
            Text(text = "Create Order", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        }, actions = {
            IconButton(onClick = {

            }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
        }, navigationIcon = {
            IconButton(onClick = {
                backConfirmVisible = true
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        })
    }) {
        val cartList = state.carts
        var boxWidth by remember { mutableStateOf(0.dp) }
        var confirmBarHeight by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current
        val totalPrice = cartList.sumOf { it.variation?.price ?: 0.0 }
        val totalPayment = (totalPrice + 50.0 + 30.0) * 1000

        Box(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
            .onGloballyPositioned { layoutCoordinates ->
                val widthInPx = layoutCoordinates.size.width
                boxWidth = with(density) { widthInPx.toDp() }
            }) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).align(Alignment.TopCenter), thickness = 1.dp, color = Color.LightGray)

            LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 8.dp).imePadding()) {
                items(cartList.size + 3) { index ->
                    if (index == 0) {
                        Text(
                            "Order Items",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                        )
                    } else if (index <= cartList.size && index > 0) {
                        CartItemComponent(modifier = Modifier.padding(horizontal = 32.dp), cartItem = cartList[index - 1], boxWidth = boxWidth, onClick = {})
                    } else if (index == cartList.size + 1) {
                        AppLogger.d("Hehe1 ${state}")
                        OrderInfoComponent(totalPayment = totalPrice,
                            paymentMethod = PaymentMethod.Cash,
                            phone = state.phone ?: "",
                            address = state.address ?: "",
                            email = state.email ?: "",
                            onOrderInfoChange = { _, _, _, _ ->

                            })
                    } else if (index == cartList.size + 2) {
                        Spacer(modifier = Modifier.height(confirmBarHeight))
                    }
                }
            }

            OrderConfirmPanel(modifier = Modifier.align(Alignment.BottomCenter).onGloballyPositioned {
                val heightInPx = it.size.height
                confirmBarHeight = with(density) { heightInPx.toDp() }
            }, totalPayment = totalPayment, onOrderConfirm = {})
        }
    }
}

@Composable
fun OrderInfoComponent(
    modifier: Modifier = Modifier,
    totalPayment: Double = 0.0,
    paymentMethod: PaymentMethodBase,
    phone: String,
    address: String,
    email: String,
    onOrderInfoChange: (PaymentMethodBase, String, String, String) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 32.dp, vertical = 8.dp)) {
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))
        Text(
            "Summary", style = MaterialTheme.typography.titleMedium, color = Color.Black, modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(vertical = 8.dp)) {
            Text(
                "Subtotal",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
            )

            val totalPaymentText = (totalPayment*1000).toCurrencyText()
            Text(
                totalPaymentText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
            )
        }

        val shippingPriceText = (50.0 * 1000).toCurrencyText()
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(vertical = 8.dp)) {
            Text(
                "Shipment Price",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
            )
            Text(
                shippingPriceText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
            )
        }

        val insurancePriceText = (30.0 * 1000).toCurrencyText()
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(vertical = 8.dp)) {
            Text(
                "Insurance",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
            )
            Text(
                insurancePriceText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
            )
        }

        Text(
            "Payment Method", style = MaterialTheme.typography.titleMedium, color = Color.Black, modifier = Modifier.padding(vertical = 8.dp)
        )

        DropdownPaymentMethod(modifier = Modifier.fillMaxWidth(), initial = paymentMethod)

        Text(
            "Phone number", style = MaterialTheme.typography.titleMedium, color = Color.Black, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        AppOutlinedTextField(modifier = Modifier.fillMaxWidth().wrapContentHeight().heightIn(min = 52.dp),
            initialValue = phone,
            onValueChange = {},
            onMessageSent = {},
            maxLines = 4,
            placeholder = "09xxxxxxxx",
            imeAction = ImeAction.Done
        )

        Text(
            "Address", style = MaterialTheme.typography.titleMedium, color = Color.Black, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        AppOutlinedTextField(modifier = Modifier.fillMaxWidth().wrapContentHeight().heightIn(min = 52.dp),
            initialValue = address,
            onValueChange = {},
            onMessageSent = {},
            maxLines = 4,
            placeholder = "Address",
            imeAction = ImeAction.Done
        )

        Text(
            "Send Receipt to ", style = MaterialTheme.typography.titleMedium, color = Color.Black, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        AppOutlinedTextField(modifier = Modifier.fillMaxWidth(),
            initialValue = email,
            onValueChange = {},
            onMessageSent = {},
            placeholder = "Email address",
            imeAction = ImeAction.Done
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun DropdownPaymentMethod(modifier: Modifier = Modifier, initial: PaymentMethodBase? = PaymentMethod.Cash) {
    val state = rememberMenuState(expanded = false)
    val selected = remember { mutableStateOf(initial) }
    val list = listOf(PaymentMethod.Cash, PaymentMethod.VNPay)

    Menu(state = state, modifier = modifier) {
        MenuButton(
            Modifier.clip(RoundedCornerShape(6.dp)).fillMaxWidth().height(52.dp).border(0.5.dp, Color(0xFFBDBDBD), RoundedCornerShape(6.dp))
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Row(modifier = Modifier.align(Alignment.CenterStart).padding(horizontal = 8.dp)) {
                    Icon(
                        imageVector = selected.value?.getIcon() ?: Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    BasicText(
                        selected.value?.getTitle() ?: "Unnamed Payment Method",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.CenterVertically).padding(start = 16.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 8.dp).align(Alignment.CenterEnd)
                )
            }
        }

        MenuContent(
            modifier = modifier.padding(horizontal = 32.dp).border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp)).background(Color.White).padding(4.dp),
        ) {
            list.forEachIndexed { _, option ->
                MenuItem(modifier = Modifier.clip(RoundedCornerShape(4.dp)), onClick = {
                    state.expanded = false
                    selected.value = option
                }) {
                    Row(modifier = Modifier.fillMaxWidth().height(52.dp)) {
                        Icon(
                            imageVector = option.getIcon(), contentDescription = null, modifier = Modifier.align(Alignment.CenterVertically).padding(start = 8.dp)
                        )
                        BasicText(
                            option.getTitle(),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.align(Alignment.CenterVertically).padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun OrderConfirmPanel(modifier: Modifier = Modifier, totalPayment: Double = 0.0, onOrderConfirm: () -> Unit = {}) {
    Card(
        shape = RectangleShape,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter), thickness = 1.dp, color = Color.LightGray)

            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 32.dp).padding(top = 16.dp, bottom = 40.dp)) {
                var isTermConfirmed by remember { mutableStateOf(false) }
                Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                    Checkbox(
                        checked = isTermConfirmed, onCheckedChange = {
                            isTermConfirmed = it
                        }, modifier = Modifier.align(Alignment.CenterVertically).padding(end = 8.dp).clip(RoundedCornerShape(10.dp))
                    )

                    Text(
                        "I agree to payment Terms & Condition",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                RedFilledTextButton(onClick = onOrderConfirm, enable = isTermConfirmed, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                    Text("Pay ${totalPayment.toCurrencyText()}", style = MaterialTheme.typography.titleMedium, color = Color.White)
                }
            }
        }
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

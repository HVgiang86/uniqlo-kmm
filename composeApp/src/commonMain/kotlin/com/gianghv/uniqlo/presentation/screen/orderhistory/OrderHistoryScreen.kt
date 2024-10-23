package com.gianghv.uniqlo.presentation.screen.orderhistory

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gianghv.uniqlo.domain.OrderDetail
import com.gianghv.uniqlo.domain.OrderHistory
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.component.RedFilledTextButton
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.presentation.screen.order.PaymentMethod
import com.gianghv.uniqlo.presentation.screen.wishlist.ProductImage
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.ext.toCurrencyText
import com.gianghv.uniqlo.util.logging.AppLogger
import org.jetbrains.compose.resources.vectorResource
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_cart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(viewModel: OrderHistoryViewModel, navigateTo: (MainScreenDestination) -> Unit) {
    val state by viewModel.state.asState()

    LaunchedEffect(Unit) {
        viewModel.sendEvent(OrderHistoryUiEvent.LoadOrderHistory)
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        AppErrorDialog(state.error?.throwable, onDismissRequest = { })
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "Order History", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        }, actions = {
            val cartCount = 0
            Box(modifier = Modifier.wrapContentSize()) {
                IconButton(onClick = {
                    navigateTo(MainScreenDestination.Cart)
                }) {
                    Icon(imageVector = vectorResource(Res.drawable.ic_cart), contentDescription = null)
                }

                if (cartCount > 0) {

                    Box(modifier = Modifier.align(Alignment.TopEnd).padding(end = 4.dp, top = 4.dp).wrapContentSize().clip(CircleShape).background(Color.Red)) {
                        Text(
                            text = "$cartCount",
                            modifier = Modifier.padding(horizontal = 2.dp),
                            fontSize = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp).fontSize,
                            color = Color.White,
                        )
                    }
                }
            }

            IconButton(onClick = {

            }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
        })
    }) { innerPadding ->
        val orders = state.orderList

        var boxWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Box(modifier = Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding(), bottom = 80.dp).onGloballyPositioned { layoutCoordinates ->
            val widthInPx = layoutCoordinates.size.width
            boxWidth = with(density) { widthInPx.toDp() }
        }) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 8.dp)) {
                items(orders) { order ->
                    OrderHistoryItem(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(vertical = 8.dp, horizontal = 8.dp),
                        order = order,
                        onClick = {},
                        boxWidth = boxWidth,
                        onPayNowClick = { orderHistory ->
                            AppLogger.d("[hehe]Pay now clicked $orderHistory")
                            navigateTo(
                                MainScreenDestination.Payment(
                                    mapOf(
                                        MainScreenDestination.Payment.ORDER_ID_KEY to orderHistory.id,
                                        MainScreenDestination.Payment.AMOUNT_KEY to (orderHistory.total?.toDouble() ?: 0.0)
                                    )
                                )
                            )
                        })
                }
            }
        }
    }
}


@Composable
fun OrderHistoryItem(
    modifier: Modifier = Modifier, order: OrderHistory, onClick: (OrderHistory) -> Unit, boxWidth: Dp, onPayNowClick: (OrderHistory) -> Unit = {}
) {
    Card(
        modifier = modifier.wrapContentHeight().fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp)) {
            Text(text = "#${order.name}", style = MaterialTheme.typography.titleMedium, color = Color.Black)
            Text(text = order.status ?: "", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            HorizontalDivider(modifier = Modifier.height(4.dp).padding(top = 8.dp, bottom = 16.dp), thickness = 1.dp, color = Color.LightGray)

            val detailSize = order.details?.size ?: 0
            for (i in 0 until detailSize) {
                if (i <= 1) {
                    val orderDetail = order.details?.get(i) ?: continue
                    OrderDetailComponent(modifier = Modifier.fillMaxWidth().wrapContentHeight(), orderDetail = orderDetail, boxWidth = boxWidth, onClick = {})
                }
            }

            if (detailSize > 2) {
                Text(text = "and ${detailSize - 2} more items", style = MaterialTheme.typography.titleSmall, color = Color.Black)
            }

            HorizontalDivider(modifier = Modifier.height(4.dp).padding(top = 8.dp, bottom = 16.dp), thickness = 1.dp, color = Color.LightGray)

            Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(4.dp)) {
                Text(text = "Total: ", style = MaterialTheme.typography.titleSmall, color = Color.Black)
                val total = (order.total?.toDouble() ?: 0.0).toCurrencyText()
                Text(text = total, style = MaterialTheme.typography.titleSmall, color = Color.Red)
            }

            Box {
                Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(4.dp).align(Alignment.CenterStart)) {
                    val paymentMethod = if (order.pay == true) PaymentMethod.VNPay else PaymentMethod.Cash

                    Icon(imageVector = paymentMethod.getIcon(), contentDescription = null, modifier = Modifier.align(Alignment.CenterVertically))

                    Text(
                        text = paymentMethod.getTitle(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterVertically).padding(start = 8.dp)
                    )
                }

                if (order.status == "pending" && order.pay == true) {
                    PayNowButton(onClick = { onPayNowClick(order) }, boxWidth = boxWidth, modifier = Modifier.align(Alignment.CenterEnd))
                }
            }
        }
    }
}

@Composable
fun PayNowButton(modifier: Modifier = Modifier, onClick: () -> Unit, boxWidth: Dp) {
    RedFilledTextButton(
        onClick = onClick,
        modifier = modifier.width(boxWidth * 0.4f).wrapContentHeight().padding(8.dp),
        text = {
            Text(text = "Pay Now", style = MaterialTheme.typography.titleSmall, color = Color.White)
        },
    )
}

@Composable
fun OrderDetailComponent(modifier: Modifier = Modifier, orderDetail: OrderDetail, boxWidth: Dp, onClick: (OrderDetail) -> Unit) {
    val variation = orderDetail.variation ?: return
    Row(modifier = modifier.padding(8.dp).fillMaxWidth().wrapContentHeight()) {
        ProductImage(
            modifier = Modifier.height(boxWidth * 0.25f).aspectRatio(1f).padding(end = 8.dp).align(Alignment.Top).clickable {
                onClick(orderDetail)
            }, imageUrl = variation.image ?: ""
        )

        Spacer(modifier = Modifier.width(16.dp))

        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(end = 8.dp).align(Alignment.Top)) {
            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                Text(variation.name ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Black,
                    minLines = 1,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable {
                        onClick(orderDetail)
                    })

                val priceText = ((variation.price?.toDouble() ?: 0.0) * 1000.0).toCurrencyText()
                Text(
                    priceText, style = MaterialTheme.typography.titleSmall, color = Color.Red, minLines = 1, maxLines = 1, overflow = TextOverflow.Ellipsis
                )

                Text(
                    "x${orderDetail.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    minLines = 1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

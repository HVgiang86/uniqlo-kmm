package com.gianghv.uniqlo.presentation.screen.wishlist

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dokar.sonner.Toaster
import com.dokar.sonner.rememberToasterState
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.component.WishListToolBar
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.ext.toCurrencyText
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.rememberAsyncImageState
import com.github.panpf.sketch.request.ComposableImageOptions
import com.github.panpf.sketch.request.placeholder
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_dark_uniqlo

@Composable
fun WishListScreen(viewModel: WishListViewModel, navigateTo: (MainScreenDestination) -> Unit) {
    val state by viewModel.state.asState()

    val uriHandler = LocalUriHandler.current
    val toasterState = rememberToasterState()

    LaunchedEffect(Unit) {
        viewModel.sendEvent(WishListUiEvent.LoadFavorite)
        viewModel.sendEvent(WishListUiEvent.LoadCartCount)
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        AppErrorDialog(state.error?.throwable, onDismissRequest = { })
    }

    Scaffold(topBar = {
        WishListToolBar(cartCount = state.cartCount, onCartClick = {
            navigateTo(MainScreenDestination.Cart)
        }, onMenuClick = {

        })
    }) {
        val products = state.favoriteProductList

        var boxWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Toaster(state = toasterState)

        HorizontalDivider(modifier = Modifier.height(1.dp).padding(bottom = 8.dp), color = Color.LightGray)

        Column(modifier = Modifier.fillMaxSize().onGloballyPositioned { layoutCoordinates ->
            val widthInPx = layoutCoordinates.size.width
            boxWidth = with(density) { widthInPx.toDp() }
        }.padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())) {
            LazyColumn {
                items(products.size) { index ->
                    ItemProduct(boxWidth = boxWidth, product = products[index], onClick = {
                        navigateTo(MainScreenDestination.ProductDetail(mapOf(MainScreenDestination.ProductDetail.PRODUCT_ID_KEY to it.id)))
                    }, onRemoveFavoriteClick = {
                        viewModel.sendEvent(WishListUiEvent.SetFavorite(it, false))
                    })

                    if (index < products.size - 1) HorizontalDivider(modifier = Modifier.height(1.dp).padding(horizontal = 8.dp), color = Color.LightGray)
                }
            }
        }
    }
}

@Composable
fun ItemProduct(boxWidth: Dp, modifier: Modifier = Modifier, product: Product, onClick: (Product) -> Unit, onRemoveFavoriteClick: (Product) -> Unit) {
    Row(modifier = modifier.padding(8.dp).fillMaxWidth().wrapContentHeight()) {
        ProductImage(
            modifier = Modifier.height(boxWidth * 0.25f).aspectRatio(1f).padding(end = 8.dp).align(Alignment.Top).clickable {
                onClick(product)
            }, imageUrl = product.defaultImage ?: ""
        )

        Spacer(modifier = Modifier.width(16.dp))

        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(end = 8.dp).align(Alignment.Top)) {
            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().clickable {
                onClick(product)
            }) {
                Text(
                    product.name ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Black,
                    minLines = 1,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    product.category?.name ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    minLines = 1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val priceText = ((product.price ?: 0.0) * 1000.0).toCurrencyText()
                Text(
                    priceText, style = MaterialTheme.typography.titleSmall, color = Color.Red, minLines = 1, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(modifier = Modifier.align(Alignment.BottomEnd), onClick = {
                onRemoveFavoriteClick(product)
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
fun ProductImage(modifier: Modifier = Modifier, imageUrl: String) {
    val isImageLoadedSuccessfully = rememberAsyncImageState(ComposableImageOptions {
        placeholder(Res.drawable.ic_dark_uniqlo)
        crossfade()
    })

    AsyncImage(
        uri = imageUrl,
        modifier = modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
        state = isImageLoadedSuccessfully,
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

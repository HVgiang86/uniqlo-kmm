package com.gianghv.uniqlo.presentation.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.HomeToolBar
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.screen.home.list.ListItem
import com.gianghv.uniqlo.presentation.screen.home.list.ProductItem
import com.gianghv.uniqlo.presentation.screen.home.navigation.HomeDestination
import com.gianghv.uniqlo.presentation.screen.home.navigation.HomeNavigation
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.logging.AppLogger

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.state.asState()

    val uriHandler = LocalUriHandler.current
    val searchSuggestion = mutableStateListOf<String>()
    val toasterState = rememberToasterState()
    val scope = rememberCoroutineScope()
    val productDetailId = remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        viewModel.sendEvent(HomeUiEvent.LoadAllProduct)
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        AppErrorDialog(state.error?.throwable, onDismissRequest = {

        })
    }

    val productId = productDetailId.value
    if (productId != null) {
        HomeNavigation(HomeDestination.ProductDetail(mapOf(HomeDestination.ProductDetail.PRODUCT_ID_KEY to productId)))
    }

    Scaffold(topBar = {
        HomeToolBar(onSearch = {
            AppLogger.d("Search $it")
        }, onMenuClick = {
            toasterState.show(message = "Menu Clicked", type = ToastType.Info)
        }, onCartClick = {
            toasterState.show(message = "Cart Clicked", type = ToastType.Info)
        }, onSearchChange = {
            searchSuggestion.clear()
            searchSuggestion.addAll(state.productList.filter { text -> text.name?.contains(it, ignoreCase = true) == true }.map { it.name ?: "" })
        }, searchSuggestion = searchSuggestion.toList())
    }) {
        val allProducts = state.productList
        val recommendProducts = state.productList.take(3)

        var boxWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Toaster(state = toasterState)
        Column(modifier = Modifier.fillMaxSize().onGloballyPositioned { layoutCoordinates ->
            val widthInPx = layoutCoordinates.size.width
            boxWidth = with(density) { widthInPx.toDp() }
        }.padding(top = it.calculateTopPadding())) {

//            HeaderRow("For you!", onShowMoreClicked = {
//
//            })
//
//            RecommendProductList(modifier = Modifier.fillMaxWidth(), productList = recommendProducts, onClick = {
//                onProductClick(it, toasterState)
//            }, onFavoriteClick = { selectedProduct, isFavorite ->
//                onFavoriteClick(selectedProduct, isFavorite, toasterState)
//            })

            HeaderRow("Popular Products", onShowMoreClicked = {

            })

            AllProductList(boxWidth = boxWidth, modifier = Modifier.fillMaxWidth(), productList = allProducts, onClick = {
                productDetailId.value = it.id
            }, onFavoriteClick = { selectedProduct, isFavorite ->
                onFavoriteClick(selectedProduct, isFavorite, toasterState)
            })
        }
    }
}

fun onFavoriteClick(product: Product, isFavorite: Boolean, toasterState: ToasterState) {
    toasterState.show(message = "${product.name} favorite clicked", type = ToastType.Info)
}

fun calculateItemList(recommendProduct: List<Product>, allProducts: List<Product>): List<ListItem> {
    val list = mutableListOf<ListItem>()
    list.add(ListItem.Header("For You!"))
    list.add(ListItem.RecommendProducts(recommendProduct))
    list.add(ListItem.Header("Popular Products"))
    list.add(ListItem.AllProducts(allProducts))
    return list
}

@Composable
fun RecommendProductList(
    modifier: Modifier = Modifier, productList: List<Product>, onClick: (Product) -> Unit, onFavoriteClick: (Product, isFavorite: Boolean) -> Unit
) {
    if (productList.isNotEmpty()) {
        LazyRow(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            items(productList) { product ->
                ProductItem(
                    modifier = modifier.padding(8.dp).fillMaxWidth(0.45f).wrapContentHeight(),
                    product = product,
                    onClick = onClick,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }
}

@Composable
fun AllProductList(
    boxWidth: Dp, modifier: Modifier = Modifier, productList: List<Product>, onClick: (Product) -> Unit, onFavoriteClick: (Product, isFavorite: Boolean) -> Unit
) {
    if (productList.isNotEmpty()) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(8.dp), columns = GridCells.Fixed(2), modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            items(productList) { product ->
                ProductItem(modifier = modifier.padding(8.dp).width(boxWidth * 0.45f).wrapContentHeight(), product = product, onClick = {
                    onClick(product)
                }, onFavoriteClick = { selectedProduct, isFavorite ->
                    onFavoriteClick(selectedProduct, isFavorite)
                })
            }
        }
    }
}

@Composable
fun HeaderRow(title: String, onShowMoreClicked: () -> Unit = {}) {
    Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp).fillMaxWidth().wrapContentHeight()) {
        Text(modifier = Modifier.align(Alignment.CenterStart), text = title, style = MaterialTheme.typography.titleMedium, color = Color.Black)
        Text(modifier = Modifier.align(Alignment.CenterEnd).clickable {
            onShowMoreClicked()
        }, text = "See More", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
    }
}

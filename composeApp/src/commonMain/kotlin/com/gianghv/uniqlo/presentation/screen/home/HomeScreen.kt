package com.gianghv.uniqlo.presentation.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.rememberToasterState
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.HomeToolBar
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.screen.home.list.ProductItem
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.presentation.screen.searchresult.SearchResultScreenType
import com.gianghv.uniqlo.util.asState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, navigateTo: (MainScreenDestination) -> Unit) {
    val state by viewModel.state.asState()

    val searchSuggestion = mutableStateListOf<Product>()
    val toasterState = rememberToasterState()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        viewModel.sendEvent(HomeUiEvent.LoadAllProduct)
        viewModel.sendEvent(HomeUiEvent.LoadRecommendProduct)
        viewModel.sendEvent(HomeUiEvent.LoadCartCount)
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        AppErrorDialog(state.error?.throwable, onDismissRequest = { })
    }

    Scaffold(topBar = {
        HomeToolBar(cartCount = state.cartCount, onSearch = {
            navigateToSearchResult(navigateTo, it)
        }, onMenuClick = {
            toasterState.show(message = "Menu Clicked", type = ToastType.Info)
        }, onCartClick = {
            navigateTo(MainScreenDestination.Cart)
        }, onSearchChange = {
            searchSuggestion.clear()
            searchSuggestion.addAll(state.productList.filter { text -> text.name?.contains(it, ignoreCase = true) == true })
        }, searchSuggestion = searchSuggestion.toList())
    }) { it ->
        val allProducts = state.productList.take(12)

        var boxWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Toaster(state = toasterState)

        PullToRefreshBox(modifier = Modifier.fillMaxSize(), state = pullToRefreshState, isRefreshing = false, onRefresh = {
            viewModel.sendEvent(HomeUiEvent.Refresh)
        }) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize().onGloballyPositioned { layoutCoordinates ->
                    val widthInPx = layoutCoordinates.size.width
                    boxWidth = with(density) { widthInPx.toDp() }
                }.padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding()),
                contentPadding = PaddingValues(8.dp), columns = GridCells.Fixed(2),
            ) {

                val recommendProducts = state.recommendProducts

                if (recommendProducts.isNotEmpty()) {
                    header {
                        HeaderRow("For you!", onShowMoreClicked = {
                            navigateToSeeMoreRecommendProducts(navigateTo)
                        })
                    }

                    header {
                        RecommendProductList(boxWidth = boxWidth, modifier = Modifier.fillMaxWidth(), productList = state.recommendProducts, onClick = {
                            navigateToProductDetail(navigateTo, it.id)
                        }, onFavoriteClick = { selectedProduct, isFavorite ->
                            viewModel.setFavorite(product = selectedProduct, isFavorite = isFavorite)
                        })
                    }
                }

                if (allProducts.isNotEmpty()) {
                    header {
                        HeaderRow("Popular Products", onShowMoreClicked = {
                            navigateToSeeMorePopularProducts(navigateTo)
                        })
                    }

                    items(allProducts) { product ->
                        ProductItem(modifier = Modifier.padding(8.dp).width(boxWidth * 0.45f).wrapContentHeight(), product = product, onClick = {
                            navigateToProductDetail(navigateTo, it.id)
                        }, onFavoriteClick = { prd, isFavorite ->
                            viewModel.setFavorite(product = prd, isFavorite = isFavorite)
                        })
                    }
                }

                if (recommendProducts.isEmpty() && allProducts.isEmpty()) {
                    header {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                "No products found!",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

fun navigateToProductDetail(navigateTo: (MainScreenDestination) -> Unit, productId: Long) =
    navigateTo(MainScreenDestination.ProductDetail(mapOf(MainScreenDestination.ProductDetail.PRODUCT_ID_KEY to productId)))

fun navigateToSearchResult(navigateTo: (MainScreenDestination) -> Unit, searchQuery: String) =
    navigateTo(MainScreenDestination.SearchResult(mapOf(MainScreenDestination.SearchResult.PRODUCT_SEARCH_QUERY_KEY to searchQuery)))

fun navigateToSeeMorePopularProducts(navigateTo: (MainScreenDestination) -> Unit) = navigateTo(
    MainScreenDestination.SearchResult(
        mapOf(
            MainScreenDestination.SearchResult.SEARCH_RESULT_SCREEN_TYPE to SearchResultScreenType.ALL
        )
    )
)

fun navigateToSeeMoreRecommendProducts(navigateTo: (MainScreenDestination) -> Unit) = navigateTo(
    MainScreenDestination.SearchResult(
        mapOf(
            MainScreenDestination.SearchResult.SEARCH_RESULT_SCREEN_TYPE to SearchResultScreenType.RECOMMEND
        )
    )
)

@Composable
fun RecommendProductList(
    boxWidth: Dp, modifier: Modifier = Modifier, productList: List<Product>, onClick: (Product) -> Unit, onFavoriteClick: (Product, isFavorite: Boolean) -> Unit
) {
    if (productList.isNotEmpty()) {
        LazyRow(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            items(productList) { product ->
                ProductItem(
                    modifier = modifier.padding(8.dp).width(boxWidth * 0.45f).wrapContentHeight(),
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
    } else {
        EmptyScreen()
    }
}

@Composable
fun EmptyScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Empty", modifier = Modifier.align(Alignment.Center), textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun HeaderRow(title: String, onShowMoreClicked: () -> Unit = {}) {
    Box(modifier = Modifier.padding(start = 8.dp, end = 8.dp).fillMaxWidth().wrapContentHeight()) {
        Text(modifier = Modifier.align(Alignment.CenterStart), text = title, style = MaterialTheme.typography.titleMedium, color = Color.Black)
        Text(modifier = Modifier.align(Alignment.CenterEnd).clickable {
            onShowMoreClicked()
        }, text = "See More", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
    }
}

package com.gianghv.uniqlo.presentation.screen.searchresult

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.rememberToasterState
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.component.SearchToolBar
import com.gianghv.uniqlo.presentation.screen.home.AllProductList
import com.gianghv.uniqlo.presentation.screen.home.onFavoriteClick
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.presentation.screen.productdetail.VariationSize
import com.gianghv.uniqlo.util.asState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchResultScreen(viewModel: SearchResultViewModel, searchQuery: String? = null, onBack: () -> Unit, navigateTo: (MainScreenDestination) -> Unit) {
    val state by viewModel.state.asState()
    val toasterState = rememberToasterState()
    val searchSuggestion = mutableStateListOf<Product>()
    val productDetailId = remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        val toSearch = searchQuery ?: ""
        viewModel.sendEvent(SearchResultUiEvent.SearchForProduct(toSearch, state.filterState))
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        AppErrorDialog(state.error?.throwable, onDismissRequest = {})
    }

    val productId = productDetailId.value
    if (productId != null) {
        navigateTo(MainScreenDestination.ProductDetail(mapOf(MainScreenDestination.ProductDetail.PRODUCT_ID_KEY to productId)))
        productDetailId.value = null
    }

    Scaffold(topBar = {
        SearchToolBar(initText = searchQuery ?: "", onSearch = {
            viewModel.sendEvent(SearchResultUiEvent.SearchForProduct(it, state.filterState))
        }, onMenuClick = {
            toasterState.show(message = "Menu Clicked", type = ToastType.Info)
        }, onCartClick = {
            toasterState.show(message = "Cart Clicked", type = ToastType.Info)
        }, onSearchChange = {
            searchSuggestion.clear()
            searchSuggestion.addAll(state.productList.filter { text -> text.name?.contains(it, ignoreCase = true) == true })
        }, searchSuggestion = searchSuggestion.toList(), onNavigationClick = onBack)
    }) {
        val allProducts = state.productList

        var boxWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Toaster(state = toasterState)
        Column(modifier = Modifier.fillMaxSize().onGloballyPositioned { layoutCoordinates ->
            val widthInPx = layoutCoordinates.size.width
            boxWidth = with(density) { widthInPx.toDp() }
        }.padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())) {

            FlowRow(modifier = Modifier.padding(16.dp)) {
                Text("Filter", style = MaterialTheme.typography.titleSmall, color = Color.Black, modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(8.dp))
                FilterSize(state.filterState.sizeFilter?.size,
                    sizeList = listOf(VariationSize.S, VariationSize.M, VariationSize.L, VariationSize.XL),
                    onSizeSelected = { sizeSelected ->
                        val sizeFilter = if (sizeSelected != null) FilterType.FilterSize(sizeSelected) else null
                        viewModel.sendEvent(SearchResultUiEvent.ApplyFilter(state.filterState.copy(sizeFilter = sizeFilter)))
                    })

                Spacer(modifier = Modifier.width(8.dp))
                FilterColor(state.filterState.colorFilter?.color, colorList = state.filterState.allColorList, onColorSelected = { colorSelected ->
                    val colorFilter = if (colorSelected != null) FilterType.FilterColor(colorSelected) else null
                    viewModel.sendEvent(SearchResultUiEvent.ApplyFilter(state.filterState.copy(colorFilter = colorFilter)))
                })

                Spacer(modifier = Modifier.width(8.dp))
                FilterPrice(state.filterState.priceFilter?.isDesc, isAscSelected = {
                    val priceFilter = FilterType.FilterPrice(false)
                    viewModel.sendEvent(SearchResultUiEvent.ApplyFilter(state.filterState.copy(priceFilter = priceFilter)))
                }, isDescSelected = {
                    val priceFilter = FilterType.FilterPrice(true)
                    viewModel.sendEvent(SearchResultUiEvent.ApplyFilter(state.filterState.copy(priceFilter = priceFilter)))
                })
            }

            AllProductList(boxWidth = boxWidth, modifier = Modifier.fillMaxWidth(), productList = allProducts, onClick = {
                productDetailId.value = it.id
            }, onFavoriteClick = { selectedProduct, isFavorite ->
                onFavoriteClick(selectedProduct, isFavorite, toasterState)
            })
        }
    }
}


package com.gianghv.uniqlo.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.rememberToasterState
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.HomeToolBar
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.screen.home.list.ProductItem
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.logging.AppLogger

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.state.asState()

    val uriHandler = LocalUriHandler.current
    val searchSuggestion = mutableStateListOf<String>()
    val toasterState = rememberToasterState()
    val scope = rememberCoroutineScope()

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
        Toaster(state = toasterState)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (state.productList.isNotEmpty())
                ProductItem(modifier = Modifier.width(200.dp).height(200.dp), product = state.productList[0], onClick = {}, onFavoriteClick = { product, isFavorite -> })
        }
    }
}

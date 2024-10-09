package com.gianghv.uniqlo.presentation.screen.productdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.getScreenHeightInDp
import com.gianghv.uniqlo.util.logging.AppLogger
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.rememberAsyncImageState
import com.github.panpf.sketch.request.ComposableImageOptions
import com.github.panpf.sketch.request.placeholder
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_dark_uniqlo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(viewModel: ProductDetailViewModel, productId: Long?, onBack: () -> Unit) {
    val state by viewModel.state.asState()

    LaunchedEffect(Unit) {
        if (productId == null) onBack()
        else viewModel.sendEvent(ProductDetailUiEvent.LoadProduct(productId))
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        AppErrorDialog(state.error?.throwable, onDismissRequest = {})
    }

    if (state.product != null) {
        AppLogger.d("ProductDetailScreen: ${state.product}")
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent), title = {}, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            }, actions = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
                }
            })
        }) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                val screenHeight = getScreenHeightInDp().dp

                val scrollState = rememberScrollState()
                Column(modifier = Modifier.fillMaxSize().background(Color.White).verticalScroll(scrollState)) {
                    ProductImagePanel(modifier = Modifier.fillMaxWidth().height(screenHeight * 0.6f), imageUrl = state.product?.defaultImage)
                    ProductInfoPanel(modifier = Modifier.fillMaxWidth().wrapContentHeight())
                }

                AddToCartPanel(modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth().fillMaxHeight(0.1f).align(Alignment.BottomCenter))
            }
        }
    }
}

@Composable
fun ProductImagePanel(modifier: Modifier = Modifier, imageUrl: String? = null) {
    Box(modifier = modifier) {
        var isImageLoadedSuccessfully = rememberAsyncImageState(ComposableImageOptions {
            placeholder(Res.drawable.ic_dark_uniqlo)
            crossfade()
        })

        AsyncImage(
            uri = imageUrl ?: "",
            modifier = modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
            state = isImageLoadedSuccessfully,
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
    }
}

@Composable
fun ProductInfoPanel(modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = Color.Yellow) {
        Box(modifier = Modifier.height(1200.dp)) {

        }
    }
}


@Composable
fun AddToCartPanel(modifier: Modifier) {
    Surface(modifier = modifier, color = Color.Green) {

    }
}

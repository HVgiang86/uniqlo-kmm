package com.gianghv.uniqlo.presentation.screen.productdetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.domain.Brand
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.rememberAsyncImageState
import com.github.panpf.sketch.request.ComposableImageOptions
import com.github.panpf.sketch.request.placeholder
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_dark_uniqlo

@Composable
fun BrandBar(modifier: Modifier = Modifier, onClick: () -> Unit, brand: Brand) {

    Row(modifier = modifier.wrapContentHeight().clickable {
        onClick()
    }) {
        BrandBarLogo(modifier = Modifier.size(40.dp).align(Alignment.Top), imageUrl = brand.logo)

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.wrapContentHeight()) {
            Text(text = brand.name ?: "Unnamed Brand", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
            Text(text = "Id #${brand.id}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@Composable
fun BrandBarLogo(modifier: Modifier = Modifier, imageUrl: String?) {
    val isImageLoadedSuccessfully = rememberAsyncImageState(ComposableImageOptions {
        placeholder(Res.drawable.ic_dark_uniqlo)
        crossfade()
    })

    Box(modifier = modifier.clip(CircleShape)) {
        AsyncImage(
            uri = imageUrl ?: "",
            modifier = Modifier.fillMaxSize(),
            state = isImageLoadedSuccessfully,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}

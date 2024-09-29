package com.gianghv.uniqlo.presentation.screen.home.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.gianghv.uniqlo.domain.Product

@Composable
fun ProductItem(modifier: Modifier = Modifier, product: Product, onClick: (Product) -> Unit, onFavoriteClick: (Product, isFavorite: Boolean) -> Unit) {
    Card(modifier = modifier, shape = RoundedCornerShape(8.dp)) {
        Column(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            Box(modifier = Modifier.size(145.dp, 145.dp).weight(1f)) {
                ProductImage(product = product)
            }
            ProductInfo(product = product, onClick = onClick, onFavoriteClick = onFavoriteClick)
        }
    }
}

@Composable
fun ProductImage(modifier: Modifier = Modifier, product: Product) {
    AsyncImage(
        model = product.defaultImage.toString(),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ProductInfo(modifier: Modifier = Modifier, product: Product, onClick: (Product) -> Unit, onFavoriteClick: (Product, isFavorite: Boolean) -> Unit) {
    Column (modifier = Modifier.padding(8.dp)) {
        Text(product.category?.name.toString(), style = MaterialTheme.typography.bodySmall)
        Text(product.name.toString(), style = MaterialTheme.typography.bodyMedium)
        Text(product.price.toString(), style = MaterialTheme.typography.bodySmall)
    }
}

package com.gianghv.uniqlo.presentation.screen.home.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.theme.Card_Border_Gray
import com.gianghv.uniqlo.util.ext.round
import com.gianghv.uniqlo.util.ext.toCurrencyText
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.rememberAsyncImageState
import com.github.panpf.sketch.request.ComposableImageOptions
import com.github.panpf.sketch.request.placeholder
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_dark_uniqlo

@Composable
fun ProductItem(modifier: Modifier = Modifier, product: Product, onClick: (Product) -> Unit, onFavoriteClick: (Product, isFavorite: Boolean) -> Unit) {
    val isFavorite = false

    Card(
        modifier = modifier.clickable {
            onClick(product)
        },
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(width = 1.dp, color = Card_Border_Gray),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp, pressedElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {


            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                    ProductImage(product = product)
                }
                ProductInfo(product = product)
            }

            IconButton(onClick = {
                onFavoriteClick(product, !isFavorite)
            }, modifier = Modifier.align(Alignment.TopEnd)) {
                if (isFavorite) Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.Red)
                else Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White)
            }
        }
    }
}

@Composable
fun ProductImage(modifier: Modifier = Modifier, product: Product) {

    var isImageLoadedSuccessfully = rememberAsyncImageState(ComposableImageOptions {
        placeholder(Res.drawable.ic_dark_uniqlo)
        crossfade()
    })

    AsyncImage(
        uri = product.defaultImage.toString(),
        modifier = modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
        state = isImageLoadedSuccessfully,
        contentScale = ContentScale.Crop,
        contentDescription = null
    )

}

@Composable
fun ProductInfo(
    modifier: Modifier = Modifier.wrapContentHeight(), product: Product
) {
    Column(modifier = modifier.padding(start = 12.dp, top = 8.dp, end = 12.dp, bottom = 8.dp).wrapContentHeight()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = product.category?.name.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = Color.DarkGray,
            maxLines = 1,
            minLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = product.name.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        ProductPrice(product = product)

        val starts = (product.averageRating ?: 0.0).round(2)

        var ratingRowModifier = Modifier.fillMaxWidth().wrapContentHeight()

        if (starts <= 0) {
            ratingRowModifier =  ratingRowModifier.alpha(0f)
        }

        Row(modifier = ratingRowModifier) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(24.dp).align(Alignment.CenterVertically),
                tint = Color.Yellow
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(modifier = Modifier.align(Alignment.CenterVertically), text = "${product.averageRating}", style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
        }
    }
}

@Composable
fun ProductPrice(modifier: Modifier = Modifier, product: Product) {
    val originalPrice = (product.price ?: 0.0) * 1000
    val discountedPrice: Double = if (product.discountPercentage != null && product.price != null) {
        originalPrice - (originalPrice * (product.discountPercentage * 1.0 / 100))
    } else originalPrice

    val originalPriceStr = originalPrice.toCurrencyText()
    val discountedPriceStr = discountedPrice.toCurrencyText()

    if (originalPrice != discountedPrice) {
        Column(modifier = modifier.fillMaxWidth().wrapContentHeight()) {
            Text(
                originalPriceStr,
                style = MaterialTheme.typography.titleSmall,
                textDecoration = TextDecoration.LineThrough,
                color = Color.Black,
                maxLines = 1,
                minLines = 1,
                overflow = TextOverflow.Clip
            )

            Text(
                text = discountedPriceStr,
                style = MaterialTheme.typography.titleSmall,
                color = Color.Red,
                maxLines = 1,
                minLines = 1,
                overflow = TextOverflow.Clip
            )
        }
    } else {
        Column(modifier = modifier.fillMaxWidth().wrapContentHeight()) {
            Text(
                originalPriceStr, style = MaterialTheme.typography.titleSmall, color = Color.Black, maxLines = 1, minLines = 1, overflow = TextOverflow.Clip
            )

            Text(
                "", style = MaterialTheme.typography.titleSmall, color = Color.Red, maxLines = 1, minLines = 1, overflow = TextOverflow.Clip
            )
        }
    }
}

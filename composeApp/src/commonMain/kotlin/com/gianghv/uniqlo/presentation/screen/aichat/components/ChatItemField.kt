package com.gianghv.uniqlo.presentation.screen.aichat.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.domain.ChatMessage
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.presentation.screen.aichat.utils.parseProductItems
import com.gianghv.uniqlo.presentation.screen.aichat.utils.replaceProduct
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.rememberAsyncImageState
import com.github.panpf.sketch.request.ComposableImageOptions
import com.github.panpf.sketch.request.placeholder
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_dark_uniqlo

@Composable
fun ChatItem(
    chatMessage: ChatMessage, products: List<Product>? = null, boxWidth: Dp, onProductClick: (Long) -> Unit, onLoadEmbeddedProduct: (List<Long>) -> Unit
) {
    if (chatMessage.isUser) {
        UserChatItem(chatMessage, boxWidth = boxWidth)
    } else {
        BotChatItem(
            chatMessage, boxWidth = boxWidth, onProductClick = onProductClick, products = products ?: emptyList(), onLoadEmbeddedProduct = onLoadEmbeddedProduct
        )
    }
}

@Composable
fun UserChatItem(chatMessage: ChatMessage, modifier: Modifier = Modifier, boxWidth: Dp) {
    Box(modifier = modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp)) {
        ChatBubble(messageString = chatMessage.content, isUser = true, modifier = Modifier.align(Alignment.TopEnd).widthIn(max = boxWidth * 0.6f))
    }
}

@Composable
fun BotChatItem(
    chatMessage: ChatMessage,
    modifier: Modifier = Modifier,
    boxWidth: Dp,
    onProductClick: (Long) -> Unit,
    products: List<Product>,
    onLoadEmbeddedProduct: (List<Long>) -> Unit
) {
    val trimmed = chatMessage.content.trim()
    val productItems = trimmed.parseProductItems()
    val filtered = productItems.distinctBy {
        it.first
    }

    val message = trimmed.replaceProduct(filtered)

    onLoadEmbeddedProduct(filtered.map { it.first })

    Column(modifier = modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp)) {
        ChatBubble(message = message, isUser = false, modifier = Modifier.align(Alignment.Start).widthIn(max = boxWidth * 0.7f))
        ProductShowcase(
            modifier = Modifier.widthIn(max = boxWidth * 0.7f).fillMaxWidth(), products = products, onProductClick = onProductClick, boxWidth = boxWidth * 0.7f
        )
    }
}

@Composable
fun ChatBubble(modifier: Modifier = Modifier, message: AnnotatedString? = null, messageString: String? = null, isUser: Boolean = false) {
    Box(modifier = modifier.wrapContentWidth().wrapContentHeight().clip(RoundedCornerShape(16.dp)).background(Color.LightGray, RoundedCornerShape(16.dp))) {
        if (message != null) {
            Text(text = message, color = Color.Black, textAlign = TextAlign.Justify, modifier = Modifier.padding(start = 8.dp))
        } else {
            Text(text = messageString ?: "", color = Color.Black, textAlign = TextAlign.Justify, modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun ProductShowcase(products: List<Product>, modifier: Modifier = Modifier, onProductClick: (Long) -> Unit, boxWidth: Dp) {
    Box(modifier = modifier.padding(vertical = 8.dp).wrapContentHeight().fillMaxWidth()) {
        LazyRow(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
            items(products) { product ->
                ProductItem(product, onProductClick = onProductClick, modifier = Modifier.widthIn(max = boxWidth - 16.dp).fillMaxWidth().wrapContentHeight())
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, modifier: Modifier = Modifier, onProductClick: (Long) -> Unit) {
    Card(modifier = modifier.padding(horizontal = 4.dp).clickable {
        onProductClick(product.id)
    }, shape = RoundedCornerShape(10.dp), border = BorderStroke(1.dp, Color.LightGray)) {
        Row {
            Box(modifier = Modifier.size(60.dp)) {
                val isImageLoadedSuccessfully = rememberAsyncImageState(ComposableImageOptions {
                    placeholder(Res.drawable.ic_dark_uniqlo)
                    crossfade()
                })

                AsyncImage(
                    uri = product.defaultImage ?: "",
                    modifier = modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
                    state = isImageLoadedSuccessfully,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }

            Text(product.name ?: "", maxLines = 3, modifier = Modifier.padding(4.dp), overflow = TextOverflow.Ellipsis)
        }
    }
}

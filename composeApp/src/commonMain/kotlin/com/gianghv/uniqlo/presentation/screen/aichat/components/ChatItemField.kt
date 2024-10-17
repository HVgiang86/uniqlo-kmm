package com.gianghv.uniqlo.presentation.screen.aichat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.domain.ChatMessage
import com.gianghv.uniqlo.presentation.screen.aichat.utils.parseProductItems
import com.gianghv.uniqlo.presentation.screen.aichat.utils.replaceProduct

@Composable
fun ChatItem(chatMessage: ChatMessage, boxWidth: Dp) {
    if (chatMessage.isUser) {
        UserChatItem(chatMessage, boxWidth = boxWidth)
    } else {
        BotChatItem(chatMessage, boxWidth = boxWidth)
    }
}

@Composable
fun UserChatItem(chatMessage: ChatMessage, modifier: Modifier = Modifier, boxWidth: Dp) {
    Box(modifier = modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp)) {
        ChatBubble(messageString = chatMessage.content, isUser = true, modifier = Modifier.align(Alignment.TopEnd).widthIn(max = boxWidth * 0.6f))
    }
}

@Composable
fun BotChatItem(chatMessage: ChatMessage, modifier: Modifier = Modifier, boxWidth: Dp) {
    val trimmed = chatMessage.content.trim()
    val productItems = trimmed.parseProductItems()
    val filtered = productItems.distinctBy {
        it.first
    }

    val message = trimmed.replaceProduct(filtered)

    Box(modifier = modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp)) {
        ChatBubble(message = message, isUser = false, modifier = Modifier.align(Alignment.TopStart).widthIn(max = boxWidth * 0.7f))
    }
}

@Composable
fun ChatBubble(modifier: Modifier = Modifier, message: AnnotatedString? = null, messageString: String? = null, isUser: Boolean = false) {
    Box(modifier = modifier.wrapContentWidth().wrapContentHeight().clip(RoundedCornerShape(16.dp)).background(Color.LightGray, RoundedCornerShape(16.dp))) {
        if (message != null) {
            Text(text = message, color = Color.Black, textAlign = TextAlign.Justify, modifier = Modifier.padding(8.dp))
        } else {
            Text(text = messageString ?: "", color = Color.Black, textAlign = TextAlign.Justify, modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun ProductShowcase(products: List<Pair<Long, String>>, modifier: Modifier = Modifier, onProductClick: (Long) -> Unit) {
    Box(modifier = modifier.padding(8.dp).wrapContentHeight()) {
        LazyRow (modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
            items() {

            }
        }
    }
}

@Composable
fun ProductItem(product: Pair<Long, String>, modifier: Modifier = Modifier, onProductClick: (Long) -> Unit) {
    Card (modifier = modifier) {

    }
}

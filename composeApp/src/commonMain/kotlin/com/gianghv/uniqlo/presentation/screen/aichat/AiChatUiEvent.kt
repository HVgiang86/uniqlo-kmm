package com.gianghv.uniqlo.presentation.screen.aichat

import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.ChatMessage
import com.gianghv.uniqlo.domain.Product

sealed class AiChatUiEvent : Reducer.UiEvent{
    data class Error(val error: Throwable) : AiChatUiEvent()
    data object LoadChatMessages : AiChatUiEvent()
    data class LoadEmbeddedProduct(val messageId: Long, val id: Long): AiChatUiEvent()
    data class LoadEmbeddedProductSuccess(val messageId: Long, val id: Long, val product: Product): AiChatUiEvent()
    data object RandomChatSuggestions : AiChatUiEvent()
    data class ShowChatSuggestions(val suggestions: List<String>) : AiChatUiEvent()
    data object HideChatSuggestions : AiChatUiEvent()
    data class LoadChatMessagesSuccess(val chatMessages: List<ChatMessage>) : AiChatUiEvent()
    data object LoadChatMessagesError : AiChatUiEvent()
    data class SendMessage(val message: String) : AiChatUiEvent()
    data class MessageSent(val chatMessage: ChatMessage) : AiChatUiEvent()
    data class MessageReceived(val chatMessage: ChatMessage) : AiChatUiEvent()
}

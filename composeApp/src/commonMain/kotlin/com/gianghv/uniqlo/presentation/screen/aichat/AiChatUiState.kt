package com.gianghv.uniqlo.presentation.screen.aichat

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.ChatMessage


@Immutable
data class AiChatUiState(
    override val isLoading: Boolean,
    override val error: ErrorState?,
    val chatMessages: List<ChatMessage> = emptyList(),
    val chatSuggestions: List<String> = emptyList(),
    val isServerTyping: Boolean = false
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = AiChatUiState(
            isLoading = false, error = null,
        )
    }
}

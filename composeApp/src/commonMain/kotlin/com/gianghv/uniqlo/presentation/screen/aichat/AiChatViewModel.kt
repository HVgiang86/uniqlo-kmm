package com.gianghv.uniqlo.presentation.screen.aichat

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.ChatRepository
import com.gianghv.uniqlo.data.WholeApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AiChatViewModel(private val chatRepository: ChatRepository) : BaseViewModel<AiChatUiState, AiChatUiEvent>() {
    override val state: StateFlow<AiChatUiState>
        get() = reducer.state
    override val reducer: Reducer<AiChatUiState, AiChatUiEvent>
        get() = ChatReducer.getInstance(AiChatUiState.initial(), this)

    override fun onClearLoadingState() {
    }

    override fun onClearErrorState() {
    }

    override val onException: (Throwable) -> Unit
        get() = {
            reducer.sendEvent(AiChatUiEvent.Error(it))
        }

    fun loadChatMessages() {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            chatRepository.getAllMessages(WholeApp.USER_ID).collect {
                reducer.sendEvent(AiChatUiEvent.LoadChatMessagesSuccess(it))
            }
        }
    }
}

class ChatReducer(initialVal: AiChatUiState, private val viewModel: AiChatViewModel) : Reducer<AiChatUiState, AiChatUiEvent>(initialVal) {
    override fun reduce(oldState: AiChatUiState, event: AiChatUiEvent) {
        when (event) {
            is AiChatUiEvent.Error -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error, true)))
            }

            AiChatUiEvent.HideChatSuggestions -> {}
            AiChatUiEvent.LoadChatMessages -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.loadChatMessages()
            }

            is AiChatUiEvent.LoadChatMessagesSuccess -> {
                setState(oldState.copy(isLoading = false, chatMessages = event.chatMessages))
            }

            is AiChatUiEvent.MessageReceived -> {}
            is AiChatUiEvent.MessageSent -> {}
            AiChatUiEvent.RandomChatSuggestions -> {}
            is AiChatUiEvent.SendMessage -> {

            }

            is AiChatUiEvent.ShowChatSuggestions -> {}
        }
    }

    companion object {
        private var INSTANCE: ChatReducer? = null
        fun getInstance(initialVal: AiChatUiState, viewModel: AiChatViewModel): ChatReducer {
            if (INSTANCE == null) {
                INSTANCE = ChatReducer(initialVal, viewModel)
            }
            return INSTANCE!!
        }
    }
}

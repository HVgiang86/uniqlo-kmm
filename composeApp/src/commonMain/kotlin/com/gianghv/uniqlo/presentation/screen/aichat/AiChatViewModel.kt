package com.gianghv.uniqlo.presentation.screen.aichat

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.ChatRepository
import com.gianghv.uniqlo.data.ProductRepository
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.domain.ChatMessage
import com.gianghv.uniqlo.util.logging.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AiChatViewModel(private val chatRepository: ChatRepository, private val productRepository: ProductRepository) :
    BaseViewModel<AiChatUiState, AiChatUiEvent>() {
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

    fun loadEmbeddedProduct(id: Long, messageId: Long) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            productRepository.getProductDetail(id).collect {
                reducer.sendEvent(AiChatUiEvent.LoadEmbeddedProductSuccess(messageId, id, it))
            }
        }
    }

    fun sendMessage(message: String) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            reducer.sendEvent(AiChatUiEvent.MessageSent(ChatMessage(-1, message, "", true, sessionNumber = -1, null)))
            chatRepository.sendMessage(WholeApp.USER_ID, message).collect{
                reducer.sendEvent(AiChatUiEvent.MessageReceived(it))
            }
        }
    }
}

class ChatReducer(initialVal: AiChatUiState, private val viewModel: AiChatViewModel) : Reducer<AiChatUiState, AiChatUiEvent>(initialVal) {
    override fun reduce(oldState: AiChatUiState, event: AiChatUiEvent) {
        when (event) {
            is AiChatUiEvent.Error -> {
                setState(oldState.copy(isLoading = false, isServerTyping = false, error = ErrorState(event.error, true)))
            }

            AiChatUiEvent.HideChatSuggestions -> {}
            AiChatUiEvent.LoadChatMessages -> {
                setState(oldState.copy(isLoading = true, error = null, isServerTyping = false))
                viewModel.loadChatMessages()
            }

            is AiChatUiEvent.LoadChatMessagesSuccess -> {
                setState(oldState.copy(isLoading = false, isServerTyping = false, chatMessages = event.chatMessages))
            }

            is AiChatUiEvent.MessageReceived -> {
                val newMessages = oldState.chatMessages.toMutableList()
                newMessages.add(event.chatMessage)
                setState(oldState.copy(chatMessages = newMessages, isServerTyping = false))
            }
            is AiChatUiEvent.MessageSent -> {
                val newMessages = oldState.chatMessages.toMutableList()
                newMessages.add(event.chatMessage)
                setState(oldState.copy(chatMessages = newMessages, isServerTyping = true))
            }
            AiChatUiEvent.RandomChatSuggestions -> {}
            is AiChatUiEvent.SendMessage -> {
                viewModel.sendMessage(event.message)
            }

            is AiChatUiEvent.ShowChatSuggestions -> {}
            is AiChatUiEvent.LoadEmbeddedProduct -> {
                viewModel.loadEmbeddedProduct(id = event.id, messageId = event.messageId)
            }

            is AiChatUiEvent.LoadEmbeddedProductSuccess -> {
                val updatedMessages = oldState.chatMessages.map { message ->
                    if (message.id == event.messageId) {
                        val tempList = message.products?.toMutableList() ?: mutableListOf()
                        val updatedProducts = tempList.apply {
                            if (none { it.id == event.id }) {
                                add(event.product)
                            }
                        }
                        message.copy(products = updatedProducts)
                    } else {
                        message
                    }
                }
                val newState = oldState.copy(chatMessages = updatedMessages)
                setState(newState)
            }
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

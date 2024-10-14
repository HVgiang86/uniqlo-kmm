package com.gianghv.uniqlo.presentation.screen.cart

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.CartRepository
import com.gianghv.uniqlo.data.UserRepository
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.domain.CartItem
import com.gianghv.uniqlo.util.logging.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository, private val userRepository: UserRepository) : BaseViewModel<CartUiState, CartUiEvent>() {
    override val state: StateFlow<CartUiState>
        get() = reducer.state
    override val reducer: Reducer<CartUiState, CartUiEvent>
        get() = CartReducer.getInstance(CartUiState.initial(), this)

    override fun onClearLoadingState() {
    }

    override fun onClearErrorState() {
    }

    override val onException: (Throwable) -> Unit
        get() = {
            reducer.sendEvent(CartUiEvent.Error(it))
        }

    fun loadOrder() {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            cartRepository.getCartItems(WholeApp.USER_ID).collect {
                reducer.sendEvent(CartUiEvent.LoadOrderSuccess(it))
            }
        }
    }

    fun selectItem(cartItem: CartItem, isSelected: Boolean) {
        if (isSelected) reducer.setState(state.value.copy(selectedItems = state.value.selectedItems + cartItem))
        else reducer.setState(state.value.copy(selectedItems = state.value.selectedItems - cartItem))
    }

    fun selectAll() {
        reducer.setState(state.value.copy(selectedItems = state.value.cartItems))
    }

    fun unSelectAll() {
        reducer.setState(state.value.copy(selectedItems = emptyList()))
    }

    fun updateQuantity(cartItem: CartItem, quantity: Int) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            cartRepository.updateQuantity(cartItem.id, quantity).collect {
                val newCartItem = cartItem.copy(quantity = it.quantity)
                reducer.sendEvent(CartUiEvent.UpdateQuantitySuccess(newCartItem))
            }
        }
    }

    fun deleteItem(cartItem: CartItem) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            AppLogger.d("delete item ${cartItem.id}")
            cartRepository.deleteCartItem(cartItem.id).collect {
                if (it) reducer.sendEvent(CartUiEvent.DeleteItemSuccess(cartItem))
            }
        }
    }

    fun deleteAll() {

    }
}


class CartReducer(initialVal: CartUiState, private val viewModel: CartViewModel) : Reducer<CartUiState, CartUiEvent>(initialVal) {
    override fun reduce(oldState: CartUiState, event: CartUiEvent) {
        when (event) {
            is CartUiEvent.Error -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error, true)))
            }

            CartUiEvent.LoadOrder -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.loadOrder()
            }

            is CartUiEvent.LoadOrderSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, cartItems = event.cartItems))
            }

            is CartUiEvent.SelectItem -> {
                viewModel.selectItem(event.cartItem, true)
            }

            is CartUiEvent.UnSelectItem -> {
                viewModel.selectItem(event.cartItem, false)
            }

            CartUiEvent.SelectAll -> {
                viewModel.selectAll()
            }

            CartUiEvent.UnSelectAll -> {
                viewModel.unSelectAll()
            }

            is CartUiEvent.UpdateQuantity -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.updateQuantity(event.cartItem, event.quantity)
            }

            is CartUiEvent.DeleteAll -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.deleteAll()
            }

            is CartUiEvent.DeleteAllSuccess -> {
                setState(oldState.copy(isLoading = false, cartItems = emptyList(), selectedItems = emptyList()))
            }

            is CartUiEvent.DeleteItem -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.deleteItem(event.cartItem)
            }

            is CartUiEvent.DeleteItemSuccess -> {
                setState(
                    oldState.copy(
                        isLoading = false, cartItems = oldState.cartItems - event.cartItem, selectedItems = oldState.selectedItems - event.cartItem
                    )
                )
            }

            is CartUiEvent.UpdateQuantitySuccess -> {
                setState(oldState.copy(isLoading = false, cartItems = oldState.cartItems.map {
                    if (it.id == event.cartItem.id) event.cartItem else it
                }, selectedItems = oldState.selectedItems.map {
                    if (it.id == event.cartItem.id) event.cartItem else it
                }))
            }
        }
    }

    companion object {
        private var INSTANCE: CartReducer? = null
        fun getInstance(initialVal: CartUiState, viewModel: CartViewModel): CartReducer {
            return INSTANCE ?: CartReducer(initialVal, viewModel).also { INSTANCE = it }
        }
    }
}

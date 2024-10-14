package com.gianghv.uniqlo.presentation.screen.cart

import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.CartItem

sealed class CartUiEvent : Reducer.UiEvent {
    data object LoadOrder : CartUiEvent()
    data class LoadOrderSuccess(val cartItems: List<CartItem>) : CartUiEvent()
    data class Error(val error: Throwable) : CartUiEvent()
    data class SelectItem(val cartItem: CartItem) : CartUiEvent()
    data class UnSelectItem(val cartItem: CartItem) : CartUiEvent()
    data object SelectAll : CartUiEvent()
    data object UnSelectAll : CartUiEvent()
    data class UpdateQuantity(val cartItem: CartItem, val quantity: Int) : CartUiEvent()
    data class DeleteItem(val cartItem: CartItem) : CartUiEvent()
    data class DeleteAll(val cartItems: List<CartItem>) : CartUiEvent()
    data class UpdateQuantitySuccess(val cartItem: CartItem) : CartUiEvent()
    data class DeleteItemSuccess(val cartItem: CartItem) : CartUiEvent()
    data class DeleteAllSuccess(val cartItems: List<CartItem>) : CartUiEvent()
}

package com.gianghv.uniqlo.presentation.screen.order

import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.CartItem
import com.gianghv.uniqlo.domain.User

sealed class OrderUiEvent : Reducer.UiEvent {
    data class Error(val error: Throwable) : OrderUiEvent()
    data class LoadUserDetail(val userId: Long) : OrderUiEvent()
    data class LoadUserDetailSuccess(val user: User) : OrderUiEvent()
    data object LoadSavedOrderInfo : OrderUiEvent()
    data class LoadSavedOrderInfoSuccess(val address: String? = "", val phone: String? = "") : OrderUiEvent()
    data class SaveCartList(val carts: List<CartItem>) : OrderUiEvent()
}

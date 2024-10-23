package com.gianghv.uniqlo.presentation.screen.order

import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.CartItem
import com.gianghv.uniqlo.domain.User

sealed class OrderUiEvent : Reducer.UiEvent {
    data class Error(val error: Throwable) : OrderUiEvent()
    data class LoadUserDetail(val userId: Long) : OrderUiEvent()
    data class LoadUserDetailSuccess(val user: User) : OrderUiEvent()
    data object LoadSavedOrderInfo : OrderUiEvent()
    data class LoadSavedOrderInfoSuccess(
        val address: String? = "", val email: String? = "", val phone: String? = "", val paymentMethod: PaymentMethodBase? = PaymentMethod.Cash
    ) : OrderUiEvent()

    data class SaveCartList(val carts: List<CartItem>) : OrderUiEvent()
    data class ChangeOrderInfo(
        val address: String? = "", val email: String? = "", val phone: String? = "", val paymentMethod: PaymentMethodBase, val orderName: String
    ) : OrderUiEvent()

    data class SaveOrderInfo(
        val address: String? = "", val email: String? = "", val phone: String? = "", val paymentMethod: PaymentMethodBase? = PaymentMethod.Cash
    ) : OrderUiEvent()

    data object CreateOrder : OrderUiEvent()

    data class CreateOrderSuccess(val orderId: Long, val total: Double) : OrderUiEvent()

    data class CreateOrderFail(
        val error: Throwable
    ) : OrderUiEvent()
}

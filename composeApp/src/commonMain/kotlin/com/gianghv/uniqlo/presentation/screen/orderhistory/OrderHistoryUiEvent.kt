package com.gianghv.uniqlo.presentation.screen.orderhistory

import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.OrderHistory

sealed class OrderHistoryUiEvent : Reducer.UiEvent {
    data class Error(val error: Throwable) : OrderHistoryUiEvent()
    data object LoadOrderHistory : OrderHistoryUiEvent()
    data class LoadOrderHistorySuccess(val orderList: List<OrderHistory>) : OrderHistoryUiEvent()
    data object LoadCartCount : OrderHistoryUiEvent()
    data class LoadCartCountSuccess(val count: Int) : OrderHistoryUiEvent()
    data class CancelOrder(val orderId: Long) : OrderHistoryUiEvent()
    data class CancelOrderSuccess(val orderId: Long) : OrderHistoryUiEvent()
    data class CreateProductEvaluation(val productId: Long, val userId: Long, val star: Double, val content: String) : OrderHistoryUiEvent()
    data object CreateProductEvaluationSuccess : OrderHistoryUiEvent()
}

package com.gianghv.uniqlo.presentation.screen.orderhistory

import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.OrderHistory

sealed class OrderHistoryUiEvent : Reducer.UiEvent {
    data class Error(val error: Throwable) : OrderHistoryUiEvent()
    data object LoadOrderHistory : OrderHistoryUiEvent()
    data class LoadOrderHistorySuccess(val orderList: List<OrderHistory>) : OrderHistoryUiEvent()
}

package com.gianghv.uniqlo.presentation.screen.payment

import com.gianghv.uniqlo.base.Reducer

sealed class PaymentUiEvent : Reducer.UiEvent {
    data class Error(val error: Throwable) : PaymentUiEvent()
    data class LoadPaymentUrl(val amount: Long?, val orderId: Long?) : PaymentUiEvent()
    data class LoadPaymentUrlSuccess(val paymentUrl: String) : PaymentUiEvent()
    data class PayingSuccess(val paymentUrl: String, val orderId: Long) : PaymentUiEvent()
    data class PayingFail(val error: Throwable) : PaymentUiEvent()
    data class CloseWebView(val lastUrl: String) : PaymentUiEvent()
    data class HandleRedirectUrl(val url: String) : PaymentUiEvent()
    data class UpdateOrderStatus(val orderId: Long) : PaymentUiEvent()
    data object UpdateOrderStatusSuccess : PaymentUiEvent()
}

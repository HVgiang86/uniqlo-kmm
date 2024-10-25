package com.gianghv.uniqlo.presentation.screen.payment

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.CartRepository
import com.gianghv.uniqlo.util.logging.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(private val cartRepository: CartRepository) : BaseViewModel<PaymentUiState, PaymentUiEvent>() {
    override val state: StateFlow<PaymentUiState>
        get() = reducer.state
    override val reducer: Reducer<PaymentUiState, PaymentUiEvent>
        get() = PaymentReducer.getInstance(PaymentUiState.initial(), this)

    override fun onClearLoadingState() {
    }

    override fun onClearErrorState() {
    }

    override val onException: (Throwable) -> Unit
        get() = {
            reducer.sendEvent(PaymentUiEvent.Error(it))
        }

    fun getPaymentUrl(amount: Long) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            cartRepository.createVNPayLink(amount).collect {
                reducer.sendEvent(PaymentUiEvent.LoadPaymentUrlSuccess(it))
            }
        }
    }

    fun handleRedirectUrl(url: String) {
        if (url.isPaymentSuccess()) {
            reducer.sendEvent(PaymentUiEvent.PayingSuccess(url, 1))
        } else {
            if (url.contains("http://localhost:3003")) {
                reducer.sendEvent(PaymentUiEvent.PayingFail(NullPointerException("Payment fail")))
                return
            }

            if (url.isPaymentFail()) {
                AppLogger.d("Payment fail, url: $url")
                uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
                    delay(2000L) // Delay for 3 seconds
                    reducer.sendEvent(PaymentUiEvent.PayingFail(NullPointerException("Payment fail")))
                }
            }
        }
    }

    fun updateOrderStatus(orderId: Long) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            AppLogger.d("Update order status")
            cartRepository.updateOrderStatus(orderId, "accept").collect {
                reducer.sendEvent(PaymentUiEvent.UpdateOrderStatusSuccess)
            }
        }
    }
}

fun String.isPaymentSuccess(): Boolean {
    return this.contains("vnp_TransactionStatus=00")
}

fun String.isPaymentFail(): Boolean {
    return this.contains("vnp_TransactionStatus=02") || (this.contains("vnpay", ignoreCase = true) && this.contains("Error", ignoreCase = true))
}

class PaymentReducer(initialVal: PaymentUiState, private val viewModel: PaymentViewModel) : Reducer<PaymentUiState, PaymentUiEvent>(initialVal) {
    override fun reduce(oldState: PaymentUiState, event: PaymentUiEvent) {
        when (event) {
            is PaymentUiEvent.Error -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error, true)))
            }

            is PaymentUiEvent.PayingFail -> {
                setState(
                    oldState.copy(
                        isLoading = false, error = ErrorState(Exception("Payment fail"), true), paymentStatus = PaymentStatus.FAIL, isWebViewOpened = false
                    )
                )
            }

            is PaymentUiEvent.PayingSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, paymentStatus = PaymentStatus.PAID, isWebViewOpened = false))
            }

            is PaymentUiEvent.LoadPaymentUrl -> {
                if (event.amount == null || event.orderId == null) {
                    setState(
                        oldState.copy(isLoading = false, cannotCreatePayment = ErrorState(NullPointerException("Amount is null"), true))
                    )
                    return
                }

                setState(oldState.copy(isLoading = true, error = null, paymentUrl = null, isWebViewOpened = false, orderId = event.orderId))
                viewModel.getPaymentUrl(event.amount)
            }

            is PaymentUiEvent.LoadPaymentUrlSuccess -> {
                setState(
                    oldState.copy(
                        isLoading = false, error = null, paymentUrl = event.paymentUrl, isWebViewOpened = true, paymentStatus = PaymentStatus.NOT_YET
                    )
                )
            }

            is PaymentUiEvent.CloseWebView -> {
                if (event.lastUrl.isPaymentSuccess() || event.lastUrl.isPaymentFail()) {
                    viewModel.sendEvent(PaymentUiEvent.PayingSuccess(event.lastUrl, 1))
                } else {
                    viewModel.sendEvent(PaymentUiEvent.PayingFail(NullPointerException("Payment fail")))
                }
            }

            is PaymentUiEvent.HandleRedirectUrl -> {
                viewModel.handleRedirectUrl(event.url)
            }

            is PaymentUiEvent.UpdateOrderStatus -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.updateOrderStatus(event.orderId)
            }

            PaymentUiEvent.UpdateOrderStatusSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, paymentStatus = PaymentStatus.SUCCESS, isWebViewOpened = false))
            }
        }
    }

    companion object {
        private var INSTANCE: PaymentReducer? = null
        fun getInstance(initialVal: PaymentUiState, viewModel: PaymentViewModel): PaymentReducer {
            if (INSTANCE == null) {
                INSTANCE = PaymentReducer(initialVal, viewModel)
            }
            return INSTANCE!!
        }
    }

}

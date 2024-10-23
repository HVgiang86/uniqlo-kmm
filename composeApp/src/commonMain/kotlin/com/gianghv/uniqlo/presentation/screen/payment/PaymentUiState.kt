package com.gianghv.uniqlo.presentation.screen.payment

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer

@Immutable
data class PaymentUiState(
    override val isLoading: Boolean,
    override val error: ErrorState?,
    val paymentUrl: String? = null,
    val orderId: Long? = null,
    val cannotCreatePayment: ErrorState? = null,
    val isWebViewOpened: Boolean = false,
    val paymentStatus: PaymentStatus = PaymentStatus.NOT_YET
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = PaymentUiState(
            isLoading = false, error = null, paymentUrl = null, cannotCreatePayment = null, orderId = null
        )
    }
}

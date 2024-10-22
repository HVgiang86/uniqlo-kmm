package com.gianghv.uniqlo.presentation.screen.order

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.CartItem
import com.gianghv.uniqlo.domain.User

@Immutable
data class OrderUiState(
    override val isLoading: Boolean,
    override val error: ErrorState?,
    val carts: List<CartItem> = emptyList(),
    val user: User? = null,
    val email: String? = "",
    val address: String? = "",
    val phone: String? = "",
    val orderName: String? = "",
    val paymentMethod: PaymentMethodBase? = PaymentMethod.Cash

    ) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = OrderUiState(
            isLoading = false, error = null
        )
    }
}

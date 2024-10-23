package com.gianghv.uniqlo.presentation.screen.orderhistory

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.data.source.remote.response.CreateOrderResponse
import com.gianghv.uniqlo.domain.OrderHistory


@Immutable
data class OrderHistoryUiState(
    override val isLoading: Boolean, override val error: ErrorState?, val orderList: List<OrderHistory> = emptyList()
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = OrderHistoryUiState(
            isLoading = true, error = null
        )
    }
}

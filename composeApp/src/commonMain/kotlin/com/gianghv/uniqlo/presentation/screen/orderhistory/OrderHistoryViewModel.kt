package com.gianghv.uniqlo.presentation.screen.orderhistory

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.CartRepository
import com.gianghv.uniqlo.data.WholeApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderHistoryViewModel(private val cartRepository: CartRepository) : BaseViewModel<OrderHistoryUiState, OrderHistoryUiEvent>() {
    override val state: StateFlow<OrderHistoryUiState>
        get() = reducer.state
    override val reducer: Reducer<OrderHistoryUiState, OrderHistoryUiEvent>
        get() = OrderHistoryReducer.getInstance(OrderHistoryUiState.initial(), this)

    override fun onClearLoadingState() {
    }

    override fun onClearErrorState() {
    }

    override val onException: (Throwable) -> Unit
        get() = {
            reducer.sendEvent(OrderHistoryUiEvent.Error(it))
        }

    fun getOrderHistory() {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            cartRepository.getOrderHistory(WholeApp.USER_ID).collect {
                val orders = it.sortedByDescending { order -> order.id }
                reducer.sendEvent(OrderHistoryUiEvent.LoadOrderHistorySuccess(orders))
            }
        }
    }

}

class OrderHistoryReducer(initialVal: OrderHistoryUiState, private val viewModel: OrderHistoryViewModel) :
    Reducer<OrderHistoryUiState, OrderHistoryUiEvent>(initialVal) {
    override fun reduce(oldState: OrderHistoryUiState, event: OrderHistoryUiEvent) {
        when (event) {
            is OrderHistoryUiEvent.Error -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error, true)))
            }

            OrderHistoryUiEvent.LoadOrderHistory -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.getOrderHistory()
            }

            is OrderHistoryUiEvent.LoadOrderHistorySuccess -> {
                setState(oldState.copy(isLoading = false, error = null, orderList = event.orderList))
            }
        }
    }

    companion object {
        private var instance: OrderHistoryReducer? = null
        fun getInstance(initialVal: OrderHistoryUiState, viewModel: OrderHistoryViewModel): OrderHistoryReducer {
            if (instance == null) {
                instance = OrderHistoryReducer(initialVal, viewModel)
            }
            return instance!!
        }
    }
}

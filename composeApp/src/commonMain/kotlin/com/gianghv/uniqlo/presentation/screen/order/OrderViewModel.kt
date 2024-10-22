package com.gianghv.uniqlo.presentation.screen.order

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import kotlinx.coroutines.flow.StateFlow

class OrderViewModel() : BaseViewModel<OrderUiState, OrderUiEvent>() {
    override val state: StateFlow<OrderUiState>
        get() = reducer.state
    override val reducer: Reducer<OrderUiState, OrderUiEvent>
        get() = OrderReducer.getInstance(OrderUiState.initial(), this)

    override fun onClearLoadingState() {
    }

    override fun onClearErrorState() {
    }

    override val onException: (Throwable) -> Unit
        get() = {
            reducer.sendEvent(OrderUiEvent.Error(it))
        }
}

class OrderReducer(initialVal: OrderUiState, private val viewModel: OrderViewModel) : Reducer<OrderUiState, OrderUiEvent>(initialVal) {
    override fun reduce(oldState: OrderUiState, event: OrderUiEvent) {
        when (event) {
            is OrderUiEvent.Error -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error, true)))
            }

            OrderUiEvent.LoadSavedOrderInfo -> {
                setState(oldState.copy(isLoading = true, error = null))
            }

            is OrderUiEvent.LoadSavedOrderInfoSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, address = event.address, phone = event.phone))
            }

            is OrderUiEvent.LoadUserDetail -> {
                setState(oldState.copy(isLoading = true, error = null))
            }

            is OrderUiEvent.LoadUserDetailSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, user = event.user))
            }
        }
    }

    companion object {
        private var INSTANCE: OrderReducer? = null
        fun getInstance(initialVal: OrderUiState, viewModel: OrderViewModel): OrderReducer {
            if (INSTANCE == null) {
                INSTANCE = OrderReducer(initialVal, viewModel)
            }
            return INSTANCE!!
        }
    }
}

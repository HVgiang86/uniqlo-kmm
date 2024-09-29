package com.gianghv.uniqlo.presentation.screen.home

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.ProductRepository
import com.gianghv.uniqlo.util.logging.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val productRepository: ProductRepository) : BaseViewModel<HomeUiState, HomeUiEvent>() {
    private val _reducer = HomeReducer(HomeUiState.initial(), this)

    override val state: StateFlow<HomeUiState>
        get() = reducer.state

    override val reducer: Reducer<HomeUiState, HomeUiEvent>
        get() = _reducer

    override fun onClearLoadingState() {
    }

    override fun onClearErrorState() {
    }

    override val onException: (Throwable) -> Unit
        get() = {
            reducer.sendEvent(HomeUiEvent.Error(it))
        }

    fun getAllProduct() {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            productRepository.getAllProduct().collect {
                reducer.sendEvent(HomeUiEvent.LoadAllProductSuccess(it))
                AppLogger.d("Product list: ${it.size}")
            }
        }
    }
}

class HomeReducer(initialVal: HomeUiState, private val viewModel: HomeViewModel) : Reducer<HomeUiState, HomeUiEvent>(initialVal) {
    override fun reduce(oldState: HomeUiState, event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.LoadAllProduct -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.getAllProduct()
            }

            is HomeUiEvent.Error -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error, true)))
            }

            is HomeUiEvent.LoadAllProductSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, productList = event.productList))
            }
        }
    }
}

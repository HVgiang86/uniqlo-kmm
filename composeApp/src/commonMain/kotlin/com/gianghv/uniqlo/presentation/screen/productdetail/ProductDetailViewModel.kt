package com.gianghv.uniqlo.presentation.screen.productdetail

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.ProductRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(private val productRepository: ProductRepository) : BaseViewModel<ProductDetailUiState, ProductDetailUiEvent>() {
    override val state: StateFlow<ProductDetailUiState>
        get() = reducer.state
    override val reducer: Reducer<ProductDetailUiState, ProductDetailUiEvent>
        get() = ProductDetailReducer.getInstance(this)

    override fun onClearLoadingState() {

    }

    override fun onClearErrorState() {

    }

    override val onException: (Throwable) -> Unit
        get() = {
            reducer.sendEvent(ProductDetailUiEvent.Error(it))
        }

    fun getProductDetail(productId: Long) {
        val getProductExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            reducer.sendEvent(ProductDetailUiEvent.LoadProductFailed(throwable))
        }
        uiStateHolderScope(Dispatchers.IO).launch(getProductExceptionHandler) {
            productRepository.getProductDetail(productId).collect {
                reducer.sendEvent(ProductDetailUiEvent.LoadProductSuccess(it))
            }
        }
    }

    fun addToCart() {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {

        }
    }
}

class ProductDetailReducer(initialVal: ProductDetailUiState, private val viewModel: ProductDetailViewModel) :
    Reducer<ProductDetailUiState, ProductDetailUiEvent>(initialVal) {
    override fun reduce(oldState: ProductDetailUiState, event: ProductDetailUiEvent) {
        when (event) {
            ProductDetailUiEvent.AddToCart -> {}
            ProductDetailUiEvent.Checkout -> {}
            is ProductDetailUiEvent.Error -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error)))
            }

            is ProductDetailUiEvent.LoadProduct -> {
                setState(oldState.copy(isLoading = true, error = null, product = null))
                viewModel.getProductDetail(event.productId)
            }

            is ProductDetailUiEvent.LoadProductSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, product = event.product))
            }

            is ProductDetailUiEvent.LoadProductFailed -> {
                setState(oldState.copy(isLoading = false, error = null, requireExitError = ErrorState(event.error)))
            }
        }
    }

    companion object {
        private val INSTANCE: ProductDetailReducer? = null
        fun getInstance(viewModel: ProductDetailViewModel): ProductDetailReducer {
            return INSTANCE ?: ProductDetailReducer(ProductDetailUiState.initial(), viewModel)
        }
    }
}

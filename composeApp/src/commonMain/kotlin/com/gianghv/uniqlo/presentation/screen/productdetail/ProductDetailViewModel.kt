package com.gianghv.uniqlo.presentation.screen.productdetail

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.CartRepository
import com.gianghv.uniqlo.data.ProductRepository
import com.gianghv.uniqlo.data.UserRepository
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.domain.ProductVariation
import com.gianghv.uniqlo.presentation.screen.productdetail.components.VariationSize
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productRepository: ProductRepository, private val userRepository: UserRepository, private val cartRepository: CartRepository
) : BaseViewModel<ProductDetailUiState, ProductDetailUiEvent>() {
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
            if (it.message == "Added products to the cart") {
                reducer.sendEvent(ProductDetailUiEvent.Error(Exception("Sản phẩm đã có trong giỏ hàng")))
            } else {
                reducer.sendEvent(ProductDetailUiEvent.Error(it))
            }
        }

    fun getProductDetail(productId: Long) {
        val getProductExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            reducer.sendEvent(ProductDetailUiEvent.LoadProductFailed(throwable))
        }
        uiStateHolderScope(Dispatchers.IO).launch(getProductExceptionHandler) {
            productRepository.getProductDetail(productId).combine(userRepository.getWishlist(WholeApp.USER_ID)) { product, wishlist ->
                product.copy(isFavorite = wishlist.contains(product.id))
            }.collect {
                reducer.sendEvent(ProductDetailUiEvent.LoadProductSuccess(it))
            }
        }
    }

    fun addToCart(quantity: Int, size: VariationSize, variation: ProductVariation) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            val variationId = variation.id
            val sizeName = size.name
            cartRepository.addCartItem(WholeApp.USER_ID, quantity, sizeName, variationId).collect {
                reducer.sendEvent(ProductDetailUiEvent.AddToCartSuccess(it.id))
            }
        }
    }

    fun setFavorite(product: Product, isFavorite: Boolean) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            userRepository.changeWishlist(WholeApp.USER_ID, product.id, isFavorite).collect {
                if (it) {
                    reducer.sendEvent(ProductDetailUiEvent.SetFavoriteSuccess(product, isFavorite))
                } else {
                    reducer.sendEvent(ProductDetailUiEvent.SetFavoriteSuccess(product, !isFavorite))
                }
            }
        }
    }

    fun getRecommendedProducts(productId: Long) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            productRepository.getAllProduct().combine(productRepository.getSimilarProduct(productId)) { products, similarProducts ->
                products.filter { product ->
                    similarProducts.contains(product.id)
                }.take(10)
            }.combine(userRepository.getWishlist(WholeApp.USER_ID)) { recommendedProducts, wishlist ->
                recommendedProducts.map { product ->
                    product.copy(isFavorite = wishlist.contains(product.id))
                }
            }.collect {
                reducer.sendEvent(ProductDetailUiEvent.LoadRecommendedProductSuccess(it))
            }
        }
    }
}

class ProductDetailReducer(initialVal: ProductDetailUiState, private val viewModel: ProductDetailViewModel) :
    Reducer<ProductDetailUiState, ProductDetailUiEvent>(initialVal) {
    override fun reduce(oldState: ProductDetailUiState, event: ProductDetailUiEvent) {
        when (event) {
            is ProductDetailUiEvent.Error -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error)))
            }

            is ProductDetailUiEvent.LoadProduct -> {
                setState(
                    oldState.copy(
                        isLoading = true,
                        error = null,
                        product = null,
                        selectedSize = null,
                        selectedVariation = null,
                        requireExitError = null,
                        addCartSuccess = false,
                        orderSuccess = false
                    )
                )
                viewModel.getProductDetail(event.productId)
            }

            is ProductDetailUiEvent.LoadProductSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, product = event.product))
            }

            is ProductDetailUiEvent.LoadProductFailed -> {
                setState(oldState.copy(isLoading = false, error = null, requireExitError = ErrorState(event.error)))
            }

            is ProductDetailUiEvent.AddToCart -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.addToCart(quantity = event.quantity, size = event.size, variation = event.variation)
            }

            is ProductDetailUiEvent.Checkout -> {
                setState(oldState.copy(isLoading = true, error = null))
            }

            is ProductDetailUiEvent.SelectVariation -> {
                if (event.variation != null && event.size != null) {
                    setState(oldState.copy(selectedVariation = event.variation, selectedSize = event.size))
                } else if (event.variation != null) {
                    setState(oldState.copy(selectedVariation = event.variation))
                } else if (event.size != null) {
                    setState(oldState.copy(selectedSize = event.size))
                }
            }

            is ProductDetailUiEvent.AddToCartSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, addCartSuccess = true))
            }

            is ProductDetailUiEvent.CheckoutSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, orderSuccess = true))
            }

            is ProductDetailUiEvent.SetFavorite -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.setFavorite(event.product, event.isFavorite)
            }

            is ProductDetailUiEvent.SetFavoriteSuccess -> {
                val product = event.product
                val isFavorite = event.isFavorite

                val edited = product.copy(isFavorite = isFavorite)
                setState(oldState.copy(isLoading = false, error = null, product = edited))
            }

            is ProductDetailUiEvent.LoadRecommendedProduct -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.getRecommendedProducts(event.productId)
            }

            is ProductDetailUiEvent.LoadRecommendedProductSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, recommendedProducts = event.productList))
            }
        }
    }

    companion object {
        private var INSTANCE: ProductDetailReducer? = null
        fun getInstance(viewModel: ProductDetailViewModel): ProductDetailReducer {
            if (INSTANCE == null) {
                INSTANCE = ProductDetailReducer(ProductDetailUiState.initial(), viewModel)
            }
            return INSTANCE as ProductDetailReducer
        }
    }
}

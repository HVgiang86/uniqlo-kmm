package com.gianghv.uniqlo.presentation.screen.home

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.CartRepository
import com.gianghv.uniqlo.data.ProductRepository
import com.gianghv.uniqlo.data.UserRepository
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.domain.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(private val productRepository: ProductRepository, private val userRepository: UserRepository, private val cartRepository: CartRepository) :
    BaseViewModel<HomeUiState, HomeUiEvent>() {
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
            productRepository.getAllProduct().combine(userRepository.getWishlist(WholeApp.USER_ID)) { products, wishlist ->
                products.map { product ->
                    product.copy(isFavorite = wishlist.contains(product.id))
                }.sortedWith(compareByDescending { WholeApp.priorityProducts.contains(it.id) })
            }.collect {
                reducer.sendEvent(HomeUiEvent.LoadAllProductSuccess(it))
            }
        }
    }

    fun getRecommendProduct() {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            productRepository.getAllProduct().combine(productRepository.getUserRecommendProduct(WholeApp.USER_ID)) { products, recommendList ->
                products.filter { product ->
                    recommendList.contains(product.id)
                }.take(10)
            }.combine(userRepository.getWishlist(WholeApp.USER_ID)) { recommendedProducts, wishlist ->
                recommendedProducts.map { product ->
                    product.copy(isFavorite = wishlist.contains(product.id))
                }.sortedWith(compareByDescending { WholeApp.priorityProducts.contains(it.id) })
            }.collect {
                reducer.sendEvent(HomeUiEvent.LoadRecommendProductSuccess(it))
            }
        }
    }

    fun setFavorite(product: Product, isFavorite: Boolean) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            userRepository.changeWishlist(WholeApp.USER_ID, product.id, isFavorite).collect {
                if (it) {
                    reducer.sendEvent(HomeUiEvent.SetFavoriteSuccess(product, isFavorite))
                } else {
                    reducer.sendEvent(HomeUiEvent.SetFavoriteSuccess(product, !isFavorite))
                }
            }
        }
    }

    fun loadCartCount() {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            cartRepository.getCartItems(WholeApp.USER_ID).collect {
                reducer.sendEvent(HomeUiEvent.LoadCartCountSuccess(it.size))
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

            HomeUiEvent.LoadRecommendProduct -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.getRecommendProduct()
            }

            is HomeUiEvent.LoadRecommendProductSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, recommendProducts = event.products))
            }

            is HomeUiEvent.SetFavorite -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.setFavorite(event.product, event.isFavorite)
            }

            is HomeUiEvent.SetFavoriteSuccess -> {
                val product = event.product
                val isFavorite = event.isFavorite

                val products = oldState.productList.map {
                    if (it.id == product.id) {
                        it.copy(isFavorite = isFavorite)
                    } else {
                        it
                    }
                }

                val recommendProducts = oldState.recommendProducts.map {
                    if (it.id == product.id) {
                        it.copy(isFavorite = isFavorite)
                    } else {
                        it
                    }
                }

                setState(oldState.copy(isLoading = false, error = null, productList = products, recommendProducts = recommendProducts))
            }

            HomeUiEvent.LoadCartCount -> {
                viewModel.loadCartCount()
            }

            is HomeUiEvent.LoadCartCountSuccess -> {
                setState(oldState.copy(cartCount = event.count))
            }

            HomeUiEvent.Refresh -> {
                setState(HomeUiState.initial().copy(isLoading = true))
                viewModel.getAllProduct()
                viewModel.getRecommendProduct()
                viewModel.loadCartCount()
            }
        }
    }
}

package com.gianghv.uniqlo.presentation.screen.wishlist

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.CartRepository
import com.gianghv.uniqlo.data.ProductRepository
import com.gianghv.uniqlo.data.UserRepository
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.presentation.screen.home.HomeUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class WishListViewModel(private val productRepository: ProductRepository, private val userRepository: UserRepository, private val cartRepository: CartRepository) :
    BaseViewModel<WishListUiState, WishListUiEvent>() {
    override val state: StateFlow<WishListUiState>
        get() = reducer.state
    override val reducer: Reducer<WishListUiState, WishListUiEvent>
        get() = WishListReducer.getInstance(WishListUiState.initial(), this)

    override fun onClearLoadingState() {

    }

    override fun onClearErrorState() {

    }

    override val onException: (Throwable) -> Unit
        get() = {
            reducer.sendEvent(WishListUiEvent.Error(it))
        }

    fun loadFavorite() {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            productRepository.getAllProduct().combine(userRepository.getWishlist(WholeApp.USER_ID)) { products, wishlist ->
                products.map { product ->
                    product.copy(isFavorite = wishlist.contains(product.id))
                }
            }.collect {
                val allProducts = it
                val favoriteProductList = it.filter { product ->
                    product.isFavorite
                }
                reducer.sendEvent(WishListUiEvent.LoadFavoriteSuccess(allProducts, favoriteProductList))
            }
        }
    }

    fun setFavorite(product: Product, isFavorite: Boolean) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            userRepository.changeWishlist(WholeApp.USER_ID, product.id, isFavorite).collect {
                if (it) {
                    reducer.sendEvent(WishListUiEvent.SetFavoriteSuccess(product, isFavorite))
                } else {
                    reducer.sendEvent(WishListUiEvent.SetFavoriteSuccess(product, !isFavorite))
                }
            }
        }
    }

    fun loadCartCount() {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            cartRepository.getCartItems(WholeApp.USER_ID).collect {
                reducer.sendEvent(WishListUiEvent.LoadCartCountSuccess(it.size))
            }
        }
    }
}


class WishListReducer(initialVal: WishListUiState, private val viewModel: WishListViewModel) : Reducer<WishListUiState, WishListUiEvent>(initialVal) {
    override fun reduce(oldState: WishListUiState, event: WishListUiEvent) {
        when (event) {
            is WishListUiEvent.Error -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error)))
            }

            is WishListUiEvent.LoadFavorite -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.loadFavorite()
            }

            is WishListUiEvent.LoadFavoriteSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, favoriteProductList = event.favoriteProductList, allProducts = event.allProducts))
            }

            is WishListUiEvent.SetFavorite -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.setFavorite(event.product, event.isFavorite)
            }

            is WishListUiEvent.SetFavoriteSuccess -> {
                val product = event.product
                val isFavorite = event.isFavorite

                val products = oldState.favoriteProductList.filterNot {
                    it.id == product.id
                }.toMutableList()

                setState(oldState.copy(isLoading = false, error = null, favoriteProductList = products))
            }

            WishListUiEvent.LoadCartCount -> {
                viewModel.loadCartCount()
            }
            is WishListUiEvent.LoadCartCountSuccess -> {
                setState(oldState.copy(cartCount = event.count))
            }
        }
    }

    companion object {
        private var INSTANCE: WishListReducer? = null
        fun getInstance(initialVal: WishListUiState, viewModel: WishListViewModel): WishListReducer {
            if (INSTANCE == null) {
                INSTANCE = WishListReducer(initialVal, viewModel)
            }
            return INSTANCE!!
        }
    }

}

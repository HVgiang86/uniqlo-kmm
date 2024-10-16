package com.gianghv.uniqlo.presentation.screen.searchresult

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.ProductRepository
import com.gianghv.uniqlo.data.UserRepository
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.util.logging.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SearchResultViewModel(private val productRepository: ProductRepository, private val userRepository: UserRepository) :
    BaseViewModel<SearchResultUiState, SearchResultUiEvent>() {
    override val state: StateFlow<SearchResultUiState>
        get() = reducer.state
    override val reducer: Reducer<SearchResultUiState, SearchResultUiEvent>
        get() = SearchResultReducer.getInstance(SearchResultUiState.initial(), this)

    override fun onClearLoadingState() {
    }

    override fun onClearErrorState() {
    }

    override val onException: (Throwable) -> Unit
        get() = {
            reducer.sendEvent(SearchResultUiEvent.Error(it))
        }

    fun searchWithFilter(query: String, filterState: FilterState) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            productRepository.getAllProduct().combine(userRepository.getWishlist(WholeApp.USER_ID)) { products, wishlist ->
                products.map { product ->
                    product.copy(isFavorite = wishlist.contains(product.id))
                }
            }.collect {
                val allProducts = it
                val searchResult = it.filter { product ->
                    product.name?.contains(query, ignoreCase = true) == true
                }.sortedWith(compareByDescending { WholeApp.priorityProducts.contains(it.id) })
                val filteredList = applyFilterForProductList(searchResult, filterState)
                setAllColorList(allProducts)
                reducer.sendEvent(SearchResultUiEvent.SearchForProductSuccess(searchResult, filteredList, allProducts))
            }
        }
    }

    fun getRecommendProduct(filterState: FilterState) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            productRepository.getAllProduct().combine(productRepository.getUserRecommendProduct(WholeApp.USER_ID)) { products, recommendList ->
                products.filter { product ->
                    recommendList.contains(product.id)
                }
            }.combine(userRepository.getWishlist(WholeApp.USER_ID)) { recommendedProducts, wishlist ->
                recommendedProducts.map { product ->
                    product.copy(isFavorite = wishlist.contains(product.id))
                }.sortedWith(compareByDescending { WholeApp.priorityProducts.contains(it.id) })
            }.collect {
                val allProducts = it
                val searchResult = it
                val filteredList = applyFilterForProductList(searchResult, filterState)
                setAllColorList(allProducts)
                reducer.sendEvent(SearchResultUiEvent.SearchForProductSuccess(searchResult, filteredList, allProducts))
            }
        }
    }

    fun setFavorite(product: Product, isFavorite: Boolean) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            userRepository.changeWishlist(WholeApp.USER_ID, product.id, isFavorite).collect {
                if (it) {
                    reducer.sendEvent(SearchResultUiEvent.SetFavoriteSuccess(product, isFavorite))
                } else {
                    reducer.sendEvent(SearchResultUiEvent.SetFavoriteSuccess(product, !isFavorite))
                }
            }
        }
    }

    private fun setAllColorList(products: List<Product>) {
        val allColorList = mutableListOf<String>()
        products.forEach { product ->
            product.variations?.forEach { variation ->
                if (!allColorList.contains(variation.color?.uppercase())) {
                    allColorList.add(variation.color?.uppercase() ?: "")
                }
            }
        }
        reducer.setState(reducer.state.value.copy(filterState = reducer.state.value.filterState.copy(allColorList = allColorList)))
    }

    fun applyFilter(products: List<Product>, filterState: FilterState) {
        val filteredList = applyFilterForProductList(products, filterState)
        reducer.sendEvent(SearchResultUiEvent.ApplyFilterSuccess(filteredList))
    }

    private fun applyFilterForProductList(productList: List<Product>, filterState: FilterState): List<Product> {
        val priceFilter = filterState.priceFilter
        val colorFilter = filterState.colorFilter
        val sizeFilter = filterState.sizeFilter

        AppLogger.d("Before filter: $productList")

        //filter size
        val filteredLSize: List<Product> = productList

        //filter color
        val filteredColor = if (colorFilter != null) {
            filteredLSize.filter { product ->
                product.variations?.any { variation ->
                    colorFilter.color.equals(variation.color, ignoreCase = true) && variation.stock != null && variation.stock > 0
                } == true
            }
        } else filteredLSize

        AppLogger.d("filteredColor: $filteredColor")

        //sort by price
        val filteredPrice = if (priceFilter != null) {
            if (priceFilter.isDesc) {
                filteredColor.sortedByDescending { it.price }
            } else {
                filteredColor.sortedBy { it.price }
            }
        } else {
            filteredColor
        }

        AppLogger.d("filteredPrice: $filteredPrice")

        return filteredPrice
    }
}

class SearchResultReducer(initialVal: SearchResultUiState, private val viewModel: SearchResultViewModel) :
    Reducer<SearchResultUiState, SearchResultUiEvent>(initialVal) {
    override fun reduce(oldState: SearchResultUiState, event: SearchResultUiEvent) {
        when (event) {
            is SearchResultUiEvent.Error -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error)))
            }

            is SearchResultUiEvent.SearchForProduct -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.searchWithFilter(event.query, event.filterState)
            }

            is SearchResultUiEvent.SearchForProductSuccess -> {
                setState(
                    oldState.copy(
                        isLoading = false, error = null, productList = event.productList, searchResult = event.searchResult, allProducts = event.allProducts
                    )
                )
            }

            is SearchResultUiEvent.ApplyFilter -> {
                setState(oldState.copy(filterState = event.filterState))
                viewModel.applyFilter(oldState.searchResult, event.filterState)
            }

            is SearchResultUiEvent.ApplyFilterSuccess -> {
                setState(oldState.copy(productList = event.productList))
            }

            is SearchResultUiEvent.SetFavorite -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.setFavorite(event.product, event.isFavorite)
            }

            is SearchResultUiEvent.SetFavoriteSuccess -> {
                val product = event.product
                val isFavorite = event.isFavorite

                val products = oldState.productList.map {
                    if (it.id == product.id) {
                        it.copy(isFavorite = isFavorite)
                    } else {
                        it
                    }
                }

                setState(oldState.copy(isLoading = false, error = null, productList = products))
            }

            is SearchResultUiEvent.GetRecommendProduct -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.getRecommendProduct(event.filterState)
            }
        }
    }

    companion object {
        private var INSTANCE: SearchResultReducer? = null
        fun getInstance(initialVal: SearchResultUiState, viewModel: SearchResultViewModel): SearchResultReducer {
            if (INSTANCE == null) {
                INSTANCE = SearchResultReducer(initialVal, viewModel)
            }
            return INSTANCE!!
        }
    }
}

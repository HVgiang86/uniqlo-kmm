package com.gianghv.uniqlo.presentation.screen.searchresult

import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.Product

sealed class SearchResultUiEvent : Reducer.UiEvent {
    data class SearchForProduct(val query: String, val filterState: FilterState) : SearchResultUiEvent()
    data class SearchForProductSuccess(val searchResult: List<Product>, val productList: List<Product>, val allProducts: List<Product>) : SearchResultUiEvent()
    data class GetRecommendProduct(val filterState: FilterState) : SearchResultUiEvent()
    data class ApplyFilter(val filterState: FilterState) : SearchResultUiEvent()
    data class ApplyFilterSuccess(val productList: List<Product>) : SearchResultUiEvent()
    data class Error(val error: Throwable) : SearchResultUiEvent()
    data class SetFavorite(val product: Product, val isFavorite: Boolean) : SearchResultUiEvent()
    data class SetFavoriteSuccess(val product: Product, val isFavorite: Boolean) : SearchResultUiEvent()
}

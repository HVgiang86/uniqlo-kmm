package com.gianghv.uniqlo.presentation.screen.wishlist

import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.presentation.screen.searchresult.SearchResultUiEvent

sealed class WishListUiEvent : Reducer.UiEvent {
    data class Error(val error: Throwable) : WishListUiEvent()
    data class SetFavorite(val product: Product, val isFavorite: Boolean) : WishListUiEvent()
    data class SetFavoriteSuccess(val product: Product, val isFavorite: Boolean) : WishListUiEvent()
    data object LoadFavorite : WishListUiEvent()
    data class LoadFavoriteSuccess(val allProducts: List<Product>, val favoriteProductList: List<Product>) : WishListUiEvent()
    data object LoadCartCount: WishListUiEvent()
    data class LoadCartCountSuccess(val count: Int): WishListUiEvent()
}

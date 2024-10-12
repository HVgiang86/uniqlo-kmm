package com.gianghv.uniqlo.presentation.screen.home

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.Product

@Immutable
sealed class HomeUiEvent : Reducer.UiEvent {
    data object LoadAllProduct : HomeUiEvent()
    data object LoadRecommendProduct: HomeUiEvent()
    data class LoadRecommendProductSuccess(val products: List<Product>): HomeUiEvent()
    data class LoadAllProductSuccess(val productList: List<Product>) : HomeUiEvent()
    data class Error(val error: Throwable) : HomeUiEvent()
}

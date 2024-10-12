package com.gianghv.uniqlo.presentation.screen.searchresult

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.Product

@Immutable
data class SearchResultUiState(
    override val isLoading: Boolean,
    override val error: ErrorState?,
    val productList: List<Product> = emptyList(),
    val searchResult: List<Product> = emptyList(),
    val allProducts: List<Product> = emptyList(),
    val filterState: FilterState,
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = SearchResultUiState(
            isLoading = false, error = null, filterState = FilterState()
        )
    }
}

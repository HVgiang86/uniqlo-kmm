package com.gianghv.uniqlo.presentation.screen.home.list

import com.gianghv.uniqlo.domain.Product

sealed class ListItem {
    data class Header(val title: String) : ListItem()
    data class RecommendProducts(val products: List<Product>) : ListItem()
    data class AllProducts(val products: List<Product>) : ListItem()
    data object TopBanner: ListItem()
    data object CollectionBanner: ListItem()

}

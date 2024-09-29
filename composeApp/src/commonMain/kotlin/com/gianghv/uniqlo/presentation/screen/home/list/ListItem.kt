package com.gianghv.uniqlo.presentation.screen.home.list

import com.gianghv.uniqlo.domain.Product

sealed class ListItem {
    data class Header(val title: String) : ListItem()
    data class Item(val product: Product) : ListItem()
    data object TopBanner: ListItem()
    data object CollectionBanner: ListItem()
}

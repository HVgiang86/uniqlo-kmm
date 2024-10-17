package com.gianghv.uniqlo.di

import com.gianghv.uniqlo.presentation.screen.auth.AuthViewModel
import com.gianghv.uniqlo.presentation.screen.cart.CartViewModel
import com.gianghv.uniqlo.presentation.screen.home.HomeViewModel
import com.gianghv.uniqlo.presentation.screen.main.MainViewModel
import com.gianghv.uniqlo.presentation.screen.productdetail.ProductDetailViewModel
import com.gianghv.uniqlo.presentation.screen.profile.ProfileViewModel
import com.gianghv.uniqlo.presentation.screen.searchresult.SearchResultViewModel
import com.gianghv.uniqlo.presentation.screen.wishlist.WishListViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { MainViewModel() }
    single { AuthViewModel(get(), get()) }
    single { HomeViewModel(get(), get(), get()) }
    single { ProductDetailViewModel(get(), get(), get()) }
    single { SearchResultViewModel(get(), get()) }
    single { WishListViewModel(get(), get(), get()) }
    single { ProfileViewModel(get(), get()) }
    single { CartViewModel(get(), get(), get()) }
}

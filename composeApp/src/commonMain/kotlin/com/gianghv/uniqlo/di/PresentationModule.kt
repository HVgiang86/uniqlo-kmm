package com.gianghv.uniqlo.di

import com.gianghv.uniqlo.presentation.screen.auth.AuthViewModel
import com.gianghv.uniqlo.presentation.screen.home.HomeViewModel
import com.gianghv.uniqlo.presentation.screen.main.MainViewModel
import com.gianghv.uniqlo.presentation.screen.productdetail.ProductDetailViewModel
import com.gianghv.uniqlo.presentation.screen.searchresult.SearchResultViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { MainViewModel() }
    single { AuthViewModel(get(), get()) }
    single { HomeViewModel(get()) }
    single { ProductDetailViewModel(get()) }
    single { SearchResultViewModel(get()) }
}

package com.gianghv.uniqlo.di

import com.gianghv.uniqlo.presentation.screen.auth.AuthViewModel
import com.gianghv.uniqlo.presentation.screen.main.MainViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { MainViewModel() }
    single { AuthViewModel(get(), get()) }
}

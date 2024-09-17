package com.gianghv.uniqlo.di

import com.gianghv.uniqlo.presentation.screen.login.LoginViewModel
import com.gianghv.uniqlo.presentation.screen.main.MainViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { MainViewModel() }
    factory { LoginViewModel(get()) }
}

package com.gianghv.uniqlo.di

import org.koin.core.module.Module


internal expect val platformModule: Module
val appModules: List<Module>
    get() = listOf(platformModule, networkModule, presentationModule, dataModule, apiServiceModule, mapperModule)

package com.gianghv.uniqlo.di

import com.gianghv.uniqlo.data.AppRepository
import com.gianghv.uniqlo.data.ProductRepository
import com.gianghv.uniqlo.data.UserRepository
import com.gianghv.uniqlo.data.repository.AppRepositoryImpl
import com.gianghv.uniqlo.data.repository.ProductRepositoryImpl
import com.gianghv.uniqlo.data.repository.UserRepositoryImpl
import com.gianghv.uniqlo.data.source.preferences.UserPreferences
import com.gianghv.uniqlo.data.source.preferences.UserPreferencesImpl
import com.gianghv.uniqlo.data.source.remote.ProductDataSource
import com.gianghv.uniqlo.data.source.remote.ProductDataSourceImpl
import com.gianghv.uniqlo.data.source.remote.UserRemoteSource
import com.gianghv.uniqlo.data.source.remote.UserRemoteSourceImpl
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val repositoryModule = module {
    single<UserRepository> {
        UserRepositoryImpl(get(), get())
    }
    single<ProductRepository> {
        ProductRepositoryImpl(get())
    }
    singleOf(::AppRepositoryImpl) bind AppRepository::class
}

private val preferencesSourceModule = module {
    single { Settings() } bind Settings::class
    singleOf(::UserPreferencesImpl) bind UserPreferences::class
}

private val dispatcherModule = module {
    single<CoroutineDispatcher> {
        Dispatchers.IO
    }
}

private val dataSourceModule = module {
    single<UserRemoteSource> {
        UserRemoteSourceImpl(get())
    }

    single<ProductDataSource> {
        ProductDataSourceImpl(get())
    }
}

val dataModule = module {
    includes(preferencesSourceModule, repositoryModule, dispatcherModule, dataSourceModule)
}

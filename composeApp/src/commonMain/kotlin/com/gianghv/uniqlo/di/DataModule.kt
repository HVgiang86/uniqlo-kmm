package com.gianghv.uniqlo.di

import com.gianghv.uniqlo.data.AppRepository
import com.gianghv.uniqlo.data.CartRepository
import com.gianghv.uniqlo.data.ChatRepository
import com.gianghv.uniqlo.data.ProductRepository
import com.gianghv.uniqlo.data.UserRepository
import com.gianghv.uniqlo.data.repository.AppRepositoryImpl
import com.gianghv.uniqlo.data.repository.CartRepositoryImpl
import com.gianghv.uniqlo.data.repository.ChatRepositoryImpl
import com.gianghv.uniqlo.data.repository.ProductRepositoryImpl
import com.gianghv.uniqlo.data.repository.UserRepositoryImpl
import com.gianghv.uniqlo.data.source.preferences.UserPreferences
import com.gianghv.uniqlo.data.source.preferences.UserPreferencesImpl
import com.gianghv.uniqlo.data.source.remote.CartRemote
import com.gianghv.uniqlo.data.source.remote.CartRemoteImpl
import com.gianghv.uniqlo.data.source.remote.ChatRemote
import com.gianghv.uniqlo.data.source.remote.ChatRemoteImpl
import com.gianghv.uniqlo.data.source.remote.ProductDataSource
import com.gianghv.uniqlo.data.source.remote.ProductDataSourceImpl
import com.gianghv.uniqlo.data.source.remote.UserRemoteSource
import com.gianghv.uniqlo.data.source.remote.UserRemoteSourceImpl
import com.gianghv.uniqlo.util.createSettings
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val repositoryModule = module {
    single<UserRepository> {
        UserRepositoryImpl(get(), get(), get())
    }
    single<ProductRepository> {
        ProductRepositoryImpl(get())
    }
    singleOf(::AppRepositoryImpl) bind AppRepository::class
    single<CartRepository>{
        CartRepositoryImpl(get())
    }
    single<ChatRepository> { ChatRepositoryImpl(get()) }
}

private val preferencesSourceModule = module {
    single<Settings> {
        createSettings()
    }
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

    single<CartRemote> {
        CartRemoteImpl(get())
    }

    single<ChatRemote> {
        ChatRemoteImpl(get())
    }
}

val dataModule = module {
    includes(preferencesSourceModule, repositoryModule, dispatcherModule, dataSourceModule)
}

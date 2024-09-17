package com.gianghv.uniqlo.di

import com.gianghv.uniqlo.maper.UserMapper
import org.koin.dsl.module

val mapperModule = module {
    single<UserMapper> { UserMapper() }

}

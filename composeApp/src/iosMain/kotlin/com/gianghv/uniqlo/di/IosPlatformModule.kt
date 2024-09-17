package com.gianghv.uniqlo.di

import com.gianghv.uniqlo.util.AppOpenerUtil
import com.gianghv.uniqlo.util.AppOpenerUtilImpl
import com.gianghv.uniqlo.util.AppVersion
import com.gianghv.uniqlo.util.IosAppVersion
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    factoryOf(::AppOpenerUtilImpl) bind AppOpenerUtil::class
    factoryOf(::IosAppVersion) bind AppVersion::class
}

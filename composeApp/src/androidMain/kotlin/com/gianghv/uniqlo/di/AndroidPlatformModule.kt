package com.gianghv.uniqlo.di

import com.gianghv.uniqlo.util.AppOpenerUtil
import com.gianghv.uniqlo.util.AppOpenerUtilImpl
import com.gianghv.uniqlo.util.AppVersion
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.gianghv.uniqlo.util.AndroidAppVersion
import org.koin.dsl.bind

internal actual val platformModule: Module = module {
    factoryOf(::AppOpenerUtilImpl) bind AppOpenerUtil::class
    factoryOf(::AndroidAppVersion) bind AppVersion::class
}

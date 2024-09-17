package com.gianghv.uniqlo.rootview

import com.gianghv.uniqlo.di.appModules
import com.gianghv.uniqlo.util.logging.AppLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

object AppInitializer {

    fun initialize(isDebug: Boolean = false, onKoinStart: KoinApplication.() -> Unit) {
        if (isDebug) AppLogger.initialize()

        startKoin {
            onKoinStart()
            modules(appModules)
            onApplicationStart()
        }
    }

    private fun KoinApplication.onApplicationStart() {
        AppLogger.d("Application Started")/* no-op */
    }
}

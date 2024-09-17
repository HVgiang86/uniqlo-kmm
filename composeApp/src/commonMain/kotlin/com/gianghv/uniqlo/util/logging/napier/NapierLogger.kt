package com.gianghv.uniqlo.util.logging.napier

import com.gianghv.uniqlo.util.logging.AppLogger
import com.gianghv.uniqlo.util.logging.Logger
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class NapierLogger : Logger {
    override fun initialize() {
        Napier.base(
            DebugAntilog(
                excludedClassList = listOf(
                    NapierLogger::class.qualifiedName ?: "",
                    AppLogger::class.qualifiedName ?: ""
                )
            )
        )
    }

    override fun e(message: String?) {
        Napier.e(message ?: "")
    }

    override fun d(message: String?) {
        Napier.d(message ?: "")
    }

    override fun i(message: String?) {
        Napier.i(message ?: "")
    }


}

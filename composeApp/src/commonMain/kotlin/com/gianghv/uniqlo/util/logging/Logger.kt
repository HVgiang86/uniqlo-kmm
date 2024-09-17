package com.gianghv.uniqlo.util.logging

import com.gianghv.uniqlo.util.logging.napier.NapierLogger

interface Logger {
    // Should be called on Application start
    fun initialize()

    fun e(message: String?)
    fun d(message: String?)
    fun i(message: String?)
}
object AppLogger : Logger by NapierLogger()

package com.gianghv.uniqlo.util.ext

import java.text.NumberFormat
import java.util.Locale

actual fun Double.toCurrencyText(): String {
    val roundedNumber = String.format(Locale.US, "%.2f", this).toDouble()
    val numberFormat = NumberFormat.getNumberInstance(Locale.US)
    return "${numberFormat.format(roundedNumber)} VND"
}

package com.gianghv.uniqlo.util.dateformat

expect object DateTime {
    fun getFormattedDate(
        timestamp: String,
        format: String = "MMM dd, yyyy HH:mm:ss",
        outputFormat: String = "DD/MM/YYYY"
    ): String?
}

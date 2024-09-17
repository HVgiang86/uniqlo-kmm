package com.gianghv.uniqlo.util.ext

import com.gianghv.uniqlo.constant.YYYY_MM_DD
import com.gianghv.uniqlo.util.dateformat.DateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Long.toLocalDate(): LocalDate {
    val instant = Instant.fromEpochMilliseconds(this)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
}

fun Any.getCurrentDate(): LocalDate {
    val instant = Clock.System.now()
    return instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
}

fun Long.millisToDateString(format: String) : String? {
    val localDate = this.toLocalDate()
    return DateTime.getFormattedDate(localDate.toString(), YYYY_MM_DD, format)
}

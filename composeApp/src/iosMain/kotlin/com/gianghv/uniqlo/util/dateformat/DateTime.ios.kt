package com.gianghv.uniqlo.util.dateformat

import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone


actual object DateTime {
    actual fun getFormattedDate(timestamp: String, format: String, outputFormat: String): String? {
        val df = NSDateFormatter()

        df.dateFormat = format

        // Set the time zone to GMT (or UTC)
        df.timeZone = NSTimeZone.localTimeZone

        // Parse the GMT timestamp into an NSDate
        val date = df.dateFromString(timestamp)

        // Create a date formatter for the local time zone
        df.timeZone = NSTimeZone.localTimeZone
        df.dateFormat = outputFormat

        // Format the NSDate into a string in the local time zone
        return date?.let { df.stringFromDate(it) }
    }
}

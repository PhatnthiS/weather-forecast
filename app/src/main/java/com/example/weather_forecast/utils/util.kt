package com.example.weather_forecast.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

fun formatUnixTime(timestamp: Long?, timezoneOffsetSeconds: Long?): String {
    if (timestamp != null && timezoneOffsetSeconds != null) {
        val date = Date(timestamp * 1000)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val offsetMillis = timezoneOffsetSeconds * 1000
        val timeZone = TimeZone.getTimeZone("GMT")
        timeZone.rawOffset = offsetMillis.toInt()
        sdf.timeZone = timeZone

        return sdf.format(date)
    } else return "-"
}

fun String.toTitleCase(): String {
    return this.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

fun mpsToKmh(speedMps: Double): String {
    return (speedMps * 3.6).roundToInt().toString()
}
package com.example.weather_forecast.utils

import com.example.weather_forecast.domain.model.ForecastItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

fun formatUnixTime(timestamp: Long?, timezoneOffsetSeconds: Long?, pattern: String): String {
    if (timestamp != null && timezoneOffsetSeconds != null) {
        val date = Date(timestamp * 1000)
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
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

fun List<ForecastItem>.groupForecastByDay(): Map<String, List<ForecastItem>> {
    return this.groupBy {
        it.time.substringBeforeLast(" ")
    }
}

fun formatDateTime(time: String, pattern: String): String {
    val inputFormatter = SimpleDateFormat("E, dd/MM HH:mm",Locale.getDefault())
    val outputFormatter = SimpleDateFormat(pattern, Locale.getDefault())

    val date = inputFormatter.parse(time) ?: return ""
    return outputFormatter.format(date)
}
package com.example.weather_forecast.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatUnixTime(timestamp: Long?, timezoneOffsetSeconds: Long?): String {
    if (timestamp != null && timezoneOffsetSeconds != null) {
        val date = Date(timestamp * 1000)
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val offsetMillis = timezoneOffsetSeconds * 1000
        val timeZone = TimeZone.getTimeZone("GMT")
        timeZone.rawOffset = offsetMillis.toInt()
        sdf.timeZone = timeZone

        return sdf.format(date)
    } else return "-"
}
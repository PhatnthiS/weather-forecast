package com.example.weather_forecast.utils

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.weather_forecast.R
import com.example.weather_forecast.data.remote.WeatherException
import com.example.weather_forecast.domain.model.ErrorInfo
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
    val inputFormatter = SimpleDateFormat("E, dd/MM HH:mm", Locale.getDefault())
    val outputFormatter = SimpleDateFormat(pattern, Locale.getDefault())

    val date = inputFormatter.parse(time) ?: return ""
    return outputFormatter.format(date)
}


fun weatherInfoGradient(context: Context, description: String, isNight: Boolean): Brush {
    val colors = when {
        description.contains(
            context.getString(R.string.clear),
            ignoreCase = true
        ) && !isNight -> listOf(
            Color(context.getColor(R.color.sunny_yellow)),
            Color(context.getColor(R.color.soft_sky_blue))
        )

        description.contains(
            context.getString(R.string.clear),
            ignoreCase = true
        ) && isNight -> listOf(
            Color(context.getColor(R.color.deep_navy)),
            Color(context.getColor(R.color.night_black))
        )

        description.contains(
            context.getString(R.string.cloud),
            ignoreCase = true
        ) && !isNight -> listOf(
            Color(context.getColor(R.color.pale_blue)),
            Color(context.getColor(R.color.cloud_gray))
        )

        description.contains(
            context.getString(R.string.cloud),
            ignoreCase = true
        ) && isNight -> listOf(
            Color(context.getColor(R.color.dark_gray)),
            Color(context.getColor(R.color.night_black))
        )

        description.contains(
            context.getString(R.string.rain),
            ignoreCase = true
        ) && !isNight -> listOf(
            Color(context.getColor(R.color.storm_blue)),
            Color(context.getColor(R.color.light_steel_blue))
        )

        description.contains(
            context.getString(R.string.rain),
            ignoreCase = true
        ) && isNight -> listOf(
            Color(context.getColor(R.color.storm_navy)),
            Color(context.getColor(R.color.night_black))
        )

        else -> listOf(Color.Gray, Color.LightGray)
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )
}

fun handleErrorException(e: WeatherException): ErrorInfo {
    return when (e) {
        is WeatherException.HttpError -> ErrorInfo(e.resId, code = e.code, msg = e.msg)
        else -> ErrorInfo(e.resId)
    }
}
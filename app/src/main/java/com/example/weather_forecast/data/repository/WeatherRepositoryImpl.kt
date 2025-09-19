package com.example.weather_forecast.data.repository

import com.example.weather_forecast.data.model.WeatherResponse
import com.example.weather_forecast.data.remote.ApiService
import com.example.weather_forecast.domain.model.WeatherInfo
import com.example.weather_forecast.domain.repository.WeatherRepository
import com.example.weather_forecast.utils.Constants.OPEN_WEATHER_ICON_URL
import com.example.weather_forecast.utils.formatUnixTime
import com.example.weather_forecast.utils.mpsToKmh
import kotlin.math.roundToInt


class WeatherRepositoryImpl(
    private val api: ApiService,
    private val apiKey: String
) : WeatherRepository {
    override suspend fun getWeatherByCity(city: String): WeatherInfo {
        val response = api.getWeatherByCity(city, apiKey)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body.toDomain()
            } else {
                throw Exception("Empty response")
            }
        } else {
            when (response.code()) {
                401 -> throw Exception("Unauthorized: Invalid API key")
                404 -> throw Exception("City not found")
                else -> throw Exception("HTTP ${response.code()}: ${response.message()}")
            }
        }
    }
}

fun WeatherResponse.toDomain(): WeatherInfo {
    return WeatherInfo(
        cityName = name ?: "Unknown",
        temperature = main?.temp?.roundToInt()?.toString() ?: "-",
        feelsLike = main?.feelsLike?.roundToInt()?.toString() ?: "-",
        humidity = main?.humidity?.toString() ?: "-",
        description = weather?.firstOrNull()?.description ?: "-",
        icon = String.format(OPEN_WEATHER_ICON_URL, weather?.firstOrNull()?.icon),
        windSpeed = wind?.speed?.let { mpsToKmh(it) } ?: "-",
        timezone = timezone?.toString() ?: "-",
        country = sys?.country ?: "",
        sunrise = formatUnixTime(sys?.sunrise, timezone),
        sunset = formatUnixTime(sys?.sunset, timezone),
        updatedAt = formatUnixTime(dt, timezone)
    )
}
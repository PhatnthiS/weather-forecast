package com.example.weather_forecast.data.repository

import com.example.weather_forecast.data.model.WeatherResponse
import com.example.weather_forecast.data.remote.ApiService
import com.example.weather_forecast.domain.model.WeatherInfo
import com.example.weather_forecast.domain.repository.WeatherRepository
import com.example.weather_forecast.utils.formatUnixTime


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
        temperature = main?.temp?.toString() ?: "-",
        feelsLike = main?.feelsLike?.toString() ?: "-",
        humidity = main?.humidity?.toString() ?: "-",
        description = weather?.firstOrNull()?.description ?: "-",
        icon = weather?.firstOrNull()?.icon ?: "-",
        windSpeed = wind?.speed?.toString() ?: "-",
        timezone = timezone?.toString() ?: "-",
        sunrise = formatUnixTime(sys?.sunrise, timezone),
        sunset = formatUnixTime(sys?.sunset, timezone),
        updatedAt =formatUnixTime(dt,timezone)
    )
}
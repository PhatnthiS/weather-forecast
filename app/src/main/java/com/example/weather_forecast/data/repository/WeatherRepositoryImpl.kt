package com.example.weather_forecast.data.repository

import com.example.weather_forecast.data.model.ForecastWeatherResponse
import com.example.weather_forecast.data.model.WeatherResponse
import com.example.weather_forecast.data.remote.ApiService
import com.example.weather_forecast.domain.model.ForecastItem
import com.example.weather_forecast.domain.model.ForecastWeatherInfo
import com.example.weather_forecast.domain.model.WeatherInfo
import com.example.weather_forecast.domain.repository.WeatherRepository
import com.example.weather_forecast.utils.formatUnixTime
import com.example.weather_forecast.utils.mpsToKmh
import com.example.weather_forecast.utils.toTitleCase
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

    override suspend fun getForecastWeatherByCity(city: String): ForecastWeatherInfo {
        val response = api.getForecastWeatherByCity(city, apiKey)
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
        description = weather?.firstOrNull()?.description?.toTitleCase() ?: "-",
        icon =weather?.firstOrNull()?.icon ?: "-",
        windSpeed = wind?.speed?.let { mpsToKmh(it) } ?: "-",
        timezone = timezone?.toString() ?: "-",
        country = sys?.country ?: "",
        sunrise = formatUnixTime(sys?.sunrise, timezone,"HH:mm"),
        sunset = formatUnixTime(sys?.sunset, timezone,"HH:mm"),
        updatedAt = formatUnixTime(dt, timezone,"E, dd/MM HH:mm")
    )
}

fun ForecastWeatherResponse.toDomain(): ForecastWeatherInfo {
    val items = this.list.orEmpty().map { item ->
        ForecastItem(
            time = formatUnixTime(item.dt, city?.timezone,"E, dd/MM HH:mm"),
            temperature = item.main?.temp?.roundToInt()?.toString() ?: "-",
            icon =item.weather?.firstOrNull()?.icon ?: "-",
            pop = "${((item.pop ?: 0.0) * 100).roundToInt()}%"
        )
    }
    return ForecastWeatherInfo(items)
}


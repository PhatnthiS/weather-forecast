package com.example.weather_forecast.data.repository

import com.example.weather_forecast.data.model.WeatherResponse
import com.example.weather_forecast.data.remote.ApiService
import com.example.weather_forecast.domain.model.WeatherInfo
import com.example.weather_forecast.domain.repository.WeatherRepository


class WeatherRepositoryImpl(
    private val api: ApiService,
    private val apiKey: String
) : WeatherRepository {
    override suspend fun getWeatherByCity(city: String): WeatherInfo {
        val response = api.getWeatherByCity(city, apiKey)
        // TODO : Handle HTTP status
        val body = response.body() ?: throw Exception("Empty response")
        return body.toDomain()
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
        sunrise = sys?.sunrise?.toString() ?: "-",
        sunset = sys?.sunset?.toString() ?: "-",
        timezone = timezone?.toString() ?: "-",
        updatedAt = dt?.toString() ?: "-"
    )
}
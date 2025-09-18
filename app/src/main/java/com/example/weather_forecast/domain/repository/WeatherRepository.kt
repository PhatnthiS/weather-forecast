package com.example.weather_forecast.domain.repository

import com.example.weather_forecast.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherByCity(city: String): WeatherInfo
}
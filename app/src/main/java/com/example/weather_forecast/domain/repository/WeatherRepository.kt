package com.example.weather_forecast.domain.repository

import com.example.weather_forecast.domain.model.ForecastWeatherInfo
import com.example.weather_forecast.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherByCity(city: String): WeatherInfo
    suspend fun getForecastWeatherByCity(city: String): ForecastWeatherInfo
}
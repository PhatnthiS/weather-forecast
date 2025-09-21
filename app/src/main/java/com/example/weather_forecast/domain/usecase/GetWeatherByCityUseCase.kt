package com.example.weather_forecast.domain.usecase

import com.example.weather_forecast.domain.model.WeatherInfo
import com.example.weather_forecast.domain.repository.WeatherRepository

class GetWeatherByCityUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(city: String): WeatherInfo {
        return repository.getWeatherByCity(city)
    }
}
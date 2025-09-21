package com.example.weather_forecast.domain.usecase

import com.example.weather_forecast.domain.model.ForecastWeatherInfo
import com.example.weather_forecast.domain.repository.WeatherRepository

class GetForecastWeatherByCityUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(city: String): ForecastWeatherInfo {
        return repository.getForecastWeatherByCity(city)
    }
}
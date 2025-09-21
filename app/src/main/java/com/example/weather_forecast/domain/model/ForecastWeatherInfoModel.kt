package com.example.weather_forecast.domain.model

data class ForecastWeatherInfo(
    val items: List<ForecastItem>
)

data class ForecastItem(
    val time: String,
    val temperature: String,
    val icon: String,
    val pop: String
)
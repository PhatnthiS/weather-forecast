package com.example.weather_forecast.domain.model

data class WeatherInfo(
    val cityName: String,
    val temperature: String,
    val feelsLike: String,
    val humidity: String,
    val description: String,
    val icon: String,
    val windSpeed: String,
    val sunrise: String,
    val sunset: String,
    val timezone: String,
    val updatedAt: String
)
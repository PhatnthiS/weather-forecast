package com.example.weather_forecast.domain.model

data class ErrorInfo(
    val messageRes: Int,
    val code: Int? = null,
    val msg: String? = null
)
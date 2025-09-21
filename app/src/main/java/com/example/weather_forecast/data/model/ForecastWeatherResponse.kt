package com.example.weather_forecast.data.model

import com.google.gson.annotations.SerializedName

data class ForecastWeatherResponse(
    @SerializedName("list") val list: List<WeatherResponse>?,
    @SerializedName("city") val city: City?,

    )

data class City(
    @SerializedName("timezone") val timezone: Long?
)
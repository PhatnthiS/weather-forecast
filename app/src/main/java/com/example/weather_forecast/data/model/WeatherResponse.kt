package com.example.weather_forecast.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("main") val main: Main?,
    @SerializedName("weather") val weather: List<Weather>?,
    @SerializedName("wind") val wind: Wind?,
    @SerializedName("dt") val dt: Long?,
    @SerializedName("sys") val sys: Sys?,
    @SerializedName("timezone") val timezone: Long?,
)

data class Main(
    @SerializedName("temp") val temp: Double?,
    @SerializedName("feels_like") val feelsLike: Double?,
    @SerializedName("humidity") val humidity: Int?
)

data class Weather(
    @SerializedName("main") val main: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("icon") val icon: String?,
)

data class Wind(
    @SerializedName("speed") val speed: Double?
)

data class Sys(
    @SerializedName("country") val country : String?,
    @SerializedName("sunrise") val sunrise: Long?,
    @SerializedName("sunset") val sunset: Long?
)

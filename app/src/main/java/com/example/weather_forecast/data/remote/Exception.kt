package com.example.weather_forecast.data.remote

import com.example.weather_forecast.R

sealed class WeatherException(val resId: Int) : Exception() {
    class Unauthorized : WeatherException(R.string.invalid_api_key)
    class NotFound : WeatherException(R.string.city_not_found)
    class EmptyResponse : WeatherException(R.string.empty_response)
    class HttpError(val code: Int, val msg: String) : WeatherException(R.string.http_error)
}
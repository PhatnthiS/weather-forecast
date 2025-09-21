package com.example.weather_forecast.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_forecast.R
import com.example.weather_forecast.data.remote.WeatherException
import com.example.weather_forecast.domain.model.ErrorInfo
import com.example.weather_forecast.domain.model.ForecastWeatherInfo
import com.example.weather_forecast.domain.model.WeatherInfo
import com.example.weather_forecast.domain.usecase.GetForecastWeatherByCityUseCase
import com.example.weather_forecast.domain.usecase.GetWeatherByCityUseCase
import com.example.weather_forecast.utils.handleErrorException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

sealed class UiState {
    data object Initial : UiState()
    data object Loading : UiState()
    data class Success(val weather: WeatherInfo, val forecast: ForecastWeatherInfo) : UiState()
    data class Error(val info: ErrorInfo) : UiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase,
    private val getForecastWeatherByCityUseCase: GetForecastWeatherByCityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState

    fun fetchWeather(city: String) {
        if (city.isBlank()) {
            _uiState.value =
                UiState.Error(ErrorInfo(R.string.please_enter_a_city_name))
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val (weather, forecast) = coroutineScope {
                    val weatherDeferred = async(Dispatchers.IO) { getWeatherByCityUseCase(city) }
                    val forecastDeferred =
                        async(Dispatchers.IO) { getForecastWeatherByCityUseCase(city) }
                    weatherDeferred.await() to forecastDeferred.await()
                }
                _uiState.value = UiState.Success(weather, forecast)
            } catch (e: CancellationException) {
                throw e
            } catch (e: WeatherException) {
                _uiState.value = UiState.Error(
                    handleErrorException(e)
                )
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    ErrorInfo(R.string.unknown_error, msg = e.message)
                )
            }
        }
    }

    fun clearError() {
        if (_uiState.value is UiState.Error) {
            _uiState.value = UiState.Initial
        }
    }
}

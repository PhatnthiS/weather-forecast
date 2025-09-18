package com.example.weather_forecast.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_forecast.domain.model.WeatherInfo
import com.example.weather_forecast.domain.usecase.GetWeatherByCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {
    data object Initial : UiState()
    data object Loading : UiState()
    data class Success(val data: WeatherInfo) : UiState()
    data class Error(val message: String) : UiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState

    fun fetchWeather(city: String) {
        if (city.isBlank()) {
            _uiState.value = UiState.Error("Please enter a city name")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = getWeatherByCityUseCase.invoke(city)
                _uiState.value = UiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

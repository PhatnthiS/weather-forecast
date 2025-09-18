package com.example.weather_forecast.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weather_forecast.domain.model.WeatherInfo

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var city by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("City") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    viewModel.fetchWeather(city.trim())
                }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                viewModel.fetchWeather(city.trim())
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is UiState.Initial -> {
                Text(
                    "Enter a city name",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                val data = (uiState as UiState.Success).data
                WeatherResult(data)
            }

            is UiState.Error -> {
                val message = (uiState as UiState.Error).message
                Text("Error: $message", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun WeatherResult(data: WeatherInfo) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = data.cityName,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(8.dp))

        Text("Temperature: ${data.temperature} °C", style = MaterialTheme.typography.bodySmall)
        Text("Feels like: ${data.feelsLike} °C", style = MaterialTheme.typography.bodySmall)
        Text("Weather: ${data.description}", style = MaterialTheme.typography.bodySmall)
        Text("Humidity: ${data.humidity} %", style = MaterialTheme.typography.bodySmall)
        Text("Wind: ${data.windSpeed} m/s", style = MaterialTheme.typography.bodySmall)
        Text("Sunrise: ${data.sunrise}", style = MaterialTheme.typography.bodySmall)
        Text("Sunset: ${data.sunset}", style = MaterialTheme.typography.bodySmall)
        Text("Last update: ${data.updatedAt}", style = MaterialTheme.typography.bodySmall)
    }
}

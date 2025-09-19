package com.example.weather_forecast.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.weather_forecast.domain.model.WeatherInfo
import com.example.weather_forecast.utils.toTitleCase

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
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            HeaderRow(
                city = data.cityName,
                country = data.country,
                updatedAt = data.updatedAt
            )

            Spacer(Modifier.height(8.dp))

            TemperatureSection(
                temperature = data.temperature,
                feelsLike = data.feelsLike,
                description = data.description,
                icon = data.icon
            )

            Spacer(Modifier.height(16.dp))

            DetailsRow(
                humidity = data.humidity,
                windSpeed = data.windSpeed,
                sunrise = data.sunrise,
                sunset = data.sunset
            )
        }
    }
}

@Composable
private fun HeaderRow(city: String, country: String, updatedAt: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$city, $country",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = updatedAt,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TemperatureSection(
    temperature: String,
    feelsLike: String,
    description: String,
    icon: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "${temperature}°",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = description.toTitleCase(),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Feels like: ${feelsLike}°",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        WeatherIcon(icon)
    }
}

@Composable
private fun DetailsRow(
    humidity: String,
    windSpeed: String,
    sunrise: String,
    sunset: String
) {
    Row(

        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconAndDetails(Icons.Filled.WaterDrop, "Humidity", "$humidity %")
        CustomVerticalDivider()
        IconAndDetails(Icons.Filled.Air, "Wind", "$windSpeed km/h")
        CustomVerticalDivider()
        IconAndDetails(Icons.Filled.WbSunny, "Sunrise", sunrise)
        CustomVerticalDivider()
        IconAndDetails(Icons.Filled.WbTwilight, "Sunset", sunset)
    }
}

@Composable
fun WeatherIcon(icon: String) {
    Box(modifier = Modifier.background(Color.Transparent)) {
        AsyncImage(
            model = icon,
            contentDescription = "Weather Icon",
            modifier = Modifier.size(150.dp)
        )
    }
}

@Composable
fun IconAndDetails(icon: ImageVector, header: String, details: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = details,
            modifier = Modifier.size(24.dp)
        )
        Text(header, style = MaterialTheme.typography.bodyMedium)
        Text(details, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun CustomVerticalDivider() {
    VerticalDivider(
        modifier = Modifier
            .fillMaxHeight()
            .width(2.dp),
        color = Color.LightGray
    )
}
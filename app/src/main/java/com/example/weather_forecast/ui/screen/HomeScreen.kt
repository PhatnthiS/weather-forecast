package com.example.weather_forecast.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.weather_forecast.R
import com.example.weather_forecast.domain.model.ForecastItem
import com.example.weather_forecast.domain.model.ForecastWeatherInfo
import com.example.weather_forecast.domain.model.WeatherInfo
import com.example.weather_forecast.ui.theme.WeatherForecastTheme
import com.example.weather_forecast.utils.Constants
import com.example.weather_forecast.utils.Constants.OPEN_WEATHER_ICON_URL
import com.example.weather_forecast.utils.formatDateTime
import com.example.weather_forecast.utils.groupForecastByDay
import com.example.weather_forecast.utils.weatherInfoGradient

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var city by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.background(color = colorResource(R.color.background_light_gray))
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            viewModel.clearError()
                        }
                    },
                label = { Text(stringResource(R.string.city), style = TextStyle(Color.Gray)) },
                textStyle = TextStyle(color = Color.Black),
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

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {
                is UiState.Initial -> {
                    Text(
                        stringResource(R.string.enter_city_name),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Success -> {
                    val data = (uiState as UiState.Success)
                    WeatherResult(data.weather)
                    ForecastResult(data.forecast, data.weather.updatedAt)
                }

                is UiState.Error -> {
                    val error = (uiState as UiState.Error).info
                    Text(text = stringResource(id = error.messageRes), color = Color.Red)
                    error.code?.let { Text(text = it.toString(), color = Color.Red) }
                    error.msg?.let { Text(text = it, color = Color.Red) }
                }
            }
        }
    }
}

@Composable
private fun WeatherResult(data: WeatherInfo) {
    val isNight = data.icon.contains(Constants.NIGHT_VARIABLE)
    val gradient = weatherInfoGradient(LocalContext.current, data.description, isNight)

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.background(gradient)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
}

@Composable
private fun HeaderRow(city: String, country: String, updatedAt: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.city_country, city, country),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = updatedAt,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.End
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
                text = stringResource(R.string.temperature_unit, temperature),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(R.string.feels_like, feelsLike),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        WeatherIcon(icon, 150)
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
                color = Color.Transparent.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconAndDetails(
            Icons.Filled.WaterDrop, stringResource(R.string.humidity),
            stringResource(R.string.unit_percent, humidity)
        )
        CustomVerticalDivider()
        IconAndDetails(
            Icons.Filled.Air, stringResource(R.string.wind),
            stringResource(R.string.unit_km_h, windSpeed)
        )
        CustomVerticalDivider()
        IconAndDetails(Icons.Filled.WbSunny, stringResource(R.string.sunrise), sunrise)
        CustomVerticalDivider()
        IconAndDetails(Icons.Filled.WbTwilight, stringResource(R.string.sunset), sunset)
    }
}

@Composable
fun WeatherIcon(icon: String, size: Int) {
    AsyncImage(
        model = String.format(OPEN_WEATHER_ICON_URL, icon),
        contentDescription = stringResource(R.string.weather_icon),
        modifier = Modifier.size(size.dp)
    )
}

@Composable
fun IconAndDetails(icon: ImageVector, header: String, details: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = details,
            modifier = Modifier.size(24.dp),
            tint = Color.White
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

@Composable
fun ForecastResult(forecast: ForecastWeatherInfo, updatedTime: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        stringResource(R.string.forecast_weather),
        style = MaterialTheme.typography.headlineMedium,
        color = Color.Black
    )
    Spacer(modifier = Modifier.height(16.dp))

    val grouped = forecast.items.groupForecastByDay()
    grouped.forEach { (day, items) ->
        Column {
            Text(
                text = if (updatedTime.contains(day)) stringResource(R.string.today) else day,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
            )
            ForecastList(items)
        }

    }
}

@Composable
fun ForecastList(listForecast: List<ForecastItem>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(listForecast) { item ->
            ForecastCard(item)
        }
    }
}

@Composable
fun ForecastCard(item: ForecastItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .height(IntrinsicSize.Min),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatDateTime(item.time, stringResource(R.string.time_pattern)),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            WeatherIcon(item.icon, 60)

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.temperature_unit, item.temperature),
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.WaterDrop,
                    contentDescription = Icons.Filled.WaterDrop.name,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
                Text(
                    text = item.pop,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// Mocking preview weather result
@Composable
fun WeatherResultPreview(description: String, isNight: Boolean) {
    val gradient = weatherInfoGradient(LocalContext.current, description, isNight)
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.background(gradient)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                HeaderRow(
                    city = "Preview",
                    country = "xx",
                    updatedAt = "xxx.xxx.xxx"
                )

                Spacer(Modifier.height(8.dp))

                TemperatureSection(
                    temperature = "xx",
                    feelsLike = "xx",
                    description = "xxx",
                    icon = ""
                )

                Spacer(Modifier.height(16.dp))

                DetailsRow(
                    humidity = "xx",
                    windSpeed = "xx",
                    sunrise = "xx",
                    sunset = "xx",
                )
            }
        }
    }
}

// Previews for different weather types
@Preview(name = "Clear Day", showBackground = true)
@Composable
fun PreviewClearDay() {
    WeatherForecastTheme(darkTheme = false) {
        WeatherResultPreview(description = "clear", isNight = false)
    }
}

@Preview(name = "Clear Night", showBackground = true)
@Composable
fun PreviewClearNight() {
    WeatherForecastTheme(darkTheme = false) {
        WeatherResultPreview(description = "clear", isNight = true)
    }
}

@Preview(name = "Rain Day", showBackground = true)
@Composable
fun PreviewRainDay() {
    WeatherForecastTheme(darkTheme = false) {
        WeatherResultPreview(description = "rain", isNight = false)
    }
}

@Preview(name = "Rain Night", showBackground = true)
@Composable
fun PreviewRainNight() {
    WeatherForecastTheme(darkTheme = false) {
        WeatherResultPreview(description = "rain", isNight = true)
    }
}

@Preview(name = "Cloudy Day", showBackground = true)
@Composable
fun PreviewCloudyDay() {
    WeatherForecastTheme(darkTheme = false) {
        WeatherResultPreview(description = "cloud", isNight = false)
    }
}

@Preview(name = "Cloudy Night", showBackground = true)
@Composable
fun PreviewCloudyNight() {
    WeatherForecastTheme(darkTheme = false) {
        WeatherResultPreview(description = "cloud", isNight = true)
    }
}

@Preview(name = "xxx", showBackground = true)
@Composable
fun PreviewOthers() {
    WeatherForecastTheme(darkTheme = false) {
        WeatherResultPreview(description = "xx", isNight = true)
    }
}

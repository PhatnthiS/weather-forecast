# WeatherForecast App

A simple Android application built with **Kotlin** and **Jetpack Compose** that fetches and displays current weather information for a given city.  
The app integrates with the **OpenWeatherMap API** to retrieve live weather data.

---

## 📱 Features
- Enter a city name to search for its weather.
- Displays temperature, description, humidity, wind, sunrise/sunset times, and forecast for the next 3 hours up to 5 days.
- Handles **loading** and **error states** (invalid city or network issues).
- Built using **Jetpack Compose** for UI.
- Uses **Kotlin Coroutines** and **StateFlow** for async and reactive UI updates.
- Follows **MVVM architecture** and **Clean Architecture principles** for better separation of concerns and maintainability.

---

## 🛠️ Tech Stack
- **Kotlin**
- **Jetpack Compose**
- **StateFlow / Flow**
- **Coroutines**
- **Retrofit** (API calls)
- **Gson** (JSON parsing)
- **Coil** (image loading)
- **Hilt** (dependency injection)

---

## 🚀 Setup & Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/weather-forecast.git
   cd weather-forecast
2. Open the project in Android Studio:
- Launch Android Studio
- Click File > Open
- Select the weather-forecast folder you just cloned

3. Run the app:
- Let Android Studio sync the Gradle files
- Connect a device or start an emulator
- Click the Run ▶️ button

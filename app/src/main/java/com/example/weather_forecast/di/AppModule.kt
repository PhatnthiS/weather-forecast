package com.example.weather_forecast.di

import android.util.Log
import com.example.weather_forecast.data.remote.ApiService
import com.example.weather_forecast.data.repository.WeatherRepositoryImpl
import com.example.weather_forecast.domain.repository.WeatherRepository
import com.example.weather_forecast.domain.usecase.GetForecastWeatherByCityUseCase
import com.example.weather_forecast.domain.usecase.GetWeatherByCityUseCase
import com.example.weather_forecast.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        val logging = HttpLoggingInterceptor { message ->
            Log.d("API-JSON", message) }
            .apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.OPEN_WEATHER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(api: ApiService): WeatherRepository {
        return WeatherRepositoryImpl(api, Constants.OPEN_WEATHER_API_KEY)
    }

    @Provides
    @Singleton
    fun provideGetWeatherByCityUseCase(
        repository: WeatherRepository
    ): GetWeatherByCityUseCase {
        return GetWeatherByCityUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetForecastWeatherByCityUseCase(
        repository: WeatherRepository
    ): GetForecastWeatherByCityUseCase {
        return GetForecastWeatherByCityUseCase(repository)
    }
}
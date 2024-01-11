package com.Metoid.network

import com.Metoid.models.WeatherResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

suspend fun fetchWeatherData(city: String): WeatherResponse { // Changez WeatherData par WeatherResponse
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    val apiKey = "fabc8b6bf59fcd628dac7eeae077aae4"
    val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&lang=fr&appid=$apiKey"

    return httpClient.get(url).body()
}
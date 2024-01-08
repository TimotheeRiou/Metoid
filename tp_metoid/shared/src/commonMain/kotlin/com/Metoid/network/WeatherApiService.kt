package com.Metoid.network

import com.Metoid.models.WeatherData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

suspend fun fetchWeatherData(city: String): WeatherData {
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Cette ligne permet d'ignorer les clés JSON inconnues
            })
        }
    }

    val apiKey = "fabc8b6bf59fcd628dac7eeae077aae4"
    val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey"

    return httpClient.get(url).body()
}

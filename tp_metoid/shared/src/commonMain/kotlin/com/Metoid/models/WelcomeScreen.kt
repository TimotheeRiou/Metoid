package com.Metoid.models

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Metoid.network.fetchWeatherData
import kotlinx.coroutines.launch

@Composable
internal fun WelcomeScreen() {
    var searchText by remember { mutableStateOf("") }
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }
    var favorites by remember { mutableStateOf(listOf<String>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val white = Color(0xFFFFFFFF)
    val customGreen = Color(0xFF4F6F52)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = white
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Text(
                "Metoid",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = customGreen
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "La meilleure application météo",
                fontSize = 20.sp,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Rechercher une ville") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        errorMessage = null
                        try {
                            weatherData = fetchWeatherData(searchText)
                        } catch (e: Exception) {
                            errorMessage = "Erreur lors de la récupération des données"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(backgroundColor = customGreen)
            ) {
                Text("Recherche", color = white)
            }
            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                CircularProgressIndicator()
            }

            weatherData?.let { data ->
                DisplayWeatherData(data)

                Button(
                    onClick = {
                        if (data.name in favorites) {
                            favorites = favorites - data.name
                        } else {
                            favorites = favorites + data.name
                        }
                    },
                    shape = RoundedCornerShape(20),
                    colors = ButtonDefaults.buttonColors(backgroundColor = customGreen)
                ) {
                    Text(
                        if (data.name in favorites) "Retirer des favoris" else "Ajouter aux favoris",
                        color = white
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Villes favorites :", fontWeight = FontWeight.Bold)
            favorites.forEach { city ->
                Text(
                    city,
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                isLoading = true
                                errorMessage = null
                                try {
                                    weatherData = fetchWeatherData(city)
                                    searchText = city
                                } catch (e: Exception) {
                                    errorMessage = "Erreur lors de la récupération des données"
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                )
            }

            errorMessage?.let {
                Text(it)
            }
        }
    }
}

@Composable
fun DisplayWeatherData(data: WeatherResponse) {
    Text(buildAnnotatedString {
        append("Météo pour ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("${data.name}, ${data.sys.country}")
        }
    })

    Spacer(modifier = Modifier.height(20.dp))

    Text(buildAnnotatedString {
        append("Température : ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("${data.main.temp.toInt()}°C")
        }
    })

    Text(buildAnnotatedString {
        append("Température ressentie : ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("${data.main.feels_like.toInt()}°C")
        }
    })

    Spacer(modifier = Modifier.height(20.dp))

    Text(buildAnnotatedString {
        append("Humidité : ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("${data.main.humidity}%")
        }
    })

    Spacer(modifier = Modifier.height(20.dp))

    Text(buildAnnotatedString {
        append("Description : ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(data.weather[0].description)
        }
    })
}

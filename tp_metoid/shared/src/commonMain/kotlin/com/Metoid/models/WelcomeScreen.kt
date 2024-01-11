package com.Metoid.models

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import com.Metoid.network.fetchWeatherData
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.launch


@Composable
internal fun WelcomeScreen() {
    var searchText by remember { mutableStateOf("") }
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }
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
                        isLoading = true // Commence le chargement
                        errorMessage = null
                        try {
                            weatherData = fetchWeatherData(searchText)
                        } catch (e: Exception) {
                            errorMessage =
                                "Erreur lors de la récupération des données"
                            //"Erreur lors de la récupération des données: ${e.message}"
                        } finally {
                            isLoading = false // Termine le chargement
                        }
                    }
                },
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(backgroundColor = customGreen)
            ) {
                Text(
                    "Recherche",
                    color = white
                )
            }
            Spacer(modifier = Modifier.height(20.dp))


            if (isLoading) {
                CircularProgressIndicator()
            }

            weatherData?.let { data ->
                Text(buildAnnotatedString {
                    append("Météo pour ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${data.name}, ${data.sys.country}")
                    }
                })

                Text(buildAnnotatedString {
                    append("Température : ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${data.main.temp.toInt()}°C")
                    }
                })

                Text(buildAnnotatedString {
                    append("Humidité : ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${data.main.humidity}%")
                    }
                })

                Text(buildAnnotatedString {
                    append("Description : ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(data.weather[0].description)
                    }
                })
            }


            errorMessage?.let {
                Text(it)
            }
        }
    }
}


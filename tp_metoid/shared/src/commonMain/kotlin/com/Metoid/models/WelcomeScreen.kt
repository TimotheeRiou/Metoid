package com.Metoid.models

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var isClicked by remember { mutableStateOf(false) }
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
                //bouton pour Ajouter à mes favoris
                Button(
                    onClick = {
                        coroutineScope.launch {
                            isClicked = !isClicked // Toggle the isClicked state
                            isLoading = true
                            errorMessage = null
                            try {
                                weatherData = fetchWeatherData(searchText)
                            } catch (e: Exception) {
                                errorMessage =
                                    "Erreur lors de la récupération des données"
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    shape = RoundedCornerShape(20),
                    colors = ButtonDefaults.buttonColors(backgroundColor = customGreen)
                ) {
                    Text(
                        if (isClicked) "Retirer des favoris" else "Ajouter aux favoris",
                        color = white
                    )
                }
                //TODO afficher une liste de ville favorite
            }


            errorMessage?.let {
                Text(it)
            }
        }
    }
}


package com.Metoid.models

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
import androidx.compose.ui.text.input.ImeAction
import com.Metoid.network.fetchWeatherData
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.launch

@Composable
internal fun WelcomeScreen() {
    var searchText by remember { mutableStateOf("") }
    var weatherData by remember { mutableStateOf<WeatherData?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp)) // Espacement en haut

        Text(
            "Metoid",
            fontSize = 30.sp,
        )
        Spacer(modifier = Modifier.height(10.dp)) // Espacement entre le titre et le sous-titre

        Text(
            "La meilleure application météo",
            fontSize = 20.sp,
        )
        Spacer(modifier = Modifier.height(20.dp)) // Espacement entre le sous-titre et la barre de recherche

        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Rechercher une ville") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp)) // Espacement entre la barre de recherche et le bouton

        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    errorMessage = null
                    try {
                        weatherData = fetchWeatherData(searchText)
                    } catch (e: ClientRequestException) {
                        errorMessage = "Erreur lors de la récupération des données"
                    } finally {
                        isLoading = false
                    }
                }
            }
        ) {
            Text("Recherche")
        }

        if (isLoading) {
            CircularProgressIndicator()
        }

        weatherData?.let { data ->
            // Afficher les données météo ici
            Text("Météo pour ${data.name}")
        }

        errorMessage?.let {
            Text(it)
        }
    }
}
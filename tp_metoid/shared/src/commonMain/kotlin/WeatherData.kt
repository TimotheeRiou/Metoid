package com.Metoid.models

import kotlinx.serialization.Serializable

@Serializable
data class WeatherData(
    val coord: Coord,
    val name: String,
    // ... autres champs ...
)

@Serializable
data class Coord(
    val lon: Double,
    val lat: Double
)


package com.a65apps.core.entities

data class Location(
        val uid: String,
        val personId: String,
        val address: String,
        val longitude: Double,
        val latitude: Double
)
package com.example.hellokitty.domain.models

data class CatBreed(
    val id: String,
    val name: String,
    val weight: String,
    val lifespan: String,
    val origin: String,
    val temperament: String,
    val description: String,
    val refImageId: String?,
    val stats : Map<String, Int>
)

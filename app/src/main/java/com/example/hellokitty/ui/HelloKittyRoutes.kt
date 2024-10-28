package com.example.hellokitty.ui

import kotlinx.serialization.Serializable


@Serializable
object CatBreedsList

@Serializable
data class BreedDetail(val id: String)
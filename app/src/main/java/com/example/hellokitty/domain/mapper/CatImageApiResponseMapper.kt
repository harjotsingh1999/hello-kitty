package com.example.hellokitty.domain.mapper

import com.example.hellokitty.data.models.CatImageApiResponse
import com.example.hellokitty.domain.models.CatImage


fun CatImageApiResponse.mapToDomain() = CatImage(
    id = id ?: error("Cat image id is null"),
    url = url ?: error("Cat image url is null")
)
package com.example.hellokitty.ui.mapper

import com.example.hellokitty.domain.models.CatBreed
import com.example.hellokitty.ui.interactor.CatBreedDetailUiModel

fun CatBreed.mapToBreedDetailUi() = CatBreedDetailUiModel(
    id = id,
    name = name,
    description = description,
    stats = stats
)
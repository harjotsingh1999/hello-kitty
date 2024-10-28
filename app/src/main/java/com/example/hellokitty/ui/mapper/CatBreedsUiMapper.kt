package com.example.hellokitty.ui.mapper

import com.example.hellokitty.domain.models.CatBreed
import com.example.hellokitty.ui.interactor.CatBreedUiModel


fun CatBreed.mapToUi() = CatBreedUiModel(
    id = id,
    name = name,
    weight = "$weight kg",
    lifespan = "$lifespan yrs",
    origin = origin,
    temperament = temperament.replace(" ", "").split(",").take(5),
    image = if(refImageId == null) CatBreedUiModel.CatUiImage.Error else CatBreedUiModel.CatUiImage.Loading,
    data = this
)
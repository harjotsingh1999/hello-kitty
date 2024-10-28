package com.example.hellokitty.ui.interactor

import com.example.hellokitty.domain.models.CatBreed

sealed class CatBreedsListUiState {
    data object Loading : CatBreedsListUiState()
    data class Success(val data: CatBreedsListUiData) : CatBreedsListUiState()
    data class Error(val message: String) : CatBreedsListUiState()
}

data class CatBreedsListUiData(
    val catBreeds: List<CatBreedUiModel>
)

data class CatBreedUiModel(
    val id: String,
    val name: String,
    val weight: String,
    val lifespan: String,
    val origin: String,
    val temperament: List<String>,
    val image: CatUiImage,
    val data: CatBreed
) {
    sealed class CatUiImage {
        data object Loading : CatUiImage()
        data object Error : CatUiImage()
        data class Success(val url: String) : CatUiImage()
    }
}

sealed class CatBreedsListSideEffects {
    data class OpenCatBreedDetails(val catBreedId: String) : CatBreedsListSideEffects()
}

sealed class CatBreedsListEvent {
    data class OnCatBreedClick(val breed: CatBreed) : CatBreedsListEvent()
    data object OnErrorReloadClick : CatBreedsListEvent()
    data object OnScrollToEnd : CatBreedsListEvent()
}
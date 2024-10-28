package com.example.hellokitty.ui.interactor

sealed class CatBreedDetailUiState {
    data object Initial : CatBreedDetailUiState()
    data class Success(val data: CatBreedDetailUiData) : CatBreedDetailUiState()
}

data class CatBreedDetailUiData(
    val breedDetails: CatBreedDetailUiModel,
    val images: CatBreedImages
)

data class CatBreedDetailUiModel(
    val id: String,
    val name: String,
    val description: String,
    val stats: Map<String, Int>
)

sealed class CatBreedImages {
    data object Loading : CatBreedImages()
    data object Error : CatBreedImages()
    data class Success(val data: List<String>) : CatBreedImages()
}

sealed class BreedDetailEvent {
    data object OnErrorReloadClick : BreedDetailEvent()
}
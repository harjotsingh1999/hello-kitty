package com.example.hellokitty.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hellokitty.data.models.ApiResult
import com.example.hellokitty.domain.repository.BreedDataRepository
import com.example.hellokitty.domain.repository.HelloKittyRepository
import com.example.hellokitty.ui.interactor.BreedDetailEvent
import com.example.hellokitty.ui.interactor.CatBreedDetailUiData
import com.example.hellokitty.ui.interactor.CatBreedDetailUiState
import com.example.hellokitty.ui.interactor.CatBreedImages
import com.example.hellokitty.ui.mapper.mapToBreedDetailUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CatBreedDetailViewModel @Inject constructor(
    private val dataRepository: BreedDataRepository,
    private val helloKittyRepository: HelloKittyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CatBreedDetailUiState>(CatBreedDetailUiState.Initial)
    val uiState = _uiState.asStateFlow()

    init {
        fetchBreedDetail()
    }

    private fun fetchBreedDetail() {
        val breedData = dataRepository.getSelectedBreedData()
        _uiState.value = CatBreedDetailUiState.Success(
            CatBreedDetailUiData(
                breedDetails = breedData.mapToBreedDetailUi(),
                images = CatBreedImages.Loading
            )
        )
        fetchBreedImages(breedData.id)
    }

    private fun getUiStateSuccessData() = (_uiState.value as? CatBreedDetailUiState.Success)?.data

    private fun fetchBreedImages(id: String) {
        viewModelScope.launch {
            val result = helloKittyRepository.getBreedImages(id)
            getUiStateSuccessData()?.let {
                val updatedState = when (result) {
                    is ApiResult.Error -> it.copy(images = CatBreedImages.Error)
                    is ApiResult.Success -> it.copy(images = CatBreedImages.Success(result.data.map { image -> image.url }))
                }
                _uiState.update { CatBreedDetailUiState.Success(updatedState) }
            }
        }
    }

    fun handleEvent(event: BreedDetailEvent) {
        when (event) {
            BreedDetailEvent.OnErrorReloadClick -> fetchBreedDetail()
        }
    }
}
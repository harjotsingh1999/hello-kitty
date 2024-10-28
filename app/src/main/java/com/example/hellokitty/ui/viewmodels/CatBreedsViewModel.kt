package com.example.hellokitty.ui.viewmodels

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hellokitty.data.models.ApiResult
import com.example.hellokitty.domain.models.CatBreed
import com.example.hellokitty.domain.models.CatImage
import com.example.hellokitty.domain.repository.BreedDataRepository
import com.example.hellokitty.domain.usecase.GetCatBreedsPaginatedUseCase
import com.example.hellokitty.domain.usecase.GetCatImageUsecase
import com.example.hellokitty.ui.interactor.CatBreedUiModel
import com.example.hellokitty.ui.interactor.CatBreedsListEvent
import com.example.hellokitty.ui.interactor.CatBreedsListSideEffects
import com.example.hellokitty.ui.interactor.CatBreedsListUiData
import com.example.hellokitty.ui.interactor.CatBreedsListUiState
import com.example.hellokitty.ui.mapper.mapToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CatBreedsViewModel @Inject constructor(
    private val getCatBreedsPaginatedUseCase: GetCatBreedsPaginatedUseCase,
    private val getCatImageUsecase: GetCatImageUsecase,
    private val breedDataRepository: BreedDataRepository
) :
    ViewModel() {

    private val _uiState = MutableStateFlow<CatBreedsListUiState>(CatBreedsListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _sideEffects = MutableSharedFlow<CatBreedsListSideEffects>()
    val sideEffects = _sideEffects.asSharedFlow()

    init {
        fetchCatBreeds()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchCatBreeds() {
        if (getCatBreedsPaginatedUseCase.dataAvailable) {
            viewModelScope.launch {
                when (val result = getCatBreedsPaginatedUseCase()) {
                    is ApiResult.Success -> {
                        getUiStateSuccessData()?.let { uiData ->
                            _uiState.update {
                                CatBreedsListUiState.Success(uiData.copy(catBreeds = uiData.catBreeds + result.data.map { it.mapToUi() }))
                            }
                        } ?: run {
                            _uiState.update {
                                CatBreedsListUiState.Success(CatBreedsListUiData(catBreeds = result.data.map { it.mapToUi() }))
                            }
                        }
                        fetchCatImages(result.data)
                    }

                    is ApiResult.Error -> {
                        if (getUiStateSuccessData() == null) {
                            _uiState.update { CatBreedsListUiState.Error(result.message) }
                        }
                    }
                }
            }
        }
    }

    private fun getUiStateSuccessData(): CatBreedsListUiData? {
        return (_uiState.value as? CatBreedsListUiState.Success)?.data
    }

    private fun fetchCatImages(catBreeds: List<CatBreed>) {
        viewModelScope.launch {
            val idToImageMap = getCatImageUsecase.invoke(catBreeds.map { it.refImageId })
                .filterIsInstance<ApiResult.Success<CatImage>>()
                .map { it.data }
                .associateBy { it.id }
            val catBreedIdToImageIdMap = catBreeds.associate { it.id to it.refImageId }
            getUiStateSuccessData()?.let {
                val updatedState = it.copy(catBreeds = it.catBreeds.map { catBreed ->
                    if (catBreedIdToImageIdMap.containsKey(catBreed.id)) {
                        catBreed.copy(
                            image = if (idToImageMap.containsKey(catBreedIdToImageIdMap[catBreed.id])) {
                                CatBreedUiModel.CatUiImage.Success(
                                    idToImageMap[catBreedIdToImageIdMap[catBreed.id]]!!.url
                                )
                            } else {
                                CatBreedUiModel.CatUiImage.Error
                            }
                        )
                    } else catBreed
                })
                _uiState.update { CatBreedsListUiState.Success(updatedState) }
            }
        }
    }

    fun handleEvent(event: CatBreedsListEvent) {
        when (event) {
            is CatBreedsListEvent.OnCatBreedClick -> {
                breedDataRepository.setSelectedBreedData(event.breed)
                emitSideEffect(
                    CatBreedsListSideEffects.OpenCatBreedDetails(
                        event.breed.id
                    )
                )
            }

            CatBreedsListEvent.OnErrorReloadClick -> {
                _uiState.update { CatBreedsListUiState.Loading }
                getCatBreedsPaginatedUseCase.reset()
                fetchCatBreeds()
            }

            CatBreedsListEvent.OnScrollToEnd -> fetchCatBreeds()
        }
    }

    private fun emitSideEffect(sideEffect: CatBreedsListSideEffects) {
        viewModelScope.launch {
            _sideEffects.emit(sideEffect)
        }
    }
}
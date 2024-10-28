package com.example.hellokitty.domain.repository

import com.example.hellokitty.domain.models.CatBreed
import javax.inject.Inject

interface BreedDataRepository {
    fun setSelectedBreedData(catBreed: CatBreed)
    fun getSelectedBreedData(): CatBreed
}

class BreedDataRepositoryImpl @Inject constructor(): BreedDataRepository {
    private lateinit var selectedBreed: CatBreed

    override fun setSelectedBreedData(catBreed: CatBreed) {
        selectedBreed = catBreed
    }

    override fun getSelectedBreedData(): CatBreed {
        return selectedBreed
    }
}
package com.example.hellokitty.domain.usecase

import com.example.hellokitty.data.models.ApiResult
import com.example.hellokitty.domain.models.CatBreed
import com.example.hellokitty.domain.repository.HelloKittyRepository
import javax.inject.Inject

class GetCatBreedsPaginatedUseCase @Inject constructor(
    private val repository: HelloKittyRepository
) {
    companion object {
        private const val LIMIT = 10
    }

    var dataAvailable: Boolean = true
        private set

    private var page = 0

    suspend operator fun invoke(): ApiResult<List<CatBreed>> {
        val result = repository.getCatBreedsPaginated(page, LIMIT)
        if (result is ApiResult.Success) {
            dataAvailable = result.data.isNotEmpty()
            page++
        } else {
            dataAvailable = false
        }
        return result
    }

    fun reset() {
        page = 0
        dataAvailable = true
    }
}
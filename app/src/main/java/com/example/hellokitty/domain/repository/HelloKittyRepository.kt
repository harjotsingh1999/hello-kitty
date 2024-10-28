package com.example.hellokitty.domain.repository

import com.example.hellokitty.data.models.ApiResult
import com.example.hellokitty.data.network.HelloKittyApiService
import com.example.hellokitty.domain.mapper.mapToDomain
import com.example.hellokitty.domain.models.CatBreed
import com.example.hellokitty.domain.models.CatImage

interface HelloKittyRepository {
    suspend fun getCatBreedsPaginated(page: Int, limit: Int): ApiResult<List<CatBreed>>
    suspend fun getCatImage(id: String): ApiResult<CatImage>
    suspend fun getBreedImages(breedId: String): ApiResult<List<CatImage>>
}

class HelloKittyRepositoryImpl(private val api: HelloKittyApiService) : HelloKittyRepository {

    override suspend fun getCatBreedsPaginated(page: Int, limit: Int): ApiResult<List<CatBreed>> {
        try {
            val response = api.getCatBreeds(limit, page)
            if (response.isSuccessful && response.body() != null) {
                val catBreeds = response.body()!!.mapToDomain()
                return ApiResult.Success(catBreeds)
            } else {
                return ApiResult.Error("Unknown error")
            }
        } catch (e: Exception) {
            return ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getCatImage(id: String): ApiResult<CatImage> {
        try {
            val response = api.getCatImageFromId(id)
            if (response.isSuccessful && response.body() != null) {
                val catImage = response.body()!!.mapToDomain()
                return ApiResult.Success(catImage)
            } else {
                return ApiResult.Error("Unknown error")
            }
        } catch (e: Exception) {
            return ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getBreedImages(breedId: String): ApiResult<List<CatImage>> {
        try {
            val response = api.searchCatImages(breedId, 9)
            if (response.isSuccessful && response.body() != null) {
                val images = response.body()!!.map { it.mapToDomain() }
                return ApiResult.Success(images)
            } else {
                return ApiResult.Error("Unknown error")
            }
        } catch (e: Exception) {
            return ApiResult.Error(e.message ?: "Unknown error")
        }
    }
}
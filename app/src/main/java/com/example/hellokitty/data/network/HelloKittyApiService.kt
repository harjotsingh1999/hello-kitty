package com.example.hellokitty.data.network

import com.example.hellokitty.data.models.CatBreedsApiModel
import com.example.hellokitty.data.models.CatImageApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HelloKittyApiService {

    companion object {
        const val BASE_URL = "https://api.thecatapi.com/v1/"
        const val ENDPOINT_GET_CAT_BREEDS = "breeds"
        const val ENDPOINT_GET_CAT_IMAGE = "images/{refImageId}"
        const val ENDPOINT_SEARCH_CAT_IMAGES = "images/search"
    }

    @GET(ENDPOINT_GET_CAT_BREEDS)
    suspend fun getCatBreeds(@Query("limit") limit : Int, @Query("page") page : Int) : Response<List<CatBreedsApiModel>>

    @GET(ENDPOINT_GET_CAT_IMAGE)
    suspend fun getCatImageFromId(@Path("refImageId") id : String) : Response<CatImageApiResponse>

    @GET(ENDPOINT_SEARCH_CAT_IMAGES)
    suspend fun searchCatImages(@Query("breed_ids") breedId: String, @Query("limit") limit: Int) : Response<List<CatImageApiResponse>>
}
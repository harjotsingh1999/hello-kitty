package com.example.hellokitty.data.models

import com.google.gson.annotations.SerializedName

data class CatImageApiResponse(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("width")
    val width: Int? = null,
    @SerializedName("height")
    val height: Int? = null,
    @SerializedName("breeds")
    val breeds: List<CatBreedsApiModel>? = null,
)
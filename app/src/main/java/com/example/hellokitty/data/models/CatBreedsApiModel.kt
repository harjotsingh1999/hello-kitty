package com.example.hellokitty.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class WeightApiModel(

    @SerializedName("metric")
    val metric: String? = null,

    @SerializedName("imperial")
    val imperial: String? = null
) : Parcelable

@Parcelize
data class CatBreedsApiModel(

    @SerializedName("suppressed_tail")
    val suppressedTail: Int? = null,

    @SerializedName("wikipedia_url")
    val wikipediaUrl: String? = null,

    @SerializedName("origin")
    val origin: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("experimental")
    val experimental: Int? = null,

    @SerializedName("life_span")
    val lifeSpan: String? = null,

    @SerializedName("rare")
    val rare: Int? = null,

    @SerializedName("lap")
    val lap: Int? = null,

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("short_legs")
    val shortLegs: Int? = null,

    @SerializedName("shedding_level")
    val sheddingLevel: Int? = null,

    @SerializedName("dog_friendly")
    val dogFriendly: Int? = null,

    @SerializedName("natural")
    val natural: Int? = null,

    @SerializedName("rex")
    val rex: Int? = null,

    @SerializedName("health_issues")
    val healthIssues: Int? = null,

    @SerializedName("hairless")
    val hairless: Int? = null,

    @SerializedName("weight")
    val weight: WeightApiModel? = null,

    @SerializedName("alt_names")
    val altNames: String? = null,

    @SerializedName("adaptability")
    val adaptability: Int? = null,

    @SerializedName("vocalisation")
    val vocalisation: Int? = null,

    @SerializedName("intelligence")
    val intelligence: Int? = null,

    @SerializedName("social_needs")
    val socialNeeds: Int? = null,

    @SerializedName("child_friendly")
    val childFriendly: Int? = null,

    @SerializedName("temperament")
    val temperament: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("grooming")
    val grooming: Int? = null,

    @SerializedName("hypoallergenic")
    val hypoallergenic: Int? = null,

    @SerializedName("indoor")
    val indoor: Int? = null,

    @SerializedName("energy_level")
    val energyLevel: Int? = null,

    @SerializedName("stranger_friendly")
    val strangerFriendly: Int? = null,

    @SerializedName("reference_image_id")
    val referenceImageId: String? = null,

    @SerializedName("affection_level")
    val affectionLevel: Int? = null
) : Parcelable

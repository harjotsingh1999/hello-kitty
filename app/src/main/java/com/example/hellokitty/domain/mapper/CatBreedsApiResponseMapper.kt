package com.example.hellokitty.domain.mapper

import com.example.hellokitty.data.models.CatBreedsApiModel
import com.example.hellokitty.domain.models.CatBreed


fun List<CatBreedsApiModel>.mapToDomain() =
    this.map {
        CatBreed(
            id = it.id ?: error("Cat breed id is null"),
            name = it.name ?: error("Cat breed name is null"),
            weight = it.weight?.metric.orEmpty(),
            lifespan = it.lifeSpan.orEmpty(),
            origin = it.origin.orEmpty(),
            temperament = it.temperament.orEmpty(),
            refImageId = it.referenceImageId,
            description = it.description.orEmpty(),
            stats = mapOf(
                "Experimental" to (it.experimental ?: 0),
                "Rare" to (it.rare ?: 0),
                "Lap" to (it.lap ?: 0),
                "Short Legs" to (it.shortLegs ?: 0),
                "Shedding Level" to (it.sheddingLevel ?: 0),
                "Dog Friendly" to (it.dogFriendly ?: 0),
                "Natural" to (it.natural ?: 0),
                "Health Issues" to (it.healthIssues ?: 0),
                "Hairless" to (it.hairless ?: 0),
                "Adaptability" to (it.adaptability ?: 0),
                "Vocalisation" to (it.vocalisation ?: 0),
                "Intelligence" to (it.intelligence ?: 0),
                "Social Needs" to (it.socialNeeds ?: 0),
                "Child Friendly" to (it.childFriendly ?: 0),
                "Grooming" to (it.grooming ?: 0),
                "Hypoallergenic" to (it.hypoallergenic ?: 0),
                "Energy Level" to (it.energyLevel ?: 0),
                "Stranger Friendly" to (it.strangerFriendly ?: 0),
                "Affection Level" to (it.affectionLevel ?: 0),
            )
        )
    }
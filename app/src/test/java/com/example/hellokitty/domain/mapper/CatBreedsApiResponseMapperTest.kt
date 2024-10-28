package com.example.hellokitty.domain.mapper

import com.example.hellokitty.data.models.CatBreedsApiModel
import com.example.hellokitty.data.models.WeightApiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class CatBreedsApiModelMappingTest {

    @Test
    fun `mapToDomain should map all fields correctly`() {
        // Arrange
        val apiModel = CatBreedsApiModel(
            id = "1",
            name = "British Shorthair",
            weight = WeightApiModel("4 - 7 kg"),
            lifeSpan = "12 - 20 years",
            origin = "United Kingdom",
            temperament = "Calm, Friendly",
            referenceImageId = "image123",
            description = "A calm and friendly breed.",
            experimental = 1,
            rare = 0,
            lap = 1,
            shortLegs = 0,
            sheddingLevel = 3,
            dogFriendly = 4,
            natural = 1,
            healthIssues = 2,
            hairless = 0,
            adaptability = 5,
            vocalisation = 2,
            intelligence = 3,
            socialNeeds = 4,
            childFriendly = 5,
            grooming = 2,
            hypoallergenic = 1,
            energyLevel = 3,
            strangerFriendly = 4,
            affectionLevel = 5
        )

        // Act
        val domainModel = listOf(apiModel).mapToDomain().first()

        // Assert
        assertEquals("1", domainModel.id)
        assertEquals("British Shorthair", domainModel.name)
        assertEquals("4 - 7 kg", domainModel.weight)
        assertEquals("12 - 20 years", domainModel.lifespan)
        assertEquals("United Kingdom", domainModel.origin)
        assertEquals("Calm, Friendly", domainModel.temperament)
        assertEquals("image123", domainModel.refImageId)
        assertEquals("A calm and friendly breed.", domainModel.description)
        assertEquals(1, domainModel.stats["Experimental"])
        assertEquals(0, domainModel.stats["Rare"])
        assertEquals(1, domainModel.stats["Lap"])
        assertEquals(0, domainModel.stats["Short Legs"])
        assertEquals(3, domainModel.stats["Shedding Level"])
        assertEquals(4, domainModel.stats["Dog Friendly"])
        assertEquals(1, domainModel.stats["Natural"])
        assertEquals(2, domainModel.stats["Health Issues"])
        assertEquals(0, domainModel.stats["Hairless"])
        assertEquals(5, domainModel.stats["Adaptability"])
        assertEquals(2, domainModel.stats["Vocalisation"])
        assertEquals(3, domainModel.stats["Intelligence"])
        assertEquals(4, domainModel.stats["Social Needs"])
        assertEquals(5, domainModel.stats["Child Friendly"])
        assertEquals(2, domainModel.stats["Grooming"])
        assertEquals(1, domainModel.stats["Hypoallergenic"])
        assertEquals(3, domainModel.stats["Energy Level"])
        assertEquals(4, domainModel.stats["Stranger Friendly"])
        assertEquals(5, domainModel.stats["Affection Level"])
    }

    @Test
    fun `mapToDomain should throw error when id is null`() {
        // Arrange
        val apiModel = CatBreedsApiModel(
            id = null, // id is null
            name = "British Shorthair",
            weight = WeightApiModel("4 - 7 kg"),
            lifeSpan = "12 - 20 years",
            origin = "United Kingdom",
            temperament = "Calm, Friendly",
            referenceImageId = "image123",
            description = "A calm and friendly breed."
        )

        // Act & Assert
        assertThrows(IllegalStateException::class.java) {
            listOf(apiModel).mapToDomain()
        }
    }

    @Test
    fun `mapToDomain should throw error when name is null`() {
        // Arrange
        val apiModel = CatBreedsApiModel(
            id = "1",
            name = null, // name is null
            weight = WeightApiModel("4 - 7 kg"),
            lifeSpan = "12 - 20 years",
            origin = "United Kingdom",
            temperament = "Calm, Friendly",
            referenceImageId = "image123",
            description = "A calm and friendly breed."
        )

        // Act & Assert
        assertThrows(IllegalStateException::class.java) {
            listOf(apiModel).mapToDomain()
        }
    }

    @Test
    fun `mapToDomain should map default values for nullable fields`() {
        // Arrange
        val apiModel = CatBreedsApiModel(
            id = "1",
            name = "British Shorthair",
            weight = null, // weight is null
            lifeSpan = null, // lifespan is null
            origin = null, // origin is null
            temperament = null, // temperament is null
            referenceImageId = null, // referenceImageId is null
            description = null, // description is null
            experimental = null,
            rare = null,
            lap = null,
            shortLegs = null,
            sheddingLevel = null,
            dogFriendly = null,
            natural = null,
            healthIssues = null,
            hairless = null,
            adaptability = null,
            vocalisation = null,
            intelligence = null,
            socialNeeds = null,
            childFriendly = null,
            grooming = null,
            hypoallergenic = null,
            energyLevel = null,
            strangerFriendly = null,
            affectionLevel = null
        )

        // Act
        val domainModel = listOf(apiModel).mapToDomain().first()

        // Assert
        assertEquals("1", domainModel.id)
        assertEquals("British Shorthair", domainModel.name)
        assertEquals("", domainModel.weight) // Default value
        assertEquals("", domainModel.lifespan) // Default value
        assertEquals("", domainModel.origin) // Default value
        assertEquals("", domainModel.temperament) // Default value
        assertNull(domainModel.refImageId) // Null is allowed
        assertEquals("", domainModel.description) // Default value
        assertEquals(0, domainModel.stats["Experimental"])
        assertEquals(0, domainModel.stats["Rare"])
        assertEquals(0, domainModel.stats["Lap"])
        assertEquals(0, domainModel.stats["Short Legs"])
        assertEquals(0, domainModel.stats["Shedding Level"])
        assertEquals(0, domainModel.stats["Dog Friendly"])
        assertEquals(0, domainModel.stats["Natural"])
        assertEquals(0, domainModel.stats["Health Issues"])
        assertEquals(0, domainModel.stats["Hairless"])
        assertEquals(0, domainModel.stats["Adaptability"])
        assertEquals(0, domainModel.stats["Vocalisation"])
        assertEquals(0, domainModel.stats["Intelligence"])
        assertEquals(0, domainModel.stats["Social Needs"])
        assertEquals(0, domainModel.stats["Child Friendly"])
        assertEquals(0, domainModel.stats["Grooming"])
        assertEquals(0, domainModel.stats["Hypoallergenic"])
        assertEquals(0, domainModel.stats["Energy Level"])
        assertEquals(0, domainModel.stats["Stranger Friendly"])
        assertEquals(0, domainModel.stats["Affection Level"])
    }
}

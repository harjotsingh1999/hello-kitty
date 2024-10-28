package com.example.hellokitty.domain.repository

import com.example.hellokitty.domain.models.CatBreed
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class BreedDataRepositoryImplTest {

    private lateinit var repository: BreedDataRepositoryImpl

    @Before
    fun setUp() {
        repository = BreedDataRepositoryImpl()
    }

    @Test
    fun `setSelectedBreedData should store the provided CatBreed`() {
        // Arrange
        val catBreed = CatBreed(
            id = "1",
            name = "Siberian",
            weight = "4 - 7 kg",
            lifespan = "12 - 15 years",
            origin = "Russia",
            temperament = "Friendly",
            refImageId = "image123",
            description = "A friendly and loyal breed",
            stats = emptyMap() // Assuming stats is a Map<String, Int>
        )

        // Act
        repository.setSelectedBreedData(catBreed)

        // Assert
        val retrievedBreed = repository.getSelectedBreedData()
        assertEquals(catBreed, retrievedBreed)
    }

    @Test
    fun `getSelectedBreedData should throw an exception when selectedBreed is not set`() {
        // Act & Assert
        assertThrows(UninitializedPropertyAccessException::class.java) {
            repository.getSelectedBreedData()
        }
    }
}

package com.example.hellokitty.domain.mapper

import com.example.hellokitty.data.models.CatImageApiResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CatImageApiResponseMapperTest {

    @Test
    fun `mapToDomain should map all fields correctly`() {
        // Arrange
        val apiResponse = CatImageApiResponse(
            id = "123",
            url = "https://example.com/cat.jpg"
        )

        // Act
        val domainModel = apiResponse.mapToDomain()

        // Assert
        assertEquals("123", domainModel.id)
        assertEquals("https://example.com/cat.jpg", domainModel.url)
    }

    @Test
    fun `mapToDomain should throw error when id is null`() {
        // Arrange
        val apiResponse = CatImageApiResponse(
            id = null, // id is null
            url = "https://example.com/cat.jpg"
        )

        // Act & Assert
        assertThrows(IllegalStateException::class.java) {
            apiResponse.mapToDomain()
        }.also { exception ->
            assertEquals("Cat image id is null", exception.message)
        }
    }

    @Test
    fun `mapToDomain should throw error when url is null`() {
        // Arrange
        val apiResponse = CatImageApiResponse(
            id = "123",
            url = null // url is null
        )

        // Act & Assert
        assertThrows(IllegalStateException::class.java) {
            apiResponse.mapToDomain()
        }.also { exception ->
            assertEquals("Cat image url is null", exception.message)
        }
    }
}

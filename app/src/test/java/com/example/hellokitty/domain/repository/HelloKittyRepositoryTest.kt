package com.example.hellokitty.domain.repository

import com.example.hellokitty.data.models.ApiResult
import com.example.hellokitty.data.models.CatBreedsApiModel
import com.example.hellokitty.data.models.CatImageApiResponse
import com.example.hellokitty.data.network.HelloKittyApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class HelloKittyRepositoryImplTest {

    private lateinit var api: HelloKittyApiService
    private lateinit var repository: HelloKittyRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        repository = HelloKittyRepositoryImpl(api)
    }

    @Test
    fun `getCatBreedsPaginated should return success when response is successful`() = runTest {
        // Arrange
        val catBreedsApiModels = listOf(
            CatBreedsApiModel(id = "1", name = "Breed1"),
            CatBreedsApiModel(id = "2", name = "Breed2")
        )
        coEvery { api.getCatBreeds(10, 1) } returns Response.success(catBreedsApiModels)

        // Act
        val result = repository.getCatBreedsPaginated(1, 10)

        // Assert
        assert(result is ApiResult.Success)
        val catBreeds = (result as ApiResult.Success).data
        assertEquals(2, catBreeds.size)
        assertEquals("Breed1", catBreeds[0].name)
        assertEquals("Breed2", catBreeds[1].name)
    }

    @Test
    fun `getCatBreedsPaginated should return error when response is unsuccessful`() = runTest {
        // Arrange
        coEvery { api.getCatBreeds(10, 1) } returns Response.error(404, mockk(relaxed = true))

        // Act
        val result = repository.getCatBreedsPaginated(1, 10)

        // Assert
        assert(result is ApiResult.Error)
        assertEquals("Unknown error", (result as ApiResult.Error).message)
    }

    @Test
    fun `getCatBreedsPaginated should return error when response throws exception`() = runTest {
        // Arrange
        coEvery { api.getCatBreeds(10, 1) } throws IOException("network error")

        // Act
        val result = repository.getCatBreedsPaginated(1, 10)

        // Assert
        assert(result is ApiResult.Error)
        assertEquals("network error", (result as ApiResult.Error).message)
    }

    @Test
    fun `getCatImage should return success when response is successful`() = runTest {
        // Arrange
        val catImageApiResponse =
            CatImageApiResponse(id = "123", url = "https://example.com/image.jpg")
        coEvery { api.getCatImageFromId("123") } returns Response.success(catImageApiResponse)

        // Act
        val result = repository.getCatImage("123")

        // Assert
        assert(result is ApiResult.Success)
        val catImage = (result as ApiResult.Success).data
        assertEquals("123", catImage.id)
        assertEquals("https://example.com/image.jpg", catImage.url)
    }

    @Test
    fun `getCatImage should return error when response is unsuccessful`() = runTest {
        // Arrange
        coEvery { api.getCatImageFromId("123") } returns Response.error(404, mockk(relaxed = true))

        // Act
        val result = repository.getCatImage("123")

        // Assert
        assert(result is ApiResult.Error)
        assertEquals("Unknown error", (result as ApiResult.Error).message)
    }

    @Test
    fun `getCatImage should return error when response throws exception`() = runTest {
        // Arrange
        coEvery { api.getCatImageFromId("123") } throws IOException("network error")

        // Act
        val result = repository.getCatImage("123")

        // Assert
        assert(result is ApiResult.Error)
        assertEquals("network error", (result as ApiResult.Error).message)
    }

    @Test
    fun `getBreedImages should return success when response is successful`() = runTest {
        // Arrange
        val catImageResponses = listOf(
            CatImageApiResponse(id = "1", url = "https://example.com/image1.jpg"),
            CatImageApiResponse(id = "2", url = "https://example.com/image2.jpg")
        )
        coEvery { api.searchCatImages("breed1", 9) } returns Response.success(catImageResponses)

        // Act
        val result = repository.getBreedImages("breed1")

        // Assert
        assert(result is ApiResult.Success)
        val images = (result as ApiResult.Success).data
        assertEquals(2, images.size)
        assertEquals("https://example.com/image1.jpg", images[0].url)
        assertEquals("https://example.com/image2.jpg", images[1].url)
    }

    @Test
    fun `getBreedImages should return error when response is unsuccessful`() = runTest {
        // Arrange
        coEvery { api.searchCatImages("breed1", 9) } returns Response.error(
            404,
            mockk(relaxed = true)
        )

        // Act
        val result = repository.getBreedImages("breed1")

        // Assert
        assert(result is ApiResult.Error)
        assertEquals("Unknown error", (result as ApiResult.Error).message)
    }

    @Test
    fun `getBreedImages should return error when response throws exception`() = runTest {
        // Arrange
        coEvery { api.searchCatImages("breed1", 9) } throws IOException("network error")

        // Act
        val result = repository.getBreedImages("breed1")

        // Assert
        assert(result is ApiResult.Error)
        assertEquals("network error", (result as ApiResult.Error).message)
    }
}

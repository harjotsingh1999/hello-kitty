package com.example.hellokitty.domain.usecase

import com.example.hellokitty.data.models.ApiResult
import com.example.hellokitty.domain.models.CatBreed
import com.example.hellokitty.domain.repository.HelloKittyRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.jeasy.random.EasyRandom
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCatBreedsPaginatedUseCaseTest {

    private val repository: HelloKittyRepository = mockk(relaxed = true)
    private lateinit var useCase: GetCatBreedsPaginatedUseCase
    private val easyRandom = EasyRandom()

    @Before
    fun setUp() {
        useCase = GetCatBreedsPaginatedUseCase(repository)
    }

    @Test
    fun `invoke should return success and increment page on successful data retrieval`() =
        runTest {
            // Arrange
            val mockCatBreeds = List(10) { easyRandom.nextObject(CatBreed::class.java) }
            coEvery { repository.getCatBreedsPaginated(any(), any()) } returns ApiResult.Success(
                mockCatBreeds
            )

            // Act
            val result = useCase()

            // Assert
            assertTrue(result is ApiResult.Success)
            assertEquals(mockCatBreeds, (result as ApiResult.Success).data)
            assertTrue(useCase.dataAvailable) // Data should be marked as available

            coVerify {
                repository.getCatBreedsPaginated(
                    0,
                    10
                )
            } // Ensure the correct page and limit were used
        }

    @Test
    fun `invoke should set dataAvailable to false and not increment page on empty data`() =
        runTest {
            // Arrange
            coEvery { repository.getCatBreedsPaginated(any(), any()) } returns ApiResult.Success(
                emptyList()
            )

            // Act
            val result = useCase()

            // Assert
            assertTrue(result is ApiResult.Success)
            assertEquals(emptyList<CatBreed>(), (result as ApiResult.Success).data)
            assertFalse(useCase.dataAvailable) // Data should be marked as not available
        }

    @Test
    fun `invoke should return error result and set dataAvailable to false on failure`() =
        runTest {
            // Arrange
            val errorResult = ApiResult.Error("Network error")
            coEvery { repository.getCatBreedsPaginated(any(), any()) } returns errorResult

            // Act
            val result = useCase()

            // Assert
            assertEquals(errorResult, result)
            assertFalse(useCase.dataAvailable) // Data should be marked as not available
        }

    @Test
    fun `reset should set page to 0 and dataAvailable to true`() = runTest {
        // Arrange
        useCase.invoke() // Set up a state where page is incremented and dataAvailable may be false
        useCase.reset()

        // Assert
        assertTrue(useCase.dataAvailable) // Data availability should reset to true
    }
}

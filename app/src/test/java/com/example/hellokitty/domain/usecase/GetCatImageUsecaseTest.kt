package com.example.hellokitty.domain.usecase

import com.example.hellokitty.data.models.ApiResult
import com.example.hellokitty.domain.models.CatImage
import com.example.hellokitty.domain.repository.HelloKittyRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.jeasy.random.EasyRandom
import org.junit.Before
import org.junit.Test

class GetCatImageUsecaseTest {

    private val repository: HelloKittyRepository = mockk()
    private lateinit var getCatImageUsecase: GetCatImageUsecase
    private val easyRandom = EasyRandom()

    @Before
    fun setUp() {
        getCatImageUsecase = GetCatImageUsecase(repository)
    }

    @Test
    fun `sample test`() {
        assertEquals(1 + 1, 2)
    }

    @Test
    fun `invoke should call repository for each non-null ID and return images`() = runTest {
        val refImageIdList = listOf("id1", "id2", null, "id3")
        val catImage = easyRandom.nextObject(CatImage::class.java)
        val expectedImages = listOf(ApiResult.Success(catImage), ApiResult.Success(catImage), ApiResult.Success(catImage))

        // Set up the mock responses
        coEvery { repository.getCatImage("id1") } returns ApiResult.Success(catImage)
        coEvery { repository.getCatImage("id2") } returns ApiResult.Success(catImage)
        coEvery { repository.getCatImage("id3") } returns ApiResult.Success(catImage)

        // Call the use case
        val result = getCatImageUsecase(refImageIdList)

        // Verify repository calls and result
        coVerify(exactly = 1) { repository.getCatImage("id1") }
        coVerify(exactly = 1) { repository.getCatImage("id2") }
        coVerify(exactly = 1) { repository.getCatImage("id3") }
        confirmVerified(repository)

        // Assert the result
        assertEquals(expectedImages, result)
    }

    @Test
    fun `invoke should return empty list when all IDs are null`() = runTest {
        val refImageIdList = listOf<String?>(null, null, null)

        // Call the use case
        val result = getCatImageUsecase(refImageIdList)

        // Assert that the result is an empty list
        assertEquals(emptyList<CatImage>(), result)

        // Verify that the repository is never called
        coVerify(exactly = 0) { repository.getCatImage(any()) }
    }
}

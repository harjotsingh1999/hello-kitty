package com.example.hellokitty.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.hellokitty.data.models.ApiResult
import com.example.hellokitty.domain.models.CatBreed
import com.example.hellokitty.domain.models.CatImage
import com.example.hellokitty.domain.repository.BreedDataRepository
import com.example.hellokitty.domain.repository.HelloKittyRepository
import com.example.hellokitty.ui.interactor.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.jeasy.random.EasyRandom
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class CatBreedDetailViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val dataRepository: BreedDataRepository = mockk(relaxed = true)
    private val helloKittyRepository: HelloKittyRepository = mockk(relaxed = true)
    private lateinit var viewModel: CatBreedDetailViewModel
    private val easyRandom = EasyRandom()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `init should fetch breed detail and set initial ui state`() = runTest {
        // Arrange
        val breedData = easyRandom.nextObject(CatBreed::class.java)
        every { dataRepository.getSelectedBreedData() } returns breedData

        val breedImages = easyRandom.objects(CatImage::class.java, 2).toList()
        coEvery { helloKittyRepository.getBreedImages(breedData.id) } returns ApiResult.Success(
            breedImages
        )

        // Act
        viewModel =
            CatBreedDetailViewModel(dataRepository, helloKittyRepository)  // Invoked in init block
        advanceUntilIdle()


        // Assert
        val uiState = viewModel.uiState.value
        assertTrue(uiState is CatBreedDetailUiState.Success)
        val successState = uiState as CatBreedDetailUiState.Success
        assertEquals(breedData.name, successState.data.breedDetails.name)
    }

    @Test
    fun `fetchBreedImages should update uiState with images on success`() = runTest {
        // Arrange
        val breedData = easyRandom.nextObject(CatBreed::class.java)
        every { dataRepository.getSelectedBreedData() } returns breedData

        val breedImages = easyRandom.objects(CatImage::class.java, 2).toList()
        coEvery { helloKittyRepository.getBreedImages(breedData.id) } returns ApiResult.Success(
            breedImages
        )

        // Act
        viewModel =
            CatBreedDetailViewModel(dataRepository, helloKittyRepository)
        advanceUntilIdle()

        // Assert
        val uiState = viewModel.uiState.value
        assertTrue(uiState is CatBreedDetailUiState.Success)
        val successState = uiState as CatBreedDetailUiState.Success
        assertTrue(successState.data.images is CatBreedImages.Success)
        assertEquals(
            breedImages.map { it.url },
            (successState.data.images as CatBreedImages.Success).data
        )
    }

    @Test
    fun `fetchBreedImages should update uiState with error when repository returns error`() =
        runTest {
            // Arrange
            val breedData = easyRandom.nextObject(CatBreed::class.java)
            every { dataRepository.getSelectedBreedData() } returns breedData


            coEvery { helloKittyRepository.getBreedImages(breedData.id) } returns ApiResult.Error("Network error")

            // Act
            viewModel =
                CatBreedDetailViewModel(dataRepository, helloKittyRepository)
            advanceUntilIdle()

            // Assert
            val uiState = viewModel.uiState.value
            assertTrue(uiState is CatBreedDetailUiState.Success)
            val successState = uiState as CatBreedDetailUiState.Success
            assertTrue(successState.data.images is CatBreedImages.Error)
        }

    @Test
    fun `handleEvent OnErrorReloadClick should refetch breed detail`() = runTest {
        // Arrange
        val breedData = easyRandom.nextObject(CatBreed::class.java)
        every { dataRepository.getSelectedBreedData() } returns breedData

        val breedImages = easyRandom.objects(CatImage::class.java, 2).toList()
        coEvery { helloKittyRepository.getBreedImages(breedData.id) } returns ApiResult.Success(
            breedImages
        )

        // Act
        viewModel =
            CatBreedDetailViewModel(dataRepository, helloKittyRepository)
        viewModel.handleEvent(BreedDetailEvent.OnErrorReloadClick)

        // Assert
        val uiState = viewModel.uiState.value
        assertTrue(uiState is CatBreedDetailUiState.Success)
        val successState = uiState as CatBreedDetailUiState.Success
        assertEquals(breedData.name, successState.data.breedDetails.name)
        assertEquals(CatBreedImages.Loading, successState.data.images) // Reloaded state
    }
}

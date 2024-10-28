package com.example.hellokitty.ui.viewmodels


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.hellokitty.data.models.ApiResult
import com.example.hellokitty.domain.models.CatBreed
import com.example.hellokitty.domain.models.CatImage
import com.example.hellokitty.domain.repository.BreedDataRepository
import com.example.hellokitty.domain.usecase.GetCatBreedsPaginatedUseCase
import com.example.hellokitty.domain.usecase.GetCatImageUsecase
import com.example.hellokitty.ui.interactor.CatBreedsListEvent
import com.example.hellokitty.ui.interactor.CatBreedsListSideEffects
import com.example.hellokitty.ui.interactor.CatBreedsListUiState
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import kotlinx.coroutines.test.setMain
import org.jeasy.random.EasyRandom
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class CatBreedsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val getCatBreedsPaginatedUseCase: GetCatBreedsPaginatedUseCase = mockk(relaxed = true)
    private val getCatImageUsecase: GetCatImageUsecase = mockk(relaxed = true)
    private val breedDataRepository: BreedDataRepository = mockk(relaxed = true)
    private lateinit var viewModel: CatBreedsViewModel
    private val easyRandom = EasyRandom()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        every { getCatBreedsPaginatedUseCase.dataAvailable } returns true
    }

    @Test
    fun `fetchCatBreeds should update uiState with Success when data is available`() = runTest {
        // Arrange

        val mockCatBreeds = List(10) { easyRandom.nextObject(CatBreed::class.java) }
        coEvery { getCatBreedsPaginatedUseCase() } returns ApiResult.Success(mockCatBreeds)
        coEvery { getCatImageUsecase(any()) } returns mockCatBreeds.map {
            ApiResult.Success(CatImage(it.refImageId!!, "https://example.com/${it.refImageId}"))
        }
        // Act
        viewModel = CatBreedsViewModel(
            getCatBreedsPaginatedUseCase = getCatBreedsPaginatedUseCase,
            getCatImageUsecase = getCatImageUsecase,
            breedDataRepository = breedDataRepository
        )

        advanceUntilIdle()
        // Assert
        val uiState = viewModel.uiState.value
        assertTrue(uiState is CatBreedsListUiState.Success)
        val successState = uiState as CatBreedsListUiState.Success
        assertEquals(10, successState.data.catBreeds.size)
    }

    @Test
    fun `fetchCatBreeds should update uiState with Error when API result is Error`() = runTest {
        // Arrange
        coEvery { getCatBreedsPaginatedUseCase() } returns ApiResult.Error("Network Error")

        // Act
        viewModel = CatBreedsViewModel(
            getCatBreedsPaginatedUseCase = getCatBreedsPaginatedUseCase,
            getCatImageUsecase = getCatImageUsecase,
            breedDataRepository = breedDataRepository
        )
        advanceUntilIdle()

        // Assert
        val uiState = viewModel.uiState.value
        assertTrue(uiState is CatBreedsListUiState.Error)
        val errorState = uiState as CatBreedsListUiState.Error
        assertEquals("Network Error", errorState.message)
    }

    @Test
    fun `handleEvent OnErrorReloadClick should reset and fetchCatBreeds`() = runTest {
        // Arrange
        coEvery { getCatBreedsPaginatedUseCase.reset() } just Runs
        coEvery { getCatBreedsPaginatedUseCase() } returns ApiResult.Success(emptyList())

        // Act
        viewModel = CatBreedsViewModel(
            getCatBreedsPaginatedUseCase = getCatBreedsPaginatedUseCase,
            getCatImageUsecase = getCatImageUsecase,
            breedDataRepository = breedDataRepository
        )
        viewModel.handleEvent(CatBreedsListEvent.OnErrorReloadClick)
        advanceUntilIdle()

        // Assert
        coVerify { getCatBreedsPaginatedUseCase.reset() }
        coVerify { getCatBreedsPaginatedUseCase() }
        val uiState = viewModel.uiState.value
        assertTrue(uiState is CatBreedsListUiState.Success)
    }

    @Test
    fun `handleEvent OnCatBreedClick should set breed and emit side effect`() = runTest {
        // Arrange
        val mockBreed = easyRandom.nextObject(CatBreed::class.java)
        coEvery { getCatBreedsPaginatedUseCase() } returns ApiResult.Success(emptyList())
        coEvery { breedDataRepository.setSelectedBreedData(mockBreed) } just Runs

        // Start collecting side effects in the background
        val emittedSideEffects = mutableListOf<CatBreedsListSideEffects>()
        val job = launch {
            viewModel.sideEffects.collect {
                emittedSideEffects.add(it)
            }
        }

        // Act
        viewModel = CatBreedsViewModel(
            getCatBreedsPaginatedUseCase = getCatBreedsPaginatedUseCase,
            getCatImageUsecase = getCatImageUsecase,
            breedDataRepository = breedDataRepository
        )
        viewModel.handleEvent(CatBreedsListEvent.OnCatBreedClick(mockBreed))
        advanceUntilIdle()

        // Assert
        coVerify { breedDataRepository.setSelectedBreedData(mockBreed) }
        assertTrue(emittedSideEffects.first() is CatBreedsListSideEffects.OpenCatBreedDetails)
        assertEquals(
            mockBreed.id,
            (emittedSideEffects.first() as CatBreedsListSideEffects.OpenCatBreedDetails).catBreedId
        )

        // Cancel the collection job to avoid leaks
        job.cancel()
    }

    @Test
    fun `handleEvent OnScrollToEnd should fetchCatBreeds when data is available`() = runTest {
        // Arrange
        coEvery { getCatBreedsPaginatedUseCase.dataAvailable } returns true
        coEvery { getCatBreedsPaginatedUseCase() } returns ApiResult.Success(emptyList())

        // Act
        viewModel = CatBreedsViewModel(
            getCatBreedsPaginatedUseCase = getCatBreedsPaginatedUseCase,
            getCatImageUsecase = getCatImageUsecase,
            breedDataRepository = breedDataRepository
        )
        advanceUntilIdle()
        viewModel.handleEvent(CatBreedsListEvent.OnScrollToEnd)
        advanceUntilIdle()

        // Assert
        coVerify(exactly = 2) { getCatBreedsPaginatedUseCase() }
    }
}

package com.example.calculadorasleep.presentation.quality

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calculadorasleep.MainDispatcherRule
import com.example.calculadorasleep.domain.sleep.UseCases.ObserveSleepHistoryUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.quality.UpsertSleepQualityUseCase
import com.example.calculadorasleep.domain.sleep.model.Sleep
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SleepQualityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var upsertSleepQualityUseCase: UpsertSleepQualityUseCase
    private lateinit var observeSleepHistoryUseCase: ObserveSleepHistoryUseCase
    private lateinit var viewModel: SleepQualityViewModel

    @Before
    fun setup() {
        upsertSleepQualityUseCase = mockk(relaxed = true)
        observeSleepHistoryUseCase = mockk(relaxed = true)
        viewModel = SleepQualityViewModel(upsertSleepQualityUseCase, observeSleepHistoryUseCase)
    }

    @Test
    fun `cambiar rating actualiza el estado y el tag sugerido`() {
        viewModel.onEvent(SleepQualityUiEvent.RatingChanged(5))
        
        assertEquals(5, viewModel.state.value.rating)
        assertEquals("Me siento descansado", viewModel.state.value.selectedTag)
    }

    @Test
    fun `cuando se toca un tag se actualiza el rating correspondiente`() {
        val tag = "Interrumpido"
        
        viewModel.onEvent(SleepQualityUiEvent.TagToggled(tag))
        
        assertEquals(tag, viewModel.state.value.selectedTag)
        assertEquals(1, viewModel.state.value.rating)
        
        viewModel.onEvent(SleepQualityUiEvent.TagToggled(tag))
        
        assertNull(viewModel.state.value.selectedTag)
        assertNull(viewModel.state.value.rating)
    }

    @Test
    fun `cuando se guarda calidad con exito se marca isSaved`() = runTest {
        val sleep = Sleep(sleepId = 1, dormirTiempo = 0, despertarTiempo = 0, ciclos = 5)
        every { observeSleepHistoryUseCase() } returns flowOf(listOf(sleep))
        coEvery { upsertSleepQualityUseCase(any()) } returns Result.success(1)

        viewModel.onEvent(SleepQualityUiEvent.RatingChanged(5))
        viewModel.onEvent(SleepQualityUiEvent.SaveQuality)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isSaved)
        assertFalse(viewModel.state.value.isLoading)
    }
}
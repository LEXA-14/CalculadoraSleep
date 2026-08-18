package com.example.calculadorasleep.presentation.quality

import com.example.calculadorasleep.MainDispatcherRule
import com.example.calculadorasleep.domain.sleep.UseCases.ObserveSleepHistoryUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.quality.UpsertSleepQualityUseCase
import com.example.calculadorasleep.domain.sleep.model.Sleep
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SleepQualityViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

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
    fun `cuando se cambia el rating se actualiza el estado`() {
        viewModel.onEvent(SleepQualityUiEvent.RatingChanged(4))
        assertEquals(4, viewModel.state.value.rating)
    }

    @Test
    fun `cuando se toca el mismo rating se deselecciona`() {
        viewModel.onEvent(SleepQualityUiEvent.RatingChanged(4))
        viewModel.onEvent(SleepQualityUiEvent.RatingChanged(4))
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

    @Test
    fun `cuando se agrega o quita un tag se actualiza la lista`() {
        val tag = "Descansado"
        viewModel.onEvent(SleepQualityUiEvent.TagToggled(tag))
        assertTrue(viewModel.state.value.selectedTags.contains(tag))

        viewModel.onEvent(SleepQualityUiEvent.TagToggled(tag))
        assertFalse(viewModel.state.value.selectedTags.contains(tag))
    }
}
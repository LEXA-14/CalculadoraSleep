package com.example.calculadorasleep.presentation.alarm.edit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calculadorasleep.MainDispatcherRule
import com.example.calculadorasleep.domain.sleep.UseCases.CalculateSleepTimesUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.ResolveSleepSessionMillisUseCase
import com.example.calculadorasleep.domain.sleep.UseCases.SaveSleepUseCase
import com.example.calculadorasleep.domain.sleep.model.Alarm
import com.example.calculadorasleep.domain.sleep.repository.AlarmRepository
import com.example.calculadorasleep.presentation.alarm.AlarmScheduler
import com.example.calculadorasleep.presentation.darkMode.ThemeState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AlarmEditViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var calculateSleepTimesUseCase: CalculateSleepTimesUseCase
    private lateinit var resolveSleepSessionMillisUseCase: ResolveSleepSessionMillisUseCase
    private lateinit var saveSleepUseCase: SaveSleepUseCase
    private lateinit var alarmRepository: AlarmRepository
    private lateinit var alarmScheduler: AlarmScheduler
    private lateinit var themeState: ThemeState
    private lateinit var viewModel: AlarmEditViewModel

    @Before
    fun setup() {
        calculateSleepTimesUseCase = mockk(relaxed = true)
        resolveSleepSessionMillisUseCase = mockk(relaxed = true)
        saveSleepUseCase = mockk(relaxed = true)
        alarmRepository = mockk(relaxed = true)
        alarmScheduler = mockk(relaxed = true)
        themeState = mockk(relaxed = true)
        
        viewModel = AlarmEditViewModel(
            calculateSleepTimesUseCase,
            resolveSleepSessionMillisUseCase,
            saveSleepUseCase,
            alarmRepository,
            alarmScheduler,
            themeState
        )
    }

    @Test
    fun `cargar alarma actualiza el estado con formato 12h`() = runTest {
        val alarm = Alarm(alarmId = 1, time = LocalTime(14, 30))
        coEvery { alarmRepository.getAlarm(1) } returns alarm

        viewModel.onEvent(AlarmEditEvent.Load(1))
        advanceUntilIdle()

        assertEquals(1, viewModel.state.value.alarmId)
        assertEquals(2, viewModel.state.value.hour)
        assertEquals(30, viewModel.state.value.minute)
        assertFalse(viewModel.state.value.isAm)
    }

    @Test
    fun `eliminar alarma llama al repositorio y al scheduler`() = runTest {
        val alarmId = 5
        val alarm = Alarm(alarmId = alarmId, time = LocalTime(7, 0))
        coEvery { alarmRepository.getAlarm(alarmId) } returns alarm
        
        viewModel.onEvent(AlarmEditEvent.Load(alarmId))
        viewModel.onEvent(AlarmEditEvent.Delete)
        advanceUntilIdle()

        coVerify { alarmRepository.delete(alarmId) }
        coVerify { alarmScheduler.cancel(alarm) }
        assertTrue(viewModel.state.value.deleted)
    }
}
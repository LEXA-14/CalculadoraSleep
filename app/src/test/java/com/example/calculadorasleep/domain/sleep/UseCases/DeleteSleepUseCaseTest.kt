package com.example.calculadorasleep.domain.sleep.UseCases

import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.domain.sleep.repository.SleepRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DeleteSleepUseCaseTest {
    private lateinit var repository: SleepRepository
    private lateinit var useCase: DeleteSleepUseCase
    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = DeleteSleepUseCase(repository)
    }
    @Test
    fun `delega la eliminacion al repositorio`() = runTest {
        val repository: SleepRepository = mockk(relaxed = true)
        val useCase = DeleteSleepUseCase(repository)
        val sleep = Sleep(sleepId = 1, dormirTiempo = 0L, despertarTiempo = 1000L, ciclos = 1)

        useCase(sleep)

        coVerify(exactly = 1) { repository.delete(sleep) }
    }

}
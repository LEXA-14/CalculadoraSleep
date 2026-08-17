package com.example.calculadorasleep.domain.sleep.Validation

import org.junit.Assert.*
import org.junit.Test

class SleepValidationTest {

    @Test
    fun `validateTiempos con dormirTiempo cero o negativo es invalido`() {
        val result = validateTiempos(dormirTiempo = 0L, despertarTiempo = 1000L)
        assertFalse(result.isValid)
        assertEquals("Hora de dormir inválida", result.error)
    }

    @Test
    fun `validateTiempos con despertarTiempo cero o negativo es invalido`() {
        val result = validateTiempos(dormirTiempo = 1000L, despertarTiempo = 0L)
        assertFalse(result.isValid)
        assertEquals("Hora de despertar inválida", result.error)
    }
    @Test
    fun `validateTiempos cuando despertar es antes o igual que dormir es invalido`() {
        val result = validateTiempos(dormirTiempo = 5000L, despertarTiempo = 5000L)
        assertFalse(result.isValid)
        assertEquals("La hora de despertar debe ser posterior a la de dormir", result.error)
    }
    @Test
    fun `validateTiempos con duracion valida es valido`() {
        val dormir = 0L
        val despertar = 8 * 60 * 60 * 1000L
        val result = validateTiempos(dormir, despertar)
        assertTrue(result.isValid)
        assertEquals(null, result.error)
    }
    @Test
    fun `validateTiempos en el limite exacto de 24 horas es valido`() {
        val dormir = 0L
        val despertar = 24 * 60 * 60 * 1000L
        val result = validateTiempos(dormir, despertar)
        assertTrue(result.isValid)
    }





}
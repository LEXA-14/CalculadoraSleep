package com.example.calculadorasleep.domain.sleep.Validation

import org.junit.Assert.*
import org.junit.Test

class SleepValidationTest {

    @Test
    fun `validateTiempos con dormirTiempo cero o negativo es invalido`() {
        val dormir = 1_700_000_000_000L
        val despertar = dormir + 8 * 60 * 60 * 1000L
        val result = validateTiempos(dormir, despertar)
        assertTrue(result.isValid)
        assertEquals(null, result.error)
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
        val dormir = 1_700_000_000_000L
        val despertar =dormir+ 8 * 60 * 60 * 1000L
        val result = validateTiempos(dormir, despertar)
        assertTrue(result.isValid)
        assertEquals(null, result.error)
    }
    @Test
    fun `validateTiempos en el limite exacto de 24 horas es valido`() {
        val dormir = 1_700_000_000_000L
        val despertar = dormir + 24 * 60 * 60 * 1000L
        val result = validateTiempos(dormir, despertar)
        assertTrue(result.isValid)
    }


    @Test
    fun `validateCalidad null es valido porque es opcional`() {
        val result = validateCalidad(null)
        assertTrue(result.isValid)
    }
    @Test
    fun `validateCalidad fuera de rango 1 a 5 es invalido`() {
        assertFalse(validateCalidad(0).isValid)
        assertFalse(validateCalidad(6).isValid)
        assertFalse(validateCalidad(-1).isValid)
    }
    @Test
    fun `validateCalidad dentro de rango 1 a 5 es valido`() {
        for (valor in 1..5) {
            assertTrue("calidad=$valor deberia ser valida", validateCalidad(valor).isValid)
        }
    }





}
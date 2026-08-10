package com.example.calculadorasleep.domain.sleep.Validation

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)

fun validateTiempos(dormirTiempo: Long, despertarTiempo: Long): ValidationResult {
    return when {
        dormirTiempo <= 0 -> ValidationResult(false, "Hora de dormir inválida")
        despertarTiempo <= 0 -> ValidationResult(false, "Hora de despertar inválida")
        despertarTiempo <= dormirTiempo -> ValidationResult(false, "La hora de despertar debe ser posterior a la de dormir")
        (despertarTiempo - dormirTiempo) > 24 * 60 * 60 * 1000L -> ValidationResult(false, "La duración no puede superar 24 horas")
        (despertarTiempo - dormirTiempo) < 10 * 60 * 1000L -> ValidationResult(false, "La duración es demasiado corta para ser una noche de sueño")
        else -> ValidationResult(true)
    }
}

fun validateCalidad(calidad: Int?): ValidationResult {
    return when {
        calidad == null -> ValidationResult(true) // opcional, puede no calificarse aún
        calidad !in 1..5 -> ValidationResult(false, "La calidad debe estar entre 1 y 5")
        else -> ValidationResult(true)
    }
}

fun validateDias(dias: Int): ValidationResult {
    return when {
        dias <= 0 -> ValidationResult(false, "El rango de días debe ser mayor a 0")
        dias > 365 -> ValidationResult(false, "El rango de días es demasiado amplio")
        else -> ValidationResult(true)
    }
}
package com.example.calculadorasleep.domain.sleep.Validation

data class AlarmValidations(
    val isValid: Boolean,
    val error: String? = null
)

fun validateLabel(label: String): AlarmValidations {
    return when {
        label.isBlank() -> AlarmValidations(false, "La etiqueta no puede estar vacía")
        label.length < 3 -> AlarmValidations(false, "La etiqueta debe tener al menos 3 caracteres")
        else -> AlarmValidations(true)
    }
}

fun validateTime(hour: Int, minute: Int): AlarmValidations {
    return when {
        hour !in 0..23 -> AlarmValidations(false, "La hora debe estar entre 0 y 23")
        minute !in 0..59 -> AlarmValidations(false, "Los minutos deben estar entre 0 y 59")
        else -> AlarmValidations(true)
    }
}
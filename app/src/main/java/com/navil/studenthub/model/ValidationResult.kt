package com.navil.studenthub.model

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
) {
    companion object {
        fun success(): ValidationResult = ValidationResult(isValid = true)
        fun error(message: String): ValidationResult = ValidationResult(isValid = false, errorMessage = message)
    }
}

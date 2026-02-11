package org.company.app.validation

object ExpiryDateValidator {
    fun validate(value: String): String? {
        val digits = value.filter(Char::isDigit)
        val month = digits.take(2).toIntOrNull() ?: 0
        val year = digits.drop(2).toIntOrNull() ?: 0

        return when {
            digits.isEmpty() -> "Поле обязательно для заполнения"
            month !in 1..12 -> "Месяц должен быть от 01 до 12"
            digits.length < 4 -> "Введите 4 цифры (MM/YY)"
            year < 23 -> "Год должен быть 23 или больше (2023+)"
            else -> null
        }
    }
}
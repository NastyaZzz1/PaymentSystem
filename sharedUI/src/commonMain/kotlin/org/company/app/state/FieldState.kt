package org.company.app.state

data class FieldState(
    val value: String = "",
    val touched: Boolean = false,
    val wasFocused: Boolean = false
)
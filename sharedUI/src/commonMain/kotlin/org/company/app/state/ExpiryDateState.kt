package org.company.app.state

data class ExpiryDateState(
    val value: String = "",
    val touched: Boolean = false,
    val wasFocused: Boolean = false,
    val error: String? = null
)
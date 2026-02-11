package org.company.app.viewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.company.app.state.ExpiryDateState
import org.company.app.state.FieldState
import org.company.app.validation.ExpiryDateValidator

class PaymentViewModel {
    private val _cardNumber = MutableStateFlow(FieldState())
    val cardNumber: StateFlow<FieldState> = _cardNumber

    private val _cardholderName = MutableStateFlow(FieldState())
    val cardholderName: StateFlow<FieldState> = _cardholderName

    private val _cvv = MutableStateFlow(FieldState())
    val cvv: StateFlow<FieldState> = _cvv

    private val _expirationDate = MutableStateFlow(ExpiryDateState())
    val expirationDate: StateFlow<ExpiryDateState> = _expirationDate

    val isFormValid: StateFlow<Boolean> = combine(
        cardNumber,
        cardholderName,
        cvv,
        expirationDate
    ) { number, name, cvv, date ->
        number.value.length == 16 &&
                name.value.isNotEmpty() &&
                cvv.value.length == 3 &&
                date.error == null
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.Companion.Eagerly,
        false
    )

    fun cardNumberFocusChanged(isFocused: Boolean) {
        focusChanged(_cardNumber, isFocused)
    }

    fun cardholderNameFocusChanged(isFocused: Boolean) {
        focusChanged(_cardholderName, isFocused)
    }

    fun cvvFocusChanged(isFocused: Boolean) {
        focusChanged(_cvv, isFocused)
    }

    fun focusChanged(
        state: MutableStateFlow<FieldState>,
        focused: Boolean
    ) {
        val current = state.value
        state.value = when {
            focused -> current.copy(wasFocused = true)
            current.wasFocused -> current.copy(touched = true)
            else -> current
        }
    }

    fun expirationFocusChanged(focused: Boolean) {
        val current = _expirationDate.value
        _expirationDate.value = when {
            focused -> current.copy(wasFocused = true)
            current.wasFocused -> current.copy(touched = true)
            else -> current
        }
    }

    fun cardNumberValueChange (input: String) {
        _cardNumber.value = _cardNumber.value.copy(
            value = input
                .filter(Char::isDigit)
                .take(16)
        )
    }

    fun cardholderNameValueChange (input: String) {
        _cardholderName.value = _cardholderName.value.copy(
            value = input
                .filter { it.isLetter() && it.code < 128 || it == ' ' }
                .uppercase()
        )
    }

    fun cvvValueChange (input: String) {
        _cvv.value = _cvv.value.copy(
            value = input
                .filter(Char::isDigit)
                .take(3)
        )
    }

    fun expirationDateValueChange (input: String) {
        val digits = input
            .filter(Char::isDigit)
            .take(4)

        _expirationDate.value = _expirationDate.value.copy(
            value = digits,
            error = ExpiryDateValidator.validate(digits)
        )
    }
}
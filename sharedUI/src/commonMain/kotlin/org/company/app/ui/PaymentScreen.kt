package org.company.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.company.app.ui.components.CardVisualTransformations
import org.company.app.viewModel.PaymentViewModel
import org.company.app.theme.LimeDark
import org.company.app.theme.OnPrimaryLight
import org.company.app.theme.TertiaryContainerDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel = remember { PaymentViewModel() }
) {
    val cardNumber by viewModel.cardNumber.collectAsState()
    val cardholderName by viewModel.cardholderName.collectAsState()
    val cvv by viewModel.cvv.collectAsState()
    val expirationDate by viewModel.expirationDate.collectAsState()
    val isFormValid by viewModel.isFormValid.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val cardVisualTransformations = CardVisualTransformations

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LimeDark,
                    titleContentColor = OnPrimaryLight,
                ),
                title = { Text("Payment Card Form") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Введите реквизиты банковской карты",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                fontSize = 22.sp
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .onFocusChanged {
                        viewModel.cardNumberFocusChanged(it.isFocused)
                    },
                value = cardNumber.value,
                onValueChange = {
                    viewModel.cardNumberValueChange(it)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TertiaryContainerDark,
                    focusedLabelColor = TertiaryContainerDark,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Row {
                        Text("Номер карты")
                        Text("*", color = Color.Red)
                    }
                },
                placeholder = { Text("0000 0000 0000 0000")},
                isError = cardNumber.touched && cardNumber.value.length != 16,
                supportingText = {
                    if(cardNumber.touched) {
                        if(cardNumber.value.isEmpty()) {
                            Text("Поле пустое")
                        }
                        else if(cardNumber.value.length != 16) {
                            Text("Меньше 16")
                        }
                    }
                },
                visualTransformation = cardVisualTransformations.CardNumbers
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .onFocusChanged {
                        viewModel.cardholderNameFocusChanged(it.isFocused)
                    },
                value = cardholderName.value,
                onValueChange = {
                    viewModel.cardholderNameValueChange(it)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TertiaryContainerDark,
                    focusedLabelColor = TertiaryContainerDark,
                ),
                label = {
                    Row {
                        Text("Имя владельца")
                        Text("*", color = Color.Red)
                    }
                },
                placeholder = { Text("IVAN IVANOV")},
                isError = cardholderName.touched && cardholderName.value.isEmpty(),
                supportingText = {
                    if(cardholderName.touched && cardholderName.value.isEmpty()) {
                        Text("Поле пустое")
                    }
                },
            )

            Row(
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged {
                            viewModel.expirationFocusChanged(it.isFocused)
                        },
                    value = expirationDate.value,
                    onValueChange = {
                        viewModel.expirationDateValueChange(it)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TertiaryContainerDark,
                        focusedLabelColor = TertiaryContainerDark,
                    ),
                    label = {
                        Row {
                            Text("MM/YY")
                            Text("*", color = Color.Red)
                        }
                    },
                    placeholder = { Text("01/25")},
                    visualTransformation = cardVisualTransformations.CardDate,
                    isError = expirationDate.error != null && expirationDate.touched,
                    supportingText = {
                        if(expirationDate.touched) {
                            expirationDate.error?.let {
                                Text(it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    },
                )

                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged {
                            viewModel.cvvFocusChanged(it.isFocused)
                        },
                    value = cvv.value,
                    onValueChange = {
                        viewModel.cvvValueChange(it)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TertiaryContainerDark,
                        focusedLabelColor = TertiaryContainerDark,
                    ),
                    label = {
                        Row {
                            Text("CVV")
                            Text("*", color = Color.Red)
                        }
                    },
                    placeholder = { Text("123")},
                    visualTransformation = PasswordVisualTransformation(),
                    isError = cvv.touched && cvv.value.length != 3,
                    supportingText = {
                        if(cvv.touched) {
                            if(cvv.value.isEmpty()) {
                                Text("Поле пустое")
                            }
                            else if(cvv.value.length != 3) {
                                Text("Меньше 3")
                            }
                        }
                    },
                )
            }

            Button(
                onClick = {
                    scope.launch {
                        snackBarHostState.showSnackbar("Данные подтверждены")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 25.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = OnPrimaryLight,
                    containerColor = LimeDark),
                enabled = isFormValid,
            ) {
                Text("Подтвердить", fontSize = 18.sp)
            }
        }
    }
}
package com.example.phonecodeconfirm

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun LoginScreen(onRegistrationSuccess: () -> Unit) {
    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
    var isPhoneValid by remember { mutableStateOf(true) }
    var consentChecked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Введите номер телефона", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { newValue ->
                phoneNumber = newValue
                isPhoneValid = validatePhoneNumber(newValue.text)
            },
            label = { Text("Номер телефона") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = !isPhoneValid,
            modifier = Modifier.fillMaxWidth()
        )

        if (!isPhoneValid) {
            Text("Неверный формат номера", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = consentChecked,
                onCheckedChange = { consentChecked = it }
            )
            Text("Я согласен с офертой")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isPhoneValid && consentChecked) {
                    // Здесь можно вызвать запрос на регистрацию через библиотеку.
                    // Например: SendyAppSdk.registerPhone(phoneNumber.text, consent = true)
                    Toast.makeText(context, "Регистрация отправлена", Toast.LENGTH_SHORT).show()
                    onRegistrationSuccess() // Переход к следующему экрану, например, экрану ввода СМС-кода
                } else {
                    Toast.makeText(context, "Пожалуйста, проверьте введённые данные", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = isPhoneValid && consentChecked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Продолжить")
        }
    }
}

fun validatePhoneNumber(phone: String): Boolean {
    // Ожидаемый формат: +7 ХХХ ХХХ ХХ ХХ, где ХХХ - три цифры, ХХ - две цифры
    val regex = Regex("^\\+7 \\d{3} \\d{3} \\d{2} \\d{2}\$")
    return regex.matches(phone)
}

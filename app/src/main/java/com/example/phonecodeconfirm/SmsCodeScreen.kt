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

@Composable
fun SmsCodeScreen(onValidationSuccess: () -> Unit = {}) {
    var smsCode by remember { mutableStateOf("") }
    var isCodeValid by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Введите код из СМС", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = smsCode,
            onValueChange = { newValue ->
                smsCode = newValue
                isCodeValid = validateSmsCode(newValue)
            },
            label = { Text("СМС код") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = !isCodeValid,
            modifier = Modifier.fillMaxWidth()
        )

        if (!isCodeValid) {
            Text(
                text = "Введите корректный 6-значный код",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isCodeValid) {
                    // Здесь можно вызвать API для проверки кода из СМС
                    Toast.makeText(context, "Код принят", Toast.LENGTH_SHORT).show()
                    onValidationSuccess()
                } else {
                    Toast.makeText(context, "Проверьте введённый код", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = smsCode.length == 6 && isCodeValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Продолжить")
        }
    }
}

fun validateSmsCode(code: String): Boolean {
    // Код должен состоять ровно из 6 цифр
    return code.length == 6 && code.all { it.isDigit() }
}



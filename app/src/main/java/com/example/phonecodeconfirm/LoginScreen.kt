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
import kotlinx.coroutines.launch
import land.sendy.pfe_sdk.api.API

@Composable
fun LoginScreen(onRegistrationSuccess: (String) -> Unit) {
    // Храним "сырой" ввод пользователя (только 10 цифр)
    var phoneRaw by remember { mutableStateOf("") }
    // Флаг корректности (true, если пользователь ввёл ровно 10 цифр)
    val isPhoneValid by derivedStateOf { isPhoneNumberComplete(phoneRaw) }

    var consentChecked by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Маска для отображения
    val maskedPhone = formatPhoneNumber(phoneRaw)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Введите номер телефона", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = maskedPhone,
            onValueChange = { newValue ->
                // Если на входе +7 уже присутствует, только добавляем цифры
                if (!newValue.startsWith("+7")) {
                    // Если ещё нет +7, добавляем его и очищаем остальное
                    phoneRaw = cleanPhoneInput(newValue)
                } else {
                    // Если уже есть +7, обрабатываем только цифры
                    phoneRaw = cleanPhoneInput(newValue.drop(2)) // Убираем +7
                }
            },
            label = { Text("Номер телефона") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = !isPhoneValid && phoneRaw.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )

        if (!isPhoneValid && phoneRaw.isNotEmpty()) {
            Text("Неверный формат номера", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = consentChecked,
                onCheckedChange = { consentChecked = it }
            )
            Text("Я согласен с офертой")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка отправки на сервер регистрации номера телефона (ПРИМЕР)
        /*Button(
            onClick = {
                if (isPhoneValid && consentChecked) {
                    // Вызываем метод SDK для регистрации
                    scope.launch {
                        val api = API.getInsatce("https://testwallet.sendy.land", "sendy")
                        val fullPhone = formatPhoneNumber(phoneRaw)
                        // Пример вызова (замените на реальный метод из SDK):
                        // Register phone number is not found
                        api.registerPhoneNumber(
                            fullPhone,
                            1, // WhiteLabel = 1
                            true, // Согласие с офертой
                            object : ApiCallback() {
                                override fun onCompleted(res: Boolean) {
                                    super.onCompleted(res)
                                    Toast.makeText(context, "Регистрация отправлена", Toast.LENGTH_SHORT).show()
                                    onRegistrationSuccess(fullPhone)
                                }

                                override fun onFail(error: LoaderError?) {
                                    super.onFail(error)
                                    Toast.makeText(context, "Ошибка: $error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                } else {
                    Toast.makeText(context, "Проверьте введённые данные", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = isPhoneValid && consentChecked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Продолжить Сервер")
        }*/

        Button(
            onClick = {
                if (isPhoneValid && consentChecked) {
                    // Вызываем метод SDK для регистрации
                    Toast.makeText(context, "Регистрация отправлена", Toast.LENGTH_SHORT).show()
                    val fullPhone = formatPhoneNumber(phoneRaw)
                    onRegistrationSuccess(fullPhone)
                } else {
                    Toast.makeText(context, "Проверьте введённые данные", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = isPhoneValid && consentChecked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Продолжить")
        }
    }
}

/**
 * Убираем все нецифровые символы и берём максимум 10 цифр (для номера после +7).
 */
fun cleanPhoneInput(input: String): String {
    val digits = input.filter { it.isDigit() }
    // Берём максимум 10 цифр (кроме +7, которую мы добавляем автоматически)
    return digits.take(10)
}


/**
 * Превращаем "1234567890" -> "+7 123 456 78 90"
 */
fun formatPhoneNumber(raw: String): String {
    val chunk1 = raw.take(3)
    val chunk2 = raw.drop(3).take(3)
    val chunk3 = raw.drop(6).take(2)
    val chunk4 = raw.drop(8).take(2)

    val sb = StringBuilder("+7")
    if (chunk1.isNotEmpty()) sb.append(" ").append(chunk1)
    if (chunk2.isNotEmpty()) sb.append(" ").append(chunk2)
    if (chunk3.isNotEmpty()) sb.append(" ").append(chunk3)
    if (chunk4.isNotEmpty()) sb.append(" ").append(chunk4)

    return sb.toString()
}

/**
 * Проверяем, набраны ли все 10 цифр (полный номер после +7).
 */
fun isPhoneNumberComplete(raw: String): Boolean {
    return raw.length == 10
}

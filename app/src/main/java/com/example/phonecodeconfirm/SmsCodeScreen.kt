package com.example.phonecodeconfirm

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import land.sendy.pfe_sdk.api.API
import land.sendy.pfe_sdk.model.types.ApiCallback
import land.sendy.pfe_sdk.model.types.LoaderError

@Composable
fun SmsCodeScreen(
    phoneNumber: String,
    onValidationSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Массив из 6 строк, каждая хранит одну цифру
    val codeLength = 6
    val codes = remember { mutableStateListOf(*Array(codeLength) { "" }) }

    // Список FocusRequester для управления фокусом
    val focusRequesters = remember { List(codeLength) { FocusRequester() } }

    // Функция для получения полного кода
    fun getFullCode(): String = codes.joinToString("")

    // Проверка, что все поля заполнены (6 цифр)
    val isAllFilled = codes.all { it.length == 1 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Введите код из СМС", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Ряд из 6 полей ввода
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (i in 0 until codeLength) {
                OutlinedTextField(
                    value = codes[i],
                    onValueChange = { newValue ->
                        // Разрешаем ввод только одной цифры
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                            codes[i] = newValue
                            // Если пользователь ввёл цифру и это не последнее поле — переходим к следующему
                            if (newValue.isNotEmpty() && i < codeLength - 1) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        }
                    },
                    modifier = Modifier
                        .width(50.dp)
                        .focusRequester(focusRequesters[i]),
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /*Button(
            onClick = {
                if (isAllFilled) {
                    scope.launch {
                        val api = API.getInsatce("https://testwallet.sendy.land", "sendy")
                        // Пример вызова (замените на реальный метод из SDK):
                        api.validateSmsCode(
                            phoneNumber,
                            smsCode,
                            1, // WhiteLabel = 1
                            object : ApiCallback() {
                                override fun onCompleted(res: Boolean) {
                                    super.onCompleted(res)
                                    Toast.makeText(context, "Код принят!", Toast.LENGTH_SHORT).show()
                                    onValidationSuccess()
                                }

                                override fun onFail(error: LoaderError?) {
                                    super.onFail(error)
                                    Toast.makeText(context, "Ошибка: $error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                } else {
                    Toast.makeText(context, "Проверьте введённый код", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = isAllFilled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Продолжить (server)")
        }*/

        Button(
            onClick = {
                if (isAllFilled) {
                    // Здесь можно вызвать API для проверки кода из СМС
                    if (codes.toList().joinToString() == "123456"){
                        Toast.makeText(context, "Код принят", Toast.LENGTH_SHORT).show()
                        onValidationSuccess()
                    }else{
                        Toast.makeText(context, "Не правильно набран код", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Проверьте введённый код", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = isAllFilled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Продолжить")
        }
    }
}




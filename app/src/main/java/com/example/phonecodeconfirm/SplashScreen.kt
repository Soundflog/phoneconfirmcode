package com.example.phonecodeconfirm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val context = LocalContext.current

    // Создаем NotificationChannel при первом запуске
    LaunchedEffect(Unit) {
        createNotificationChannel(context)
    }

    // Отображаем логотип в центре экрана
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo), // Убедитесь, что ресурс ic_logo существует в res/drawable
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )
    }

    // Задержка 3 секунды, после которой вызывается onTimeout
    LaunchedEffect(Unit) {
        delay(3000)
        // Симулируем приход SMS-кода, например, генерируя случайный код
        val smsCode = "123456" // Или сгенерировать динамически
        sendSmsCodeNotification(context, smsCode)
        onTimeout()
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen {}
}

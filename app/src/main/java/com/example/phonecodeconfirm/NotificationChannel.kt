package com.example.phonecodeconfirm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.os.Build
import androidx.core.content.ContextCompat
import android.Manifest

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "sms_code_channel"
        val channelName = "SMS Code Notifications"
        val channelDescription = "Channel for SMS code notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}


fun sendSmsCodeNotification(context: Context, smsCode: String) {
    val channelId = "sms_code_channel"

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_logo) // Убедитесь, что ресурс существует
        .setContentTitle("SMS Code")
        .setContentText("Ваш SMS-код: $smsCode")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    // Проверяем, получено ли разрешение на отправку уведомлений
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
        NotificationManagerCompat.from(context).notify(1, notification)
    } else {
        // Обработка случая, когда разрешение не получено.
        // Например, можно запросить разрешение или вывести лог.
    }
}

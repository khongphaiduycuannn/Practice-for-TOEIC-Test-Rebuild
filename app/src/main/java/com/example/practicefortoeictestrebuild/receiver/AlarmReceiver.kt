package com.example.practicefortoeictestrebuild.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.SplashScreen

private const val CHANNEL_ID = "channel_notification"

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("EXTRA_TITLE") ?: return
        val message = listOf<String>(
            "Practice English every day to improve your communication skills!",
            "Don't forget to learn new vocabulary today to enhance your word power!",
            "Spend at least 10 minutes every day to learn English and track your progress!",
            "Complete today's exercises to keep progressing in your English learning journey!",
            "Challenge yourself with quizzes to test your English proficiency!",
            "Take breaks and don't overwork yourself - learning English should be enjoyable and rewarding!"
        )

        createNotificationChannel(context)

        val notificationId = 1
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(title)
            .setContentText(message.random())
            .setAutoCancel(true)

        notificationBuilder.setContentIntent(
            PendingIntent.getActivity(
                context, 0,
                Intent(context, SplashScreen::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        )

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val description = context.getString(R.string.channel_description)

            val important = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, important)
            channel.description = description
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }
}
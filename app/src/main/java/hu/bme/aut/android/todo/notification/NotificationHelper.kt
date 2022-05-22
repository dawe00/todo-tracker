package hu.bme.aut.android.todo.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import hu.bme.aut.android.todo.R
import hu.bme.aut.android.todo.TodoListActivity
import hu.bme.aut.android.todo.model.Todo
import kotlin.random.Random

class NotificationHelper{
    companion object {
        private const val ACTION_SHOW_AD = "hu.bme.aut.android.todo"

        fun createNotificationChannels(ctx: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AdBrowserNotificationChannel.values().forEach {
                    val name = it.channelName
                    val descriptionText = it.channelDescription
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val channel = NotificationChannel(it.id, name, importance).apply {
                        description = descriptionText
                    }
                    with(NotificationManagerCompat.from(ctx)) {
                        createNotificationChannel(channel)
                    }
                }

            }
        }

        @SuppressLint("UnspecifiedImmutableFlag")
        fun createTodoNotification(ctx: Context, todo: Todo) {
            val intent = Intent(ctx, TodoListActivity::class.java).apply {
                action = ACTION_SHOW_AD
            }
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val snoozeIntent = Intent(ctx, SnoozeBroadcastReceiver::class.java)
            val snoozePendingIntent: PendingIntent =
                PendingIntent.getBroadcast(ctx, 0, snoozeIntent, 0)

            val builder =
                NotificationCompat.Builder(ctx, AdBrowserNotificationChannel.TODO_SOON.id)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(todo.title + " is due today!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .addAction(
                        0,
                        "Ignore",
                        snoozePendingIntent
                    )
                    .setAutoCancel(true)

            with(NotificationManagerCompat.from(ctx)) {
                notify(Random.Default.nextInt(10000, 100000), builder.build())
            }
        }
    }
}
package hu.bme.aut.android.todo.notification

enum class AdBrowserNotificationChannel(
    val id: String,
    val channelName: String,
    val channelDescription: String
) {
    TODO_SOON("hu.bme.aut.android.todo.notification", "TODO", "Todo is coming"),
}
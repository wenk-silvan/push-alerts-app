package ch.wenksi.pushalerts.models

/**
 * This class represents a notification when received from the Firebase Cloud Messaging service
 */
data class Notification(
    val title: String,
    val description: String,
    val payload: Map<String, String>?
)
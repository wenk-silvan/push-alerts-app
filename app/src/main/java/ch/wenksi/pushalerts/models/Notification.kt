package ch.wenksi.pushalerts.models

data class Notification(
    val title: String,
    val description: String,
    val payload: Map<String, String>?
)
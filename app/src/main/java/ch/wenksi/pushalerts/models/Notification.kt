package ch.wenksi.pushalerts.models

import java.sql.Timestamp
import java.util.*

data class Notification(
    val uuid: UUID = UUID.randomUUID(),
    val status: String = "",
    val createdAt: Timestamp,
)
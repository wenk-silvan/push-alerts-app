package ch.wenksi.pushalerts.models

import java.sql.Timestamp
import java.time.Instant
import java.util.*

data class Task(
    val _id: String = "",
    val uuid: UUID = UUID.randomUUID(),
    val title: String = "",
    val description: String = "",
    val source: String = "",
    val createdAt: Timestamp,
    val closedAt: Timestamp?,
//    val payload: Dictionary<String, String>?,
    val payload: String = "",
    val user: User?,
)
package ch.wenksi.pushalerts.models

import java.util.*

data class User(
    val uuid: UUID = UUID.randomUUID(),
    val email: String = "",
)
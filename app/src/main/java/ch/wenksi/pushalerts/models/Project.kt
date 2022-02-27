package ch.wenksi.pushalerts.models

import java.util.*

data class Project(
    val uuid: UUID = UUID.randomUUID(),
    val name: String = "",
    val description: String = "",
)
package ch.wenksi.pushalerts.models

import java.util.*

data class Project(
    val uuid: UUID = UUID.randomUUID(),
    val name: String = "",
    val description: String = "",
    val menuId: Int = (0..1000).random()
)
package ch.wenksi.pushalerts.models

import java.util.*

/**
 * This class represents a project which a task can be part of.
 * The menu id is needed to dynamically generate an item in the navigation drawer.
 */
data class Project(
    val uuid: UUID = UUID.randomUUID(),
    val name: String = "",
    val description: String = "",
    val menuId: Int = (0..1000).random()
)
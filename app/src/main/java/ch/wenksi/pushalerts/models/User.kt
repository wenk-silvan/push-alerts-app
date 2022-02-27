package ch.wenksi.pushalerts.models

import java.util.*
import kotlin.collections.ArrayList

data class User(
    val uuid: UUID = UUID.randomUUID(),
    val email: String = "",
    val projects: ArrayList<Project> = arrayListOf(),
)
package ch.wenksi.pushalerts.models

import java.util.*

data class Token(val value: String, val expiryUtc: Date, val email: String, val uuid: UUID)
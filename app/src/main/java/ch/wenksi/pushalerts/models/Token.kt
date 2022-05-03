package ch.wenksi.pushalerts.models

import java.util.*

/**
 * Represents the token which is returned after a successful user login. The Value property is the actual JSON Web Token.
 */
data class Token(val value: String, val expiryUtc: Date, val email: String, val uuid: UUID)
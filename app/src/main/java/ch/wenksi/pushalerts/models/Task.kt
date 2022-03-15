package ch.wenksi.pushalerts.models

import ch.wenksi.pushalerts.errors.TaskStateError
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

const val DATE_PATTERN = "yyyy/MM/dd, hh:mm"

class Task {
    val uuid: UUID = UUID.randomUUID()
    val title: String = ""
    val description: String = ""
    val source: String = ""
    val createdAt: Date = Timestamp.from(Instant.now())
    var assignedAt: Date? = null
    var closedAt: Date? = null
    val payload: String = "-" // TODO: Use dictionary<string,string>
    var user: User? = null
    var state: TaskState = TaskState.Opened

    fun assign(user: User) {
        when {
            this.state != TaskState.Opened -> throw TaskStateError("Can't assign user if task state is not Opened.")
            this.user != null -> throw TaskStateError("Can't assign user if task already contains another user.")
            this.assignedAt != null -> throw TaskStateError("Can't assign user if task already has an assignedAt timestamp.")
            else -> {
                this.state = TaskState.Assigned
                this.assignedAt = Timestamp.from(Instant.now())
                this.user = user
            }
        }
    }

    fun finish() {
        when {
            this.state != TaskState.Assigned -> throw TaskStateError("Can't close if task state is not Assigned.")
            this.user == null -> throw TaskStateError("Can't close task if no user is assigned.")
            this.closedAt != null -> throw TaskStateError("Can't finish if task already has a closedAt timestamp.")
            else -> {
                this.state = TaskState.Done
                this.closedAt = Timestamp.from(Instant.now())
            }
        }
    }

    fun reject() {
        when {
            this.state != TaskState.Assigned -> throw TaskStateError("Can't reject if task state is not Assigned.")
            this.user == null -> throw TaskStateError("Can't reject task if no user is assigned.")
            this.closedAt != null -> throw TaskStateError("Can't reject if task already has a closedAt timestamp.")
            else -> {
                this.state = TaskState.Rejected
                this.closedAt = Timestamp.from(Instant.now())
            }
        }
    }

    fun createdAtFormatted(): String = SimpleDateFormat(DATE_PATTERN).format(createdAt)
    fun assignedAtFormatted(): String = SimpleDateFormat(DATE_PATTERN).format(assignedAt)
    fun closedAtFormatted(): String = SimpleDateFormat(DATE_PATTERN).format(closedAt)
}
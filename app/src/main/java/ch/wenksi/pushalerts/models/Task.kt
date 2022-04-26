package ch.wenksi.pushalerts.models

import ch.wenksi.pushalerts.errors.TaskStateError
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

const val DATE_PATTERN = "yyyy/MM/dd, hh:mm"

class Task(
    val uuid: UUID = UUID.randomUUID(),
    val title: String = "",
    val description: String = "",
    val source: String = "",
    val createdAt: Date = Timestamp.from(Instant.now()),
    var assignedAt: Date? = null,
    var closedAt: Date? = null,
    val payload: String = "-", // TODO: Use dictionary<string,string>
    var userEmail: String? = null,
    var status: TaskState = TaskState.Opened
) {
    fun assign(email: String): Task {
        when {
            this.status != TaskState.Opened -> throw TaskStateError("Can't assign user if task state is not Opened.")
            this.userEmail != null -> throw TaskStateError("Can't assign user if task already contains another user.")
            this.assignedAt != null -> throw TaskStateError("Can't assign user if task already has an assignedAt timestamp.")
            else -> {
                this.status = TaskState.Assigned
                this.assignedAt = Timestamp.from(Instant.now())
                this.userEmail = email
            }
        }
        return this
    }

    fun finish(): Task {
        when {
            this.status != TaskState.Assigned -> throw TaskStateError("Can't close if task state is not Assigned.")
            this.userEmail == null -> throw TaskStateError("Can't close task if no user is assigned.")
            this.closedAt != null -> throw TaskStateError("Can't finish if task already has a closedAt timestamp.")
            else -> {
                this.status = TaskState.Finished
                this.closedAt = Timestamp.from(Instant.now())
            }
        }
        return this
    }

    fun reject(): Task {
        when {
            this.status != TaskState.Assigned -> throw TaskStateError("Can't reject if task state is not Assigned.")
            this.userEmail == null -> throw TaskStateError("Can't reject task if no user is assigned.")
            this.closedAt != null -> throw TaskStateError("Can't reject if task already has a closedAt timestamp.")
            else -> {
                this.status = TaskState.Rejected
                this.closedAt = Timestamp.from(Instant.now())
            }
        }
        return this
    }

    fun createdAtFormatted(): String = SimpleDateFormat(DATE_PATTERN).format(createdAt)
    fun assignedAtFormatted(): String = SimpleDateFormat(DATE_PATTERN).format(assignedAt)
    fun closedAtFormatted(): String = SimpleDateFormat(DATE_PATTERN).format(closedAt)
}
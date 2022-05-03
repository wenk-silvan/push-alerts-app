package ch.wenksi.pushalerts.models

import ch.wenksi.pushalerts.errors.TaskStateError
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

const val DATE_PATTERN = "yyyy/MM/dd, hh:mm"

/**
 * This class represents a task
 */
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
    /**
     * Sets the given email in the task, sets the assignedAt timestamp and updates the status to assigned.
     * @throws TaskStateError when task has an invalid state or data
     */
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

    /**
     * Sets the closetAt timestamp and updates the status to finished.
     * @throws TaskStateError when task has an invalid state or data
     */
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


    /**
     * Sets the closetAt timestamp and updates the status to rejected.
     * @throws TaskStateError when task has an invalid state or data
     */
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

    /**
     * Serves as a getter for the createdAt timestamp formatted using the DATE_PATTERN constant.
     */
    fun createdAtFormatted(): String = SimpleDateFormat(DATE_PATTERN).format(createdAt)


    /**
     * Serves as a getter for the assignedAt timestamp formatted using the DATE_PATTERN constant.
     */
    fun assignedAtFormatted(): String = SimpleDateFormat(DATE_PATTERN).format(assignedAt)


    /**
     * Serves as a getter for the closedAt timestamp formatted using the DATE_PATTERN constant.
     */
    fun closedAtFormatted(): String = SimpleDateFormat(DATE_PATTERN).format(closedAt)
}
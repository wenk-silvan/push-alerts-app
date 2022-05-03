package ch.wenksi.pushalerts.models

/**
 * Represents the states in which a task can be.
 */
enum class TaskState() {
    Opened,
    Assigned,
    Finished,
    Rejected,
}
package ch.wenksi.pushalerts.errors

/**
 * Represents an error when the task has an invalid state regarding the task data
 */
class TaskStateError(message: String) : Exception(message)
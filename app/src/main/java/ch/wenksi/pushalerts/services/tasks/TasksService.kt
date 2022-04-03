package ch.wenksi.pushalerts.services.tasks

import ch.wenksi.pushalerts.Constants
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.TaskState
import retrofit2.http.*

interface TasksService {
    @GET("${Constants.apiTasksUri}/{uuid}")
    suspend fun getTasks(@Path("uuid") uuid: String): List<Task>

    @PUT()
    suspend fun assignTask(taskUUID: String, userUUID: String): Task

    @PUT()
    suspend fun close(taskUUID: String, status: TaskState): Task
}
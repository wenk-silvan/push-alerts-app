package ch.wenksi.pushalerts.services.tasks

import ch.wenksi.pushalerts.util.Constants
import ch.wenksi.pushalerts.models.Task
import retrofit2.http.*

interface TasksService {
    @GET("${Constants.apiTasksUri}/{uuid}")
    suspend fun getTasks(@Path("uuid") uuid: String): List<Task>

    @PUT("${Constants.apiTasksUri}/{taskUUID}/assign/{userUUID}")
    suspend fun assignTask(
        @Path("taskUUID") taskUUID: String,
        @Path("userUUID") userUUID: String
    )

    @PUT("${Constants.apiTasksUri}/{taskUUID}/close")
    suspend fun close(
        @Path("taskUUID") taskUUID: String,
        @Query("status") status: Int
    )
}
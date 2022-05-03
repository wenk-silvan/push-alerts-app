package ch.wenksi.pushalerts.services.tasks

import ch.wenksi.pushalerts.util.Constants
import ch.wenksi.pushalerts.models.Task
import retrofit2.http.*

/**
 * Represents the http requests regarding tasks
 */
interface TasksService {
    /**
     * Calls the endpoint of the PushAlerts api to get the tasks of the project
     * @param uuid is the uuid of the project
     */
    @GET("${Constants.apiTasksUri}/{uuid}")
    suspend fun getTasks(@Path("uuid") uuid: String): List<Task>

    /**
     * Calls the endpoint of the PushAlerts api to assign a task to the user
     * @param taskUUID is the uuid of the task
     * @param userUUID is the uuid of the user
     */
    @PUT("${Constants.apiTasksUri}/{taskUUID}/assign/{userUUID}")
    suspend fun assignTask(
        @Path("taskUUID") taskUUID: String,
        @Path("userUUID") userUUID: String
    )

    /**
     * Calls the endpoint of the PushAlerts api to close (finish or reject) a task
     * @param taskUUID is the uuid of the task
     * @param status declares whether the task is finished is rejected
     */
    @PUT("${Constants.apiTasksUri}/{taskUUID}/close")
    suspend fun close(
        @Path("taskUUID") taskUUID: String,
        @Query("status") status: Int
    )
}
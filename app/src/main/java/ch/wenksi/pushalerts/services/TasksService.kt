package ch.wenksi.pushalerts.services

import ch.wenksi.pushalerts.models.Task
import retrofit2.http.GET

interface TasksService {
    @GET()
    suspend fun getTasks(uuid: String): List<Task>
}
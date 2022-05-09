package ch.wenksi.pushalerts.services.projects

import ch.wenksi.pushalerts.util.Constants
import ch.wenksi.pushalerts.models.Project
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Represents the http requests regarding projects
 */
interface ProjectsService {
    /**
     * Calls the endpoint of the PushAlerts api to get the projects
     * @param userUUID is the uuid of the user
     */
    @GET("${Constants.apiProjectsUri}/{userUUID}")
    suspend fun getProjects(
        @Path("userUUID") userUUID: String
    ): List<Project>
}
package ch.wenksi.pushalerts.services.projects

import ch.wenksi.pushalerts.util.Constants
import ch.wenksi.pushalerts.models.Project
import retrofit2.http.GET

/**
 * Represents the http requests regarding projects
 */
interface ProjectsService {
    /**
     * Calls the endpoint of the PushAlerts api to get the projects
     */
    @GET(Constants.apiProjectsUri)
    suspend fun getProjects(): List<Project>
}
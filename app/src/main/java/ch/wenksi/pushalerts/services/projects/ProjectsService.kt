package ch.wenksi.pushalerts.services.projects

import ch.wenksi.pushalerts.Constants
import ch.wenksi.pushalerts.models.Project
import retrofit2.http.GET

interface ProjectsService {
    @GET(Constants.apiProjectsUri)
    suspend fun getProjects(): List<Project>
}
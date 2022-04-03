package ch.wenksi.pushalerts.services

import ch.wenksi.pushalerts.models.Project
import retrofit2.http.GET

interface ProjectsService {
    @GET("projects/")
    suspend fun getProjects(): List<Project>
}
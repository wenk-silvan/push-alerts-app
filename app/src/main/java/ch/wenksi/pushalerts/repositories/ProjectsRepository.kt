package ch.wenksi.pushalerts.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.wenksi.pushalerts.util.Constants
import ch.wenksi.pushalerts.errors.ProjectsRetrievalError
import ch.wenksi.pushalerts.models.Project
import ch.wenksi.pushalerts.services.projects.ProjectsServiceFactory
import ch.wenksi.pushalerts.services.projects.ProjectsService
import kotlinx.coroutines.withTimeout

/**
 * This class manages all operations related to the projects using the ProjectsService
 */
class ProjectsRepository {
    private val projectsService: ProjectsService = ProjectsServiceFactory.createApi()
    private val _projects: MutableLiveData<List<Project>> = MutableLiveData()
    private val _error: MutableLiveData<String> = MutableLiveData()
    private val _logoutRequest: MutableLiveData<Boolean> = MutableLiveData()

    val projects: LiveData<List<Project>> get() = _projects
    val error: LiveData<String> get() = _error
    val logoutRequest: LiveData<Boolean> get() = _logoutRequest

    /**
     * Gets the projects and triggers the projects live data on successful response
     * Triggers the logoutRequest live data if the error has status code 401
     * Triggers the error live data if an error occurs
     * @param userUUID is the unique identifier of the user
     * @throws ProjectsRetrievalError if an error occurs during the request
     */
    suspend fun getProjects(userUUID: String) {
        try {
            val result = withTimeout(Constants.apiTimeout) {
                projectsService.getProjects(userUUID)
            }
            Log.i(ProjectsRepository::class.qualifiedName, "Fetched projects: \n${result}")
            _projects.value = result
        } catch (e: Exception) {
            if (e.message != null && e.message!!.contains("401")) _logoutRequest.value = true
            _error.value = "Error while fetching projects"
            throw ProjectsRetrievalError("Error while fetching projects from web server: \n${e.message}")
        }
    }
}



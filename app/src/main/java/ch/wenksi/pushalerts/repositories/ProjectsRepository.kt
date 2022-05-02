package ch.wenksi.pushalerts.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.wenksi.pushalerts.util.Constants
import ch.wenksi.pushalerts.errors.ProjectsRetrievalError
import ch.wenksi.pushalerts.models.Project
import ch.wenksi.pushalerts.services.projects.ProjectsServiceFactory
import ch.wenksi.pushalerts.services.projects.ProjectsService
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.withTimeout
import java.io.IOException


private const val jsonFileName = "projects.json"

class ProjectsRepository() {
    private val projectsService: ProjectsService = ProjectsServiceFactory.createApi()
    private val _projects: MutableLiveData<List<Project>> = MutableLiveData()
    private val _error: MutableLiveData<String> = MutableLiveData()
    private val _logoutRequest: MutableLiveData<Boolean> = MutableLiveData()

    val projects: LiveData<List<Project>> get() = _projects
    val error: LiveData<String> get() = _error
    val logoutRequest: LiveData<Boolean> get() = _logoutRequest

    suspend fun getProjectsFromJson(context: Context) {
        try {
            val jsonString = context.assets.open(jsonFileName)
                .bufferedReader().use { it.readText() }
            Log.i("data", jsonString)
            val gson = GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss").create();
            val listProjectType = object : TypeToken<List<Project>>() {}.type
            var tempProjects: List<Project> = gson.fromJson(jsonString, listProjectType)
            _projects.value = tempProjects
        } catch (e: IOException) {
            throw ProjectsRetrievalError("Can't open json file: \n${e.message}")
        } catch (e: Exception) {
            throw ProjectsRetrievalError("Error while fetching projects from json file: \n${e.message}")
        }
    }

    suspend fun getProjectsFromServer() {
        try {
            val result = withTimeout(Constants.apiTimeout) {
                projectsService.getProjects()
            }
            _projects.value = result
        } catch (e: Exception) {
            if (e.message != null && e.message!!.contains("401")) _logoutRequest.value = true
            _error.value = "Error while closing tasks"
            throw ProjectsRetrievalError("Error while fetching projects from web server: \n${e.message}")
        }
    }
}



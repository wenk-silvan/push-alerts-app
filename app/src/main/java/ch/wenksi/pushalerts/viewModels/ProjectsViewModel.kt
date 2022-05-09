package ch.wenksi.pushalerts.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ch.wenksi.pushalerts.models.Project
import ch.wenksi.pushalerts.errors.ProjectsRetrievalError
import ch.wenksi.pushalerts.repositories.ProjectsRepository
import kotlinx.coroutines.launch
import java.util.*

/**
 * ViewModel for project data to encapsulate this logic from the user interface (fragments).
 */
class ProjectsViewModel(application: Application) : AndroidViewModel(application) {
    var selectedProjectUUID: UUID? = null

    private val repository = ProjectsRepository()

    var projects: LiveData<List<Project>> = repository.projects
    var error: LiveData<String> = repository.error
    var logoutRequest: LiveData<Boolean> = repository.logoutRequest

    /**
     * Gets the projects using a ProjectsRepository
     * @param userUUID is the unique identifier of the user
     */
    fun getProjects(userUUID: String) {
        viewModelScope.launch {
            try {
                repository.getProjects(userUUID)
            } catch (error: ProjectsRetrievalError) {
                Log.e("Error while fetching projects", error.message.toString())
            }
        }
    }

    /**
     * Gets the project with the given menu id.
     * @param menuId is the id of the menu entry in the navigation drawer
     */
    fun getProject(menuId: Int): Project? {
        return projects.value?.first() { p -> p.menuId == menuId }
    }
}

package ch.wenksi.pushalerts.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ch.wenksi.pushalerts.models.Project
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.TaskState
import ch.wenksi.pushalerts.errors.ProjectsRetrievalError
import ch.wenksi.pushalerts.repositories.ProjectsRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class ProjectsViewModel(application: Application) : AndroidViewModel(application) {
    var selectedProjectUUID: UUID? = null

    private val repository = ProjectsRepository()

    var projects: LiveData<List<Project>> = repository.projects

    fun getProjects() {
        viewModelScope.launch {
            try {
                repository.getProjectsFromServer()
            } catch (error: ProjectsRetrievalError) {
                Log.e("Error while fetching projects", error.message.toString())
            }
        }
    }

    fun getProjectByMenuId(menuId: Int): Project? {
        return projects.value?.first() { p -> p.menuId == menuId }
    }
}

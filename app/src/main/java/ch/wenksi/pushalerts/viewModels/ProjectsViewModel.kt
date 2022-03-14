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
    lateinit var selectedProjectUUID: UUID

    private val repository = ProjectsRepository()

    var projects: LiveData<List<Project>> = repository.projects

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String> get() = _errorText

    fun getProjects(isRefresh: Boolean) {
        _errorText.value = null
        if (!isRefresh && repository.projects.value != null) {
            return
        }

        viewModelScope.launch {
            try {
                repository.getProjectsFromJson(getApplication())
            } catch (error: ProjectsRetrievalError) {
                _errorText.value = error.message
                Log.e("Error while fetching projects", error.message.toString())
            }
        }
    }
}

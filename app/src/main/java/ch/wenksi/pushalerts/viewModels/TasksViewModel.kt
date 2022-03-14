package ch.wenksi.pushalerts.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.errors.TasksRetrievalError
import ch.wenksi.pushalerts.models.TaskState
import ch.wenksi.pushalerts.repositories.TasksRepository
import kotlinx.coroutines.launch
import java.util.*

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TasksRepository()

    var tasks: LiveData<List<Task>> = repository.tasks

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String> get() = _errorText

    fun getTasks(isRefresh: Boolean, projectUUID: UUID) {
        _errorText.value = null
        if (!isRefresh && repository.tasks.value != null) {
            return
        }

        viewModelScope.launch {
            try {
                repository.getTasksFromJson(getApplication())
            } catch (error: TasksRetrievalError) {
                _errorText.value = error.message
                Log.e("Error while fetching projects", error.message.toString())
            }
        }
    }

    fun getTasks(state: TaskState): List<Task>? {
        return tasks.value?.filter { t -> t.state == state }
    }

    fun getOpenTasks(): List<Task>? {
        return tasks.value?.filter { t -> t.state == TaskState.Opened || t.state == TaskState.Assigned }
    }

    fun getClosedTasks(): List<Task>? {
        return tasks.value?.filter { t -> t.state == TaskState.Done || t.state == TaskState.Rejected }
    }

    fun getMyOpenTasks(uuid: UUID): List<Task>? {
        return tasks.value?.filter { t -> t.user?.uuid == uuid }
    }

    fun getMyClosedTasks(uuid: UUID): List<Task>? {
        return tasks.value?.filter { t -> t.user?.uuid == uuid }
    }

    fun getTask(uuid: String?): Task {
        var task: Task? = tasks.value?.first { t -> t.uuid.toString() == uuid }
            ?: throw TasksRetrievalError("Task with uuid: ${uuid.toString()} not found.")
        return task!!
    }
}

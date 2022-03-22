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

    fun getTasks(projectUUID: UUID) {
        _errorText.value = null
//        if (!isRefresh && repository.tasks.value != null) {
//            return
//        }
        // TODO: Implement caching

        viewModelScope.launch {
            try {
                repository.getTasksFromJson(getApplication())
            } catch (error: TasksRetrievalError) {
                _errorText.value = error.message
                Log.e("Error while fetching projects", error.message.toString())
            }
        }
    }

    fun getTasks(state: TaskState, tasks: List<Task>): List<Task> {
        return tasks.filter { t -> t.state == state }
    }

    fun getOpenTasks(tasks: List<Task>): List<Task> {
        return tasks.filter { t -> t.state == TaskState.Opened || t.state == TaskState.Assigned }
    }

    fun getClosedTasks(tasks: List<Task>): List<Task> {
        return tasks.filter { t -> t.state == TaskState.Done || t.state == TaskState.Rejected }
    }

    fun getTasksOfUser(uuid: UUID, tasks: List<Task>): List<Task> {
        return tasks.filter { t -> t.user?.uuid == uuid }
    }

    fun getTask(uuid: String?): Task {
        return tasks.value?.first { t -> t.uuid.toString() == uuid }
            ?: throw TasksRetrievalError("Task with uuid: ${uuid.toString()} not found.")
    }
}

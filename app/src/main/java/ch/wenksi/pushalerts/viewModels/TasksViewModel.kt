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

    fun getTasks(projectUuid: UUID) {
        _errorText.value = null
//        if (!isRefresh && repository.tasks.value != null) {
//            return
//        }
        // TODO: Implement caching

        viewModelScope.launch {
            try {
                repository.getTasksFromServer(projectUuid)
            } catch (error: TasksRetrievalError) {
                _errorText.value = error.message
                Log.e("Error while fetching projects", error.message.toString())
            }
        }
    }

    fun getTasks(state: TaskState, tasks: List<Task>): List<Task> {
        return tasks.filter { t -> t.status == state }
    }

    fun getOpenTasks(tasks: List<Task>): List<Task> {
        return tasks.filter { t -> t.status == TaskState.Opened || t.status == TaskState.Assigned }
    }

    fun getClosedTasks(tasks: List<Task>): List<Task> {
        return tasks.filter { t -> t.status == TaskState.Done || t.status == TaskState.Rejected }
    }

    fun getTasksOfUser(email: String, tasks: List<Task>): List<Task> {
        return tasks.filter { t -> t.userEmail == email }
    }

    fun getTask(uuid: String?): Task {
        return tasks.value?.first { t -> t.uuid.toString() == uuid }
            ?: throw TasksRetrievalError("Task with uuid: ${uuid.toString()} not found.")
    }
}

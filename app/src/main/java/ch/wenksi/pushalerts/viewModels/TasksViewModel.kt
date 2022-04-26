package ch.wenksi.pushalerts.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ch.wenksi.pushalerts.errors.TaskUpdateError
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.errors.TasksRetrievalError
import ch.wenksi.pushalerts.models.TaskState
import ch.wenksi.pushalerts.repositories.TasksRepository
import kotlinx.coroutines.launch
import java.util.*

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TasksRepository()

    var tasks: LiveData<List<Task>> = repository.tasks
    var taskUpdate: LiveData<Boolean> = repository.taskUpdate

    fun assignTask(task: Task, userUUID: UUID, userEmail: String) {
        task.assign(userEmail)
        viewModelScope.launch {
            try {
                repository.assignTask(task, userUUID)
            } catch (error: TaskUpdateError) {
                Log.e("Error while assigning task with uuid ${task.uuid}", error.message.toString())
            }
        }
    }

    fun rejectTask(task: Task) {
        task.reject()
        viewModelScope.launch {
            try {
                repository.closeTask(task.uuid, TaskState.Rejected)
            } catch (error: TaskUpdateError) {
                Log.e("Error while closing task with uuid ${task.uuid}", error.message.toString())
            }
        }
    }

    fun finishTask(task: Task) {
        task.finish()
        viewModelScope.launch {
            try {
                repository.closeTask(task.uuid, TaskState.Finished)
            } catch (error: TaskUpdateError) {
                Log.e("Error while closing task with uuid ${task.uuid}", error.message.toString())
            }
        }
    }

    fun getTasks(projectUuid: UUID) {
        viewModelScope.launch {
            try {
                repository.getTasksFromServer(projectUuid)
            } catch (error: TasksRetrievalError) {
                Log.e("Error while fetching tasks", error.message.toString())
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
        return tasks.filter { t -> t.status == TaskState.Finished || t.status == TaskState.Rejected }
    }

    fun getTasksOfUser(email: String, tasks: List<Task>): List<Task> {
        return tasks.filter { t -> t.userEmail == email }
    }

    fun getTask(uuid: String?): Task {
        return tasks.value?.first { t -> t.uuid.toString() == uuid }
            ?: throw TasksRetrievalError("Task with uuid: ${uuid.toString()} not found.")
    }
}

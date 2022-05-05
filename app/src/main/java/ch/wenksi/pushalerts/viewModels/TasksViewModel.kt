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

/**
 * ViewModel for task data to encapsulate this logic from the user interface (fragments).
 */
class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TasksRepository()

    var tasks: LiveData<List<Task>> = repository.tasks
    var taskUpdate: LiveData<Boolean> = repository.taskUpdate
    var error: LiveData<String> = repository.error
    var logoutRequest: LiveData<Boolean> = repository.logoutRequest

    /**
     * Assigns a task to the user using a TasksRepository
     * @param task is the task
     * @param userEmail is the email of the user that is assigned
     * @param userUUID is the uuid of the user that is assigned
     */
    fun assignTask(task: Task, userUUID: String, userEmail: String) {
        task.assign(userEmail)
        viewModelScope.launch {
            try {
                repository.assignTask(task, userUUID)
            } catch (error: TaskUpdateError) {
                Log.e("Error while assigning task with uuid ${task.uuid}", error.message.toString())
            }
        }
    }

    /**
     * Rejects a task to the user using a TasksRepository
     * @param task is the task
     */
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

    /**
     * Finishes a task to the user using a TasksRepository
     * @param task is the task
     */
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


    /**
     * Gets the tasks of the project using a TasksRepository
     * @param projectUUID is the uuid of the project
     */
    fun getTasks(projectUUID: UUID) {
        viewModelScope.launch {
            try {
                repository.getTasksFromServer(projectUUID)
            } catch (error: TasksRetrievalError) {
                Log.e("Error while fetching tasks", error.message.toString())
            }
        }
    }

    /**
     * Filters the given tasks by the given TaskState
     */
    fun getTasks(state: TaskState, tasks: List<Task>): List<Task> {
        return tasks.filter { t -> t.status == state }
    }

    /**
     * Filers the given tasks to open tasks. These are TaskState.Opened and TaskState.Assigned
     */
    fun getOpenTasks(tasks: List<Task>): List<Task> {
        return tasks.filter { t -> t.status == TaskState.Opened || t.status == TaskState.Assigned }
    }

    /**
     * Filers the given tasks to closed tasks. These are TaskState.Finished and TaskState.Rejected
     */
    fun getClosedTasks(tasks: List<Task>): List<Task> {
        return tasks.filter { t -> t.status == TaskState.Finished || t.status == TaskState.Rejected }
    }

    /**
     * Filters the given tasks with the user email.
     */
    fun getTasksOfUser(email: String, tasks: List<Task>): List<Task> {
        return tasks.filter { t -> t.userEmail == email }
    }

    /**
     * Filters the tasks with the task uuid.
     */
    fun getTask(uuid: String?): Task {
        return tasks.value?.first { t -> t.uuid.toString() == uuid }
            ?: throw TasksRetrievalError("Task with uuid: ${uuid.toString()} not found.")
    }
}

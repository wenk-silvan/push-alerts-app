package ch.wenksi.pushalerts.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.wenksi.pushalerts.errors.ProjectsRetrievalError
import ch.wenksi.pushalerts.errors.TaskUpdateError
import ch.wenksi.pushalerts.errors.TasksRetrievalError
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.TaskState
import ch.wenksi.pushalerts.services.tasks.TasksServiceFactory
import ch.wenksi.pushalerts.services.tasks.TasksService
import ch.wenksi.pushalerts.util.Constants
import kotlinx.coroutines.withTimeout
import java.util.*

/**
 * This class manages all operations related to the tasks using the TasksService
 */
class TasksRepository() {
    private val tasksService: TasksService = TasksServiceFactory.createApi()
    private val _tasks: MutableLiveData<List<Task>> = MutableLiveData()
    private val _taskUpdate: MutableLiveData<Boolean> = MutableLiveData()
    private val _error: MutableLiveData<String> = MutableLiveData()
    private val _logoutRequest: MutableLiveData<Boolean> = MutableLiveData()

    val tasks: LiveData<List<Task>> get() = _tasks
    val taskUpdate: LiveData<Boolean> get() = _taskUpdate
    val error: LiveData<String> get() = _error
    val logoutRequest: LiveData<Boolean> get() = _logoutRequest

    /**
     * Gets the tasks of the project with the given uuid and triggers the tasks live data on successful response
     * Triggers the logoutRequest live data if the error has status code 401
     * Triggers the error live data if an error occurs
     * @param projectUuid is the uuid of a project
     * @throws ProjectsRetrievalError if an error occurs during the request
     */
    suspend fun getTasks(projectUuid: UUID) {
        try {
            val result = withTimeout(Constants.apiTimeout) {
                tasksService.getTasks(projectUuid.toString())
            }
            Log.i(TasksRepository::class.qualifiedName, "Fetched tasks: \n${result}")
            _tasks.value = result
        } catch (e: Exception) {
            if (e.message != null && e.message!!.contains("401")) _logoutRequest.value = true
            _error.value = "Error while fetching tasks"
            throw TasksRetrievalError("Error while fetching tasks from web server: \n${e.message}")
        }
    }

    /**
     * Assigns the task to the user and triggers the taskUpdate live data on successful response
     * Triggers the logoutRequest live data if the error has status code 401
     * Triggers the error live data if an error occurs
     * @param task is the task which should be updated
     * @param userUUID is the uuid of the user which is assigned to the task
     * @throws TaskUpdateError if an error occurs during the request
     */
    suspend fun assignTask(task: Task, userUUID: String) {
        try {
            withTimeout(Constants.apiTimeout) {
                tasksService.assignTask(task.uuid.toString(), userUUID)
            }
            Log.i(
                TasksRepository::class.qualifiedName,
                "Assigned user: $userUUID to task: \n${task.uuid}"
            )
            _taskUpdate.value = true
        } catch (e: Exception) {
            if (e.message != null && e.message!!.contains("401")) _logoutRequest.value = true
            _error.value = "Error while assigning tasks"
            throw TaskUpdateError("Error while assigning task with uuid ${task.uuid}: \n${e.message}")
        }
    }

    /**
     * Closed the task and triggers the taskUpdate live data on successful response
     * Triggers the logoutRequest live data if the error has status code 401
     * Triggers the error live data if an error occurs
     * @param taskUUID is the uuid of the task which should be updated
     * @param state must either be finished or rejected
     * @throws TaskUpdateError if an error occurs during the request
     */
    suspend fun closeTask(taskUUID: UUID, state: TaskState) {
        try {
            withTimeout(Constants.apiTimeout) {
                tasksService.close(taskUUID.toString(), state.ordinal)
            }
            Log.i(
                TasksRepository::class.qualifiedName,
                "Closed task: $taskUUID to state: \n$state"
            )
            _taskUpdate.value = true
        } catch (e: Exception) {
            if (e.message != null && e.message!!.contains("401")) _logoutRequest.value = true
            _error.value = "Error while closing tasks"
            throw TaskUpdateError("Error while closing task with uuid ${taskUUID}: \n${e.message}")
        }
    }
}



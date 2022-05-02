package ch.wenksi.pushalerts.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.wenksi.pushalerts.errors.TasksRetrievalError
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.TaskState
import ch.wenksi.pushalerts.services.tasks.TasksServiceFactory
import ch.wenksi.pushalerts.services.tasks.TasksService
import ch.wenksi.pushalerts.util.Constants
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.withTimeout
import java.io.IOException
import java.util.*


private const val jsonFileName = "tasks.json"

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

    suspend fun getTasksFromJson(context: Context) {
        try {
            val jsonString = context.assets.open(jsonFileName)
                .bufferedReader().use { it.readText() }
            Log.i("data", jsonString)
            val gson = GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss").create();
            val listTaskType = object : TypeToken<List<Task>>() {}.type
            val tempTasks: List<Task> = gson.fromJson(jsonString, listTaskType)
            _tasks.value = tempTasks
        } catch (e: IOException) {
            throw TasksRetrievalError("Can't open json file: \n${e.message}")
        } catch (e: Exception) {
            throw TasksRetrievalError("Error while fetching tasks from json file: \n${e.message}")
        }
    }

    suspend fun getTasksFromServer(projectUuid: UUID) {
        try {
            val result = withTimeout(Constants.apiTimeout) {
                tasksService.getTasks(projectUuid.toString())
            }
            _tasks.value = result
        } catch (e: Exception) {
            if (e.message != null && e.message!!.contains("401")) _logoutRequest.value = true
            _error.value = "Error while fetching tasks"
            throw TasksRetrievalError("Error while fetching tasks from web server: \n${e.message}")
        }
    }

    suspend fun assignTask(task: Task, userUUID: UUID) {
        try {
            withTimeout(Constants.apiTimeout) {
                tasksService.assignTask(task.uuid.toString(), userUUID.toString())
            }
            _taskUpdate.value = true
        } catch (e: Exception) {
            if (e.message != null && e.message!!.contains("401")) _logoutRequest.value = true
            _error.value = "Error while assigning tasks"
            throw TasksRetrievalError("Error while assigning task with uuid ${task.uuid}: \n${e.message}")
        }
    }

    suspend fun closeTask(taskUUID: UUID, state: TaskState) {
        try {
            withTimeout(Constants.apiTimeout) {
                tasksService.close(taskUUID.toString(), state.ordinal)
            }
            _taskUpdate.value = true
        } catch (e: Exception) {
            if (e.message != null && e.message!!.contains("401")) _logoutRequest.value = true
            _error.value = "Error while closing tasks"
            throw TasksRetrievalError("Error while closing task with uuid ${taskUUID}: \n${e.message}")
        }
    }
}



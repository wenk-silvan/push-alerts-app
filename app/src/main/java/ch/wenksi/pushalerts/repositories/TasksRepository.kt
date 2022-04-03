package ch.wenksi.pushalerts.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.wenksi.pushalerts.Constants
import ch.wenksi.pushalerts.errors.TasksRetrievalError
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.TaskState
import ch.wenksi.pushalerts.services.tasks.TasksApiService
import ch.wenksi.pushalerts.services.tasks.TasksService
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.withTimeout
import java.io.IOException
import java.util.*


private const val jsonFileName = "tasks.json"

class TasksRepository() {
    private val tasksService: TasksService = TasksApiService.createApi()
    private val _tasks: MutableLiveData<List<Task>> = MutableLiveData()

    val tasks: LiveData<List<Task>> get() = _tasks

    suspend fun getTasksFromJson(context: Context) {
        try {
            val jsonString = context.assets.open(jsonFileName)
                .bufferedReader().use { it.readText() }
            Log.i("data", jsonString)
            val gson = GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss").create();
            val listTaskType = object : TypeToken<List<Task>>() {}.type
            var tempTasks: List<Task> = gson.fromJson(jsonString, listTaskType)
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
            throw TasksRetrievalError("Error while fetching tasks from web server: \n${e.message}")
        }
    }

    suspend fun assignTask(taskUUID: UUID, userUUID: UUID) {
        try {
            val result = withTimeout(Constants.apiTimeout) {
                tasksService.assignTask(taskUUID.toString(), userUUID.toString())
            }
            Log.i("", result.toString());  // TODO: Fix logging
        } catch (e: Exception) {
            throw TasksRetrievalError("Error while assigning task with uuid ${taskUUID}: \n${e.message}")
        }
    }

    suspend fun closeTask(taskUUID: UUID, state: TaskState) {
        try {
            val result = withTimeout(Constants.apiTimeout) {
                tasksService.close(taskUUID.toString(), state)
            }
            Log.i("", result.toString());  // TODO: Fix logging
        } catch (e: Exception) {
            throw TasksRetrievalError("Error while assigning task with uuid ${taskUUID}: \n${e.message}")
        }
    }
}



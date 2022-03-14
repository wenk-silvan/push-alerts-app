package ch.wenksi.pushalerts.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.wenksi.pushalerts.errors.ProjectsRetrievalError
import ch.wenksi.pushalerts.errors.TasksRetrievalError
import ch.wenksi.pushalerts.models.Task
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.*


private const val jsonFileName = "tasks.json"

class TasksRepository() {
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
        }
        catch (e: IOException) {
            throw TasksRetrievalError("Can't open json file: \n${e.message}")
        }
        catch (e: Exception) {
            throw TasksRetrievalError("Error while fetching tasks from json file: \n${e.message}")
        }
    }

    suspend fun getTasksFromServer(uuid: UUID) {
        try {

        } catch (e: Exception) {
            throw TasksRetrievalError("Error while fetching tasks from web server: \n${e.message}")
        }
    }
}



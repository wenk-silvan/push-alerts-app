package ch.wenksi.pushalerts.services.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.wenksi.pushalerts.models.Task

class TasksRepository {
    private val _tasks: MutableLiveData<List<Task>> = MutableLiveData()

    val tasks: LiveData<List<Task>> get() = _tasks

    suspend fun getTasksFromJson() {
        try {

        } catch (e: Exception) {
            throw TasksRetrievalError("Error while fetching tasks from json file: \n${e.message}")
        }
    }

    suspend fun getTasksFromServer() {
        try {

        } catch (e: Exception) {
            throw TasksRetrievalError("Error while fetching tasks from web server: \n${e.message}")
        }
    }
}



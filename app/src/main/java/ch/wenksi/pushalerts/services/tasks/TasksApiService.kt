package ch.wenksi.pushalerts.services.tasks

import ch.wenksi.pushalerts.util.Constants
import ch.wenksi.pushalerts.models.TaskState
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TasksApiService {
    companion object {
        fun createApi(): TasksService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.apiBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder()
                            .registerTypeAdapter(TaskState::class.java, TaskStateDeserializer())
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS")
                            .create()
                    )
                )
                .build()
                .create(TasksService::class.java)
        }
    }
}
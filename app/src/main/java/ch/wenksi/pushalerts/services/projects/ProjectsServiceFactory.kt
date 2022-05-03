package ch.wenksi.pushalerts.services.projects

import ch.wenksi.pushalerts.services.auth.AuthInterceptor
import ch.wenksi.pushalerts.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProjectsServiceFactory {
    companion object {
        /**
         * Configures and creates a new ProjectsService instance using the retrofit builder
         */
        fun createApi(): ProjectsService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.apiBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProjectsService::class.java)
        }
    }
}
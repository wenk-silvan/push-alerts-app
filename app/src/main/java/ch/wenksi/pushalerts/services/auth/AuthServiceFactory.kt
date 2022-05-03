package ch.wenksi.pushalerts.services.auth

import ch.wenksi.pushalerts.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthServiceFactory {
    companion object {
        /**
         * Configures and creates a new AuthService instance using the retrofit builder
         */
        fun createApi(): AuthService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.apiBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthService::class.java)
        }
    }
}
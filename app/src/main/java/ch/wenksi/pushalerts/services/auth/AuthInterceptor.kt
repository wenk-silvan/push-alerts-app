package ch.wenksi.pushalerts.services.auth

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import java.security.InvalidKeyException
import kotlin.jvm.Throws

/**
 * This class is used to intercept http requests before they are sent across the network.
 * It reads the session token from the SessionManager and appends it to the request header.
 */
class AuthInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val invalidCredentialsResponse = Response.Builder()
            .code(401)
            .body("".toResponseBody(null))
            .protocol(Protocol.HTTP_2)
            .message("Invalid credentials")
            .request(request)
            .build()

        return try {
            val authenticatedRequest = addTokenToRequest(request)
            Log.d(
                AuthInterceptor::class.qualifiedName,
                "Added token to request header: $authenticatedRequest"
            )
            chain.proceed(authenticatedRequest)
        } catch (error: Throwable) {
            Log.e(
                AuthInterceptor::class.qualifiedName,
                "Failed to add token to request header, error: ${error.message}"
            )
            invalidCredentialsResponse
        }
    }

    private fun addTokenToRequest(request: Request): Request {
        val apiToken: String = SessionManager.requireToken().value
        return request.newBuilder()
            .header("Authorization", "Bearer $apiToken")
            .build()
    }
}
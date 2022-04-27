package ch.wenksi.pushalerts.services.login

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import java.security.InvalidKeyException
import kotlin.jvm.Throws

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
            chain.proceed(addTokenToRequest(request))
        } catch (error: Throwable) {
            invalidCredentialsResponse
        }
    }

    private fun addTokenToRequest(request: Request): Request {
        val apiToken: String? = SessionManager.requireToken().value

        if (apiToken.isNullOrBlank()) {
            throw InvalidKeyException("No API key provided")
        }

        return request.newBuilder()
            .header("Authorization", "Bearer $apiToken")
            .build()
    }
}
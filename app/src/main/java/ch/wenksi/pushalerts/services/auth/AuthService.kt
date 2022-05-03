package ch.wenksi.pushalerts.services.auth

import ch.wenksi.pushalerts.models.Credentials
import ch.wenksi.pushalerts.models.Token
import ch.wenksi.pushalerts.util.Constants
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Represents the http requests regarding authentication
 */
interface AuthService {
    /**
     * Calls the login endpoint of the PushAlerts api
     * @param body is the credentials to log in the user
     */
    @POST(Constants.apiLoginUri)
    suspend fun login(@Body body: Credentials): Token
}
package ch.wenksi.pushalerts.services.login

import ch.wenksi.pushalerts.models.Credentials
import ch.wenksi.pushalerts.models.Token
import ch.wenksi.pushalerts.util.Constants
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST(Constants.apiLoginUri)
    suspend fun login(@Body body: Credentials): Token
}
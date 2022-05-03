package ch.wenksi.pushalerts.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.wenksi.pushalerts.errors.AuthenticationError
import ch.wenksi.pushalerts.util.Constants
import ch.wenksi.pushalerts.models.Credentials
import ch.wenksi.pushalerts.models.Token
import ch.wenksi.pushalerts.services.auth.AuthServiceFactory
import ch.wenksi.pushalerts.services.auth.AuthService
import kotlinx.coroutines.withTimeout

/**
 * This class manages all operations related to the user authentication using the UsersService
 */
class UserRepository() {
    private val authService: AuthService = AuthServiceFactory.createApi()
    private val _token: MutableLiveData<Token> = MutableLiveData()
    private val _error: MutableLiveData<String> = MutableLiveData()

    val token: LiveData<Token> get() = _token
    val error: LiveData<String> get() = _error

    /**
     * Logs in the user and triggers the token live data on successful response
     * Triggers the logoutRequest live data if the error has status code 401
     * Triggers the error live data if an error occurs
     * @throws AuthenticationError if an error occurs during the request
     */
    suspend fun login(credentials: Credentials) {
        try {
            val result = withTimeout(Constants.apiTimeout) {
                authService.login(credentials)
            }
            Log.i("TOKEN Value", result.value)
            Log.i("TOKEN Expiry", result.expiryUtc.toString())
            _token.value = result
        } catch (e: Exception) {
            _error.value = "Login failed"
            throw AuthenticationError("Error while login in user: \n${e.message}")
        }
    }
}



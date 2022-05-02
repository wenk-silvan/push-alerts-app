package ch.wenksi.pushalerts.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.wenksi.pushalerts.errors.AuthenticationError
import ch.wenksi.pushalerts.util.Constants
import ch.wenksi.pushalerts.models.Credentials
import ch.wenksi.pushalerts.models.Token
import ch.wenksi.pushalerts.services.login.LoginServiceFactory
import ch.wenksi.pushalerts.services.login.LoginService
import kotlinx.coroutines.withTimeout

class UserRepository() {
    private val loginService: LoginService = LoginServiceFactory.createApi()
    private val _token: MutableLiveData<Token> = MutableLiveData()
    private val _error: MutableLiveData<String> = MutableLiveData()

    val token: LiveData<Token> get() = _token
    val error: LiveData<String> get() = _error

    suspend fun login(credentials: Credentials) {
        try {
            val result = withTimeout(Constants.apiTimeout) {
                loginService.login(credentials)
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



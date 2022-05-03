package ch.wenksi.pushalerts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.wenksi.pushalerts.databinding.ActivityAuthBinding
import ch.wenksi.pushalerts.services.auth.SessionManager

/**
 * This activity is launched on startup of the app. It contains the login screens and
 * navigates to the MainActivity after login or if there is already an existing session.
 */
class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.init(application)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
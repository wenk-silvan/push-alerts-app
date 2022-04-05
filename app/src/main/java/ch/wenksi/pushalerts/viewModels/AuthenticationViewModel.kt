package ch.wenksi.pushalerts.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.User
import java.util.*

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {
    var user = User(
        UUID.fromString("d94a2afa-1962-4329-a79c-9722de0f20d2"),
        "alice@company.com"
    )

    fun isAssignedToMe(task: Task): Boolean {
        return user.email == task.userEmail
    }
}

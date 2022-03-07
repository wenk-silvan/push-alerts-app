package ch.wenksi.pushalerts.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.User
import java.util.*

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {
    val user = User(
        UUID.fromString("c8474f18-7e90-416b-ab1c-f150630ddec7"),
        "user@mail.com"
    )

    fun isAssignedToMe(task: Task): Boolean {
        return user.uuid == task.user?.uuid
    }
}

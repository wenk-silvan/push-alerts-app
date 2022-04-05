package ch.wenksi.pushalerts.viewModels

import android.app.Application
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.User
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class AuthenticationViewModelTest {
    private lateinit var viewModel: AuthenticationViewModel

    @Before
    fun setUp() {
        val applicationMock = mockk<Application>()
        viewModel = AuthenticationViewModel(applicationMock)
        viewModel.user = User(email = "alice@company.com")
    }

    @Test
    fun `Check if task is assigned to authenticated user, same user`() {
        val task = Task(title = "Unit Test Task")
        task.assign(viewModel.user)
        val result = viewModel.isAssignedToMe(task)
        assertThat(result).isTrue()
    }

    @Test
    fun `Check if task is assigned to authenticated user, different user`() {
        val task = Task(title = "Unit Test Task")
        val user = User(email = "bob.company.com")
        task.assign(user)
        val result = viewModel.isAssignedToMe(task)
        assertThat(result).isFalse()
    }
}
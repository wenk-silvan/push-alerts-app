package ch.wenksi.pushalerts.viewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.TaskState
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Assert.assertThrows

import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.NoSuchElementException

class TasksViewModelTest {
    private lateinit var viewModel: TasksViewModel
    private lateinit var tasks: List<Task>
    private lateinit var alice: User
    private lateinit var bob: User
    private lateinit var randomUUID: UUID

    @Before
    fun setUp() {
        val applicationMock = mockk<Application>()
        viewModel = TasksViewModel(applicationMock)
        alice = User(email = "alice@company.com")
        bob = User(email = "bob@company.com")
        randomUUID = UUID.randomUUID()
        tasks = listOf(
            // Unassigned Tasks
            Task(title = "A", uuid = randomUUID),
            Task(title = "B"),
            // Assigned Tasks
            Task(title = "C").assign(alice),
            Task(title = "D").assign(bob),
            // Finished Tasks
            Task(title = "E").assign(alice).finish(),
            Task(title = "F").assign(bob).finish(),
            Task(title = "G").assign(bob).finish(),
            // Rejected Tasks
            Task(title = "H").assign(bob).reject()
        )
        viewModel.tasks = MutableLiveData(tasks)
    }

    @Test
    fun `Get tasks, returns opened tasks`() {
        val tasks = viewModel.getTasks(TaskState.Opened, tasks)
        assertThat(tasks).hasSize(2)
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "A" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "B" })
    }

    @Test
    fun `Get tasks, returns empty list`() {
        val tasks = viewModel.getTasks(TaskState.Opened, listOf())
        assertThat(tasks).isEmpty()
    }

    @Test
    fun `Get tasks, returns assigned tasks`() {
        val tasks = viewModel.getTasks(TaskState.Assigned, tasks)
        assertThat(tasks).hasSize(2)
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "C" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "D" })
    }

    @Test
    fun `Get tasks, returns finished tasks`() {
        val tasks = viewModel.getTasks(TaskState.Finished, tasks)
        assertThat(tasks).hasSize(3)
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "E" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "F" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "G" })
    }

    @Test
    fun `Get tasks, returns rejected tasks`() {
        val tasks = viewModel.getTasks(TaskState.Rejected, tasks)
        assertThat(tasks).hasSize(1)
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "H" })
    }

    @Test
    fun `Get open tasks`() {
        val tasks = viewModel.getOpenTasks(tasks)
        assertThat(tasks).hasSize(4)
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "A" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "B" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "C" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "D" })
    }

    @Test
    fun `Get open tasks, returns empty list`() {
        val tasks = viewModel.getOpenTasks(listOf())
        assertThat(tasks).isEmpty()
    }

    @Test
    fun `Get closed tasks`() {
        val tasks = viewModel.getClosedTasks(tasks)
        assertThat(tasks).hasSize(4)
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "E" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "F" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "G" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "H" })
    }

    @Test
    fun `Get closed tasks, returns empty list`() {
        val tasks = viewModel.getClosedTasks(listOf())
        assertThat(tasks).isEmpty()
    }

    @Test
    fun `Get tasks of user, returns tasks of Alice`() {
        val tasks = viewModel.getTasksOfUser(alice.email, tasks)
        assertThat(tasks).hasSize(2)
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "C" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "E" })
    }

    @Test
    fun `Get tasks of user, returns tasks of Bob`() {
        val tasks = viewModel.getTasksOfUser(bob.email, tasks)
        assertThat(tasks).hasSize(4)
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "D" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "F" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "G" })
        assertThat(tasks).contains(this.tasks.first { t -> t.title == "H" })
    }

    @Test
    fun `Get tasks of user, returns tasks of Charlie`() {
        val charlie = User(email = "charlie@company.com")
        val tasks = viewModel.getTasksOfUser(charlie.email, tasks)
        assertThat(tasks).isEmpty()
    }

    @Test
    fun `Get task with given uuid, task found`() {
        val task = viewModel.getTask(randomUUID.toString())
        assertThat(task).isNotNull()
        assertThat(task).isEqualTo(this.tasks.first { t -> t.title == "A" })
    }

    @Test
    fun `Get task with given uuid, task not found`() {
        assertThrows(NoSuchElementException::class.java) {
            viewModel.getTask(UUID.randomUUID().toString())
        }
    }
}
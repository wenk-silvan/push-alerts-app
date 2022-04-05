package ch.wenksi.pushalerts.viewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import ch.wenksi.pushalerts.models.Project
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test


class ProjectsViewModelTest {
    private lateinit var viewModel: ProjectsViewModel
    private lateinit var projects: List<Project>

    @Before
    fun setUp() {
        val applicationMock = mockk<Application>()
        viewModel = ProjectsViewModel(applicationMock)
        projects = listOf(
            Project(name = "Alpha", menuId = 1),
            Project(name = "Beta", menuId = 2),
            Project(name = "Gamma", menuId = 3),
        )
        viewModel.projects = MutableLiveData(projects)
    }

    @Test
    fun `Get project by menu id, found existing`() {
        val project = viewModel.getProjectByMenuId(1)
        assertThat(project).isIn(projects)
    }

    @Test
    fun `Get project by menu id, found none`() {
        assertThrows(NoSuchElementException::class.java) {
            viewModel.getProjectByMenuId(-1)
        }
    }
}
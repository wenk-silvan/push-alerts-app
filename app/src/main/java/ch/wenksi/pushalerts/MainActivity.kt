package ch.wenksi.pushalerts

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import ch.wenksi.pushalerts.databinding.ActivityMainBinding
import ch.wenksi.pushalerts.models.Project
import ch.wenksi.pushalerts.viewModels.ProjectsViewModel
import ch.wenksi.pushalerts.viewModels.TasksViewModel

// 0 - 1000 is reserved for project menu items.
const val MENU_ID_LOGOUT = 1001

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val projectsViewModel: ProjectsViewModel by viewModels()
    private val tasksViewModel: TasksViewModel by viewModels()
    private val projects: ArrayList<Project> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)
        binding.navigationView.setNavigationItemSelectedListener { i -> onClickMenuItem(i) }
        projectsViewModel.getProjects()
        observeProjects()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            findNavController(R.id.nav_host_fragment_content_main)
        )
                || super.onOptionsItemSelected(item)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    private fun observeProjects() {
        projectsViewModel.projects.observe(this) {
            projects.clear()
            projects.addAll(it)
            addMenuItemsForProjects()
        }
    }

    private fun onClickMenuItem(menuItem: MenuItem): Boolean {
        binding.navigationView.menu.forEach { i ->
            i.isChecked = false
        }
        when (menuItem.itemId) {
            R.id.AboutFragment -> findNavController(R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_MainFragment_to_AboutFragment) // TODO: Don't navigate in.
            MENU_ID_LOGOUT -> {
                // TODO: Log out
            }
            else -> {
                val project = projectsViewModel.getProjectByMenuId(menuItem.itemId)
                if (project != null) {
                    projectsViewModel.selectedProjectUUID = project.uuid
                    tasksViewModel.getTasks(project.uuid)
                }
            }
        }
        menuItem.isChecked = true
        binding.drawerLayout.close()
        return true
    }

    private fun addMenuItemsForProjects() {
        val menu = binding.navigationView.menu
        projects.forEach {
            menu.add(R.id.Projects, it.menuId, Menu.NONE, "Project ${it.name}")
        }
        menu.add(R.id.Actions, R.id.AboutFragment, Menu.NONE, "About")
            .setIcon(R.drawable.ic_baseline_info_24)
        menu.add(R.id.Actions, MENU_ID_LOGOUT, Menu.NONE, "Logout")
            .setIcon(R.drawable.ic_baseline_logout_24)
        menu.getItem(0).isChecked = true
        binding.navigationView.invalidate()
    }
}
package ch.wenksi.pushalerts

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ch.wenksi.pushalerts.databinding.ActivityMainBinding
import ch.wenksi.pushalerts.models.Project
import ch.wenksi.pushalerts.viewModels.ProjectsViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel: ProjectsViewModel by viewModels()
    private val projects: ArrayList<Project> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)

        setupNavigationDrawer()

        viewModel.getProjects(false)
        observeProjects()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun observeProjects() {
        viewModel.projects.observe(this) {
            projects.clear()
            projects.addAll(it)
            // TODO: Potentially order projects
            viewModel.selectedProjectUUID = it.first().uuid
            addMenuItemsForProjects()
        }
    }

    private fun setupNavigationDrawer() {
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            binding.navigationView.menu.forEach { i ->
                i.isChecked = false
            }
            when(menuItem.title == "About") {

            }
            menuItem.isChecked = true
            binding.drawerLayout.close()
            true
        }
    }

    private fun addMenuItemsForProjects() {
        val menu = binding.navigationView.menu
        projects.forEach {
            menu.add(R.id.Projects, Menu.NONE, Menu.NONE, "Project ${it.name}")
        }
        menu.add(R.id.Actions, Menu.NONE, Menu.NONE, "About")
            .setIcon(R.drawable.ic_baseline_info_24)
        menu.add(R.id.Actions, Menu.NONE, Menu.NONE, "Logout")
            .setIcon(R.drawable.ic_baseline_logout_24)
        menu.getItem(0).isChecked = true
        binding.navigationView.invalidate()
    }
}
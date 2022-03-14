package ch.wenksi.pushalerts

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
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
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

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

        // TODO: Fix dissapearing hamburger menu
        binding.topAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
        binding.topAppBar.setNavigationIconTint(resources.getColor(R.color.primary))
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            binding.drawerLayout.close()
            true
        }
    }

    private fun addMenuItemsForProjects() {
        val menu = binding.navigationView.menu
        projects.forEach {
            menu.add(R.id.Projects, Menu.NONE, Menu.NONE, it.name)
        }
        binding.navigationView.invalidate()
    }
}
package ch.wenksi.pushalerts.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import ch.wenksi.pushalerts.R
import ch.wenksi.pushalerts.databinding.FragmentMainBinding
import ch.wenksi.pushalerts.ui.tasks.TabLayoutFragmentAdapter
import ch.wenksi.pushalerts.viewModels.ProjectsViewModel
import ch.wenksi.pushalerts.viewModels.TasksViewModel
import com.google.android.material.tabs.TabLayout
import java.util.*

/**
 * Shows the home screen. This class initializes the tab view.
 * Listens to projects live data and gets tasks when it is triggered
 * Listens to taskUpdate live data and gets tasks when it is triggered
 */
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val tasksViewModel: TasksViewModel by activityViewModels()
    private val projectsViewModel: ProjectsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataChangeListeners()
        initTabLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initTabLayout() {
        val fm = requireActivity().supportFragmentManager
        binding.viewPager.adapter = TabLayoutFragmentAdapter(fm, lifecycle)
        binding.tabLayout.removeAllTabs()
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.tiTasksOpen))
                .setIcon(R.drawable.ic_outline_playlist_add_24)
        )
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.tiTasksClosed))
                .setIcon(R.drawable.ic_outline_history_24)
        )

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun initDataChangeListeners() {
        projectsViewModel.projects.observe(viewLifecycleOwner) {
            if (projectsViewModel.selectedProjectUUID == null)
                projectsViewModel.selectedProjectUUID = it.first().uuid
            tasksViewModel.getTasks(projectsViewModel.selectedProjectUUID!!)
        }
        tasksViewModel.taskUpdate.observe(viewLifecycleOwner) {
            tasksViewModel.getTasks(projectsViewModel.selectedProjectUUID!!)
        }
    }
}
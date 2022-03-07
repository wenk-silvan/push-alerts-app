package ch.wenksi.pushalerts.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.wenksi.pushalerts.R
import ch.wenksi.pushalerts.databinding.FragmentTabClosedBinding
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.TaskState
import ch.wenksi.pushalerts.viewModels.AuthenticationViewModel
import ch.wenksi.pushalerts.viewModels.ProjectsViewModel

class TabClosedFragment : Fragment() {
    private var _binding: FragmentTabClosedBinding? = null
    private val binding get() = _binding!!
    private val projectsViewModel: ProjectsViewModel by activityViewModels()
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
    private val tasks: ArrayList<Task> = arrayListOf()
    private lateinit var recyclerViewAdapter: ClosedTasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabClosedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasks.addAll(projectsViewModel.getClosedTasksOfSelectedProject()) // TODO: Only closed tasks
        initChipGroup()
        initRecyclerView()
        initSwipeToRefresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initChipGroup() {
        binding.chipFilterMine.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) refreshTaskList(
                projectsViewModel.getMyClosedTasks(authenticationViewModel.user.uuid)
            )
            else refreshTaskList(projectsViewModel.getClosedTasksOfSelectedProject())
        }
        binding.chipFilterDone.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) refreshTaskList(projectsViewModel.getTasks(TaskState.Done))
            else refreshTaskList(projectsViewModel.getClosedTasksOfSelectedProject())
        }
        binding.chipFilterRejected.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) refreshTaskList(projectsViewModel.getTasks(TaskState.Rejected))
            else refreshTaskList(projectsViewModel.getClosedTasksOfSelectedProject())
        }
    }

    private fun initRecyclerView() {
        recyclerViewAdapter = ClosedTasksAdapter(
            tasks
        ) { t: Task -> onClickCard(t) }

        binding.rvTasks.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvTasks.adapter = recyclerViewAdapter
        binding.rvTasks.isNestedScrollingEnabled = false
        binding.rvTasks.addItemDecoration(
            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        )
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            projectsViewModel.getProjects(true)
            binding.chipFilterMine.isChecked = false
            binding.chipFilterDone.isChecked = false
            binding.chipFilterRejected.isChecked = false
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun onClickCard(task: Task) {
        findNavController().navigate(
            R.id.action_TasksFragment_to_TaskDetailsFragment,
            bundleOf(BUNDLE_TASK_ID to task.uuid.toString())
        )
    }

    private fun refreshTaskList(tasks: List<Task>) {
        this.tasks.clear()
        this.tasks.addAll(tasks)
        recyclerViewAdapter.notifyDataSetChanged()
    }
}

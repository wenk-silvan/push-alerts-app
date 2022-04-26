package ch.wenksi.pushalerts.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.wenksi.pushalerts.R
import ch.wenksi.pushalerts.databinding.FragmentTabOpenBinding
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.TaskState
import ch.wenksi.pushalerts.services.login.SessionManager
import ch.wenksi.pushalerts.viewModels.ProjectsViewModel
import ch.wenksi.pushalerts.viewModels.TasksViewModel
import ch.wenksi.pushalerts.viewModels.UserViewModel
import kotlin.collections.ArrayList

class TabOpenFragment : Fragment() {
    private var _binding: FragmentTabOpenBinding? = null
    private val binding get() = _binding!!
    private val tasksViewModel: TasksViewModel by activityViewModels()
    private val projectsViewModel: ProjectsViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private val tasks: ArrayList<Task> = arrayListOf()
    private lateinit var recyclerViewAdapter: OpenTasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabOpenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChipGroup()
        initRecyclerView()
        initSwipeToRefresh()
        initTaskChangeListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initChipGroup() {
        val listener = CompoundButton.OnCheckedChangeListener { _, _ -> filterTasks() }
        binding.chipFilterMine.setOnCheckedChangeListener(listener)
        binding.chipFilterUnassigned.setOnCheckedChangeListener(listener)
    }

    private fun initRecyclerView() {
        recyclerViewAdapter = OpenTasksAdapter(
            tasks,
            SessionManager.requireToken().email,
            { t: Task ->
                tasksViewModel.assignTask(
                    t,
                    SessionManager.requireToken().uuid,
                    SessionManager.requireToken().email
                )
            },
            { t: Task -> tasksViewModel.finishTask(t) },
            { t: Task -> tasksViewModel.rejectTask(t) },
            { t: Task -> onClickCard(t) }
        )
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
            tasksViewModel.getTasks(projectsViewModel.selectedProjectUUID!!)
            binding.chipFilterMine.isChecked = false
            binding.chipFilterUnassigned.isChecked = false
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun onClickCard(task: Task) {
        findNavController().navigate(
            R.id.action_MainFragment_to_TaskDetailsFragment,
            bundleOf(BUNDLE_TASK_ID to task.uuid.toString())
        )
    }

    private fun filterTasks() {
        if (tasksViewModel.tasks.value == null) return
        var tasks = tasksViewModel.getOpenTasks(tasksViewModel.tasks.value!!)

        if (binding.chipFilterMine.isChecked) {
            tasks = tasksViewModel.getTasksOfUser(
                SessionManager.requireToken().email, tasks
            )
        }
        if (binding.chipFilterUnassigned.isChecked) {
            tasks = tasksViewModel.getTasks(TaskState.Opened, tasks)
        }
        refreshTaskList(tasks)
    }

    private fun refreshTaskList(tasks: List<Task>?) {
        if (tasks != null) {
            this.tasks.clear()
            this.tasks.addAll(tasks)
            recyclerViewAdapter.notifyDataSetChanged()
        }
    }

    private fun initTaskChangeListeners() {
        tasksViewModel.tasks.observe(viewLifecycleOwner) {
            refreshTaskList(tasksViewModel.getOpenTasks(it))
        }
    }
}

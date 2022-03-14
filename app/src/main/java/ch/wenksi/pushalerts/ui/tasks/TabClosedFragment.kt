package ch.wenksi.pushalerts.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.wenksi.pushalerts.databinding.FragmentTabClosedBinding
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.TaskState
import ch.wenksi.pushalerts.viewModels.AuthenticationViewModel
import ch.wenksi.pushalerts.viewModels.TasksViewModel

class TabClosedFragment : Fragment() {
    private var _binding: FragmentTabClosedBinding? = null
    private val binding get() = _binding!!
    private val tasksViewModel: TasksViewModel by activityViewModels()
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
        initChipGroup()
        initRecyclerView()
        initSwipeToRefresh()
        observeTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initChipGroup() {
        binding.chipFilterMine.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) refreshTaskList(
                tasksViewModel.getMyClosedTasks(authenticationViewModel.user.uuid)
            )
            else refreshTaskList(tasksViewModel.getClosedTasks())
        }
        binding.chipFilterDone.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) refreshTaskList(tasksViewModel.getTasks(TaskState.Done))
            else refreshTaskList(tasksViewModel.getClosedTasks())
        }
        binding.chipFilterRejected.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) refreshTaskList(tasksViewModel.getTasks(TaskState.Rejected))
            else refreshTaskList(tasksViewModel.getClosedTasks())
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
            tasksViewModel.getTasks(true)
            binding.chipFilterMine.isChecked = false
            binding.chipFilterDone.isChecked = false
            binding.chipFilterRejected.isChecked = false
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun onClickCard(task: Task) {
//        findNavController().navigate(
//            R.id.action_TasksFragment_to_TaskDetailsFragment,
//            bundleOf(BUNDLE_TASK_ID to task.uuid.toString())
//        )
    }

    private fun refreshTaskList(tasks: List<Task>?) {
        if (tasks != null) {
            this.tasks.clear()
            this.tasks.addAll(tasks)
            recyclerViewAdapter.notifyDataSetChanged()
        }
    }

    private fun observeTasks() {
        tasksViewModel.tasks.observe(viewLifecycleOwner) {
            refreshTaskList(tasksViewModel.getClosedTasks())
        }
    }
}

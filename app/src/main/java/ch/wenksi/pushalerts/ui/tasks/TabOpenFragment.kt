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
import ch.wenksi.pushalerts.databinding.FragmentTabOpenBinding
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.viewModels.AuthenticationViewModel
import ch.wenksi.pushalerts.viewModels.ProjectsViewModel

class TabOpenFragment : Fragment() {
    private var _binding: FragmentTabOpenBinding? = null
    private val binding get() = _binding!!
    private val projectsViewModel: ProjectsViewModel by activityViewModels()
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
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
        tasks.addAll(projectsViewModel.getOpenTasksOfSelectedProject()) // TODO: Only open tasks
        initChipGroup()
        initRecyclerView()
        initSwipeToRefresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initChipGroup() {
        // TODO: Setup filters
    }

    private fun initRecyclerView() {
        recyclerViewAdapter = OpenTasksAdapter(
            tasks,
            authenticationViewModel.user,
            { t: Task -> onClickBtnAssign(t) },
            { t: Task -> onClickBtnClose(t) },
            { t: Task -> onClickBtnReject(t) },
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
            projectsViewModel.getProjects(true)
            binding.chipFilterMine.isChecked = false
            binding.chipFilterUnassigned.isChecked = false
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun onClickBtnAssign(task: Task) {
        // TODO: Update in DB
        task.assign(authenticationViewModel.user)
        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun onClickBtnClose(task: Task) {
        task.finish()
        tasks.remove(task)
        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun onClickBtnReject(task: Task) {
        task.reject()
        tasks.remove(task)
        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun onClickCard(task: Task) {
        // TODO: Navigate to detailed task fragment
    }
}

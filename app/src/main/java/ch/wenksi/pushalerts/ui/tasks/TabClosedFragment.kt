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
import ch.wenksi.pushalerts.viewModels.ProjectsViewModel

class TabClosedFragment : Fragment() {
    private var _binding: FragmentTabClosedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProjectsViewModel by activityViewModels()
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
        tasks.addAll(viewModel.getTaskOfSelectedProject()) // TODO: Only closed tasks
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
        recyclerViewAdapter = ClosedTasksAdapter(tasks)
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
            viewModel.getProjects(true)
            binding.chipFilterMine.isChecked = false
            binding.chipFilterOld.isChecked = false
            binding.swipeRefresh.isRefreshing = false
        }
    }
}

//    private fun observeTasks() {
//        viewModel.projects.observe(viewLifecycleOwner) {
//            tasks.clear()
//            tasks.addAll(it) // TODO: Only closed tasks
//            recyclerViewAdapter.notifyDataSetChanged()
//        }
//    }

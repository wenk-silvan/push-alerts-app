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
import ch.wenksi.pushalerts.viewModels.ProjectsViewModel

class TabOpenFragment : Fragment() {
    private var _binding: FragmentTabOpenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProjectsViewModel by activityViewModels()
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
        tasks.addAll(viewModel.getTaskOfSelectedProject()) // TODO: Only open tasks
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
        recyclerViewAdapter = OpenTasksAdapter(tasks)
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
            binding.chipFilterUnassigned.isChecked = false
            binding.swipeRefresh.isRefreshing = false
        }
    }

//    private fun observeTasks() {
//        viewModel.projects.observe(viewLifecycleOwner) {
//            tasks.clear()
//            tasks.addAll(it)
//            recyclerViewAdapter.notifyDataSetChanged()
//        }
//    }
}

package ch.wenksi.pushalerts.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ch.wenksi.pushalerts.databinding.FragmentTasksBinding
import ch.wenksi.pushalerts.models.Project
import ch.wenksi.pushalerts.viewModels.ProjectsViewModel

class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProjectsViewModel by activityViewModels()
    private val projects: ArrayList<Project> = arrayListOf()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProjects(false)
        observeTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeTasks() {
        viewModel.projects.observe(viewLifecycleOwner) {
            projects.clear()
            projects.addAll(it)
        }
    }

}
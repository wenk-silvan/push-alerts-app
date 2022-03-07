package ch.wenksi.pushalerts.ui.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ch.wenksi.pushalerts.databinding.FragmentTaskDetailsBinding
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.TaskState
import ch.wenksi.pushalerts.viewModels.AuthenticationViewModel
import ch.wenksi.pushalerts.viewModels.ProjectsViewModel

const val BUNDLE_TASK_ID = "bundle_task_uuid"

class TaskDetailsFragment : Fragment() {
    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!
    private val projectsViewModel: ProjectsViewModel by activityViewModels()
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uuid = arguments?.getString(BUNDLE_TASK_ID)
        task = projectsViewModel.getTask(uuid)
        setupTextViews()
        setElementVisibilities()
        setupClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClickBtnAssign() {
        // TODO: Update in DB
        task.assign(authenticationViewModel.user)
        setElementVisibilities()
    }

    private fun onClickBtnFinish() {
        task.finish()
        setElementVisibilities()
    }

    private fun onClickBtnReject() {
        task.reject()
        setElementVisibilities()
    }

    private fun setupClickListeners() {
        binding.btnAssignTask.setOnClickListener { onClickBtnAssign() }
        binding.btnFinishTask.setOnClickListener { onClickBtnFinish() }
        binding.btnRejectTask.setOnClickListener { onClickBtnReject() }
    }

    private fun setElementVisibilities() {
        when(task.state) {
            TaskState.Opened -> {
                binding.tvTaskUser.visibility = View.GONE
                binding.btnAssignTask.visibility = View.VISIBLE
                binding.btnFinishTask.visibility = View.GONE
                binding.btnRejectTask.visibility = View.GONE
            }
            TaskState.Assigned -> {
                binding.tvTaskUser.visibility = View.VISIBLE
                binding.btnAssignTask.visibility = View.GONE

                if (authenticationViewModel.isAssignedToMe(task)) {
                    binding.btnFinishTask.visibility = View.VISIBLE
                    binding.btnRejectTask.visibility = View.VISIBLE
                } else {
                    binding.btnFinishTask.visibility = View.GONE
                    binding.btnRejectTask.visibility = View.GONE
                }
            }
            TaskState.Done, TaskState.Rejected -> {
                binding.tvTaskUser.visibility = View.VISIBLE
                binding.btnAssignTask.visibility = View.GONE
                binding.btnFinishTask.visibility = View.GONE
                binding.btnRejectTask.visibility = View.GONE
            }
        }
    }

    private fun setupTextViews() {
        binding.tvTaskAssigned.text = if (task.assignedAt == null) "-" else task.assignedAt.toString()
        binding.tvTaskClosed.text = if (task.closedAt == null) "-" else task.closedAt.toString()
        binding.tvTaskCreated.text = task.createdAt.toString()
        binding.tvTaskDescription.text = task.description
        binding.tvTaskName.text = task.title
        binding.tvTaskPayload.text = task.payload
        binding.tvTaskSource.text = task.source
        binding.tvTaskUser.text = task.user?.email
    }
}
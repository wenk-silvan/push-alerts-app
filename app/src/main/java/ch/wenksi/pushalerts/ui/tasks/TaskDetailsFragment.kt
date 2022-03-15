package ch.wenksi.pushalerts.ui.tasks

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ch.wenksi.pushalerts.R
import ch.wenksi.pushalerts.databinding.FragmentTaskDetailsBinding
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.TaskState
import ch.wenksi.pushalerts.viewModels.AuthenticationViewModel
import ch.wenksi.pushalerts.viewModels.TasksViewModel

const val BUNDLE_TASK_ID = "bundle_task_uuid"

class TaskDetailsFragment : Fragment() {
    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!
    private val tasksViewModel: TasksViewModel by activityViewModels()
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
        task = tasksViewModel.getTask(uuid)
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
        when (task.state) {
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
        binding.tvTaskAssigned.text =
            if (task.assignedAt == null) "-" else task.assignedAtFormatted()
        binding.tvTaskClosed.text = if (task.closedAt == null) "-" else task.closedAtFormatted()
        binding.tvTaskCreated.text = task.createdAtFormatted()
        binding.tvTaskDescription.text = task.description
        binding.tvTaskName.text = task.title
        binding.tvTaskPayload.text = task.payload
        binding.tvTaskSource.text = task.source
        binding.tvTaskUser.text = task.user?.email
        binding.tvTaskUser.paintFlags = binding.tvTaskUser.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        when (task.state) {
            TaskState.Opened -> {
                binding.ivTaskClosedIcon.setImageResource(R.drawable.ic_outline_check_24)
                binding.tvTaskStatus.text = getString(R.string.tvTaskStatus_Created)
                binding.ivTaskStatusIcon.setImageResource(R.drawable.ic_outline_add_24)
            }
            TaskState.Assigned -> {
                binding.ivTaskClosedIcon.setImageResource(R.drawable.ic_outline_check_24)
                binding.tvTaskStatus.text = getString(R.string.tvTaskStatus_Assigned)
                binding.ivTaskStatusIcon.setImageResource(R.drawable.ic_outline_person_24)
            }
            TaskState.Rejected -> {
                binding.ivTaskClosedIcon.setImageResource(R.drawable.ic_baseline_remove_24)
                binding.tvTaskStatus.text = getString(R.string.tvTaskStatus_Rejected)
                binding.ivTaskStatusIcon.setImageResource(R.drawable.ic_baseline_remove_24)

            }
            TaskState.Done -> {
                binding.ivTaskClosedIcon.setImageResource(R.drawable.ic_outline_check_24)
                binding.tvTaskStatus.text = getString(R.string.tvTaskStatus_Done)
                binding.ivTaskStatusIcon.setImageResource(R.drawable.ic_outline_check_24)
            }
        }
    }
}
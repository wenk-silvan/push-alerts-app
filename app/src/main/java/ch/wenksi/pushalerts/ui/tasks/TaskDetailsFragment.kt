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
import ch.wenksi.pushalerts.services.auth.SessionManager
import ch.wenksi.pushalerts.viewModels.ProjectsViewModel
import ch.wenksi.pushalerts.viewModels.TasksViewModel
import ch.wenksi.pushalerts.viewModels.UserViewModel
import com.google.android.material.snackbar.Snackbar

const val BUNDLE_TASK_ID = "bundle_task_uuid"

/**
 * This class binds the data to the task details screen and changes element visibilities based on the data.
 */
class TaskDetailsFragment : Fragment() {
    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!
    private val tasksViewModel: TasksViewModel by activityViewModels()
    private val projectsViewModel: ProjectsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments == null) {
            Snackbar.make(
                view,
                getString(R.string.errorTaskDetailsMissingArguments),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }
        initTaskData()
        initDataChangeListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initDataChangeListeners() {
        tasksViewModel.taskUpdate.observe(viewLifecycleOwner) {
            tasksViewModel.getTasks(projectsViewModel.selectedProjectUUID!!)
        }
        tasksViewModel.tasks.observe(viewLifecycleOwner) {
            initTaskData()
        }
    }

    private fun initTaskData() {
        val task = getTask(requireArguments())
        setupTextViews(task)
        setElementVisibilities(task)
        setupClickListeners(task)
    }

    private fun getTask(arguments: Bundle): Task {
        val uuid = arguments.getString(BUNDLE_TASK_ID)
        return tasksViewModel.getTask(uuid)
    }

    private fun setupClickListeners(task: Task) {
        binding.btnAssignTask.setOnClickListener {
            tasksViewModel.assignTask(
                task,
                SessionManager.requireToken().uuid,
                SessionManager.requireToken().email
            )
        }
        binding.btnFinishTask.setOnClickListener { tasksViewModel.finishTask(task) }
        binding.btnRejectTask.setOnClickListener { tasksViewModel.rejectTask(task) }
    }

    private fun setElementVisibilities(task: Task) {
        when (task.status) {
            TaskState.Opened -> setupElementVisibilitiesTaskStateOpened()
            TaskState.Assigned -> setupElementVisibilitiesTaskStateAssigned(
                SessionManager.requireToken().email == task.userEmail
            )
            TaskState.Finished, TaskState.Rejected -> setupElementVisibilitiesTaskStateDoneAndRejected()
        }
    }

    private fun setupElementVisibilitiesTaskStateOpened() {
        binding.tvTaskUser.visibility = View.GONE
        binding.btnAssignTask.visibility = View.VISIBLE
        binding.btnFinishTask.visibility = View.GONE
        binding.btnRejectTask.visibility = View.GONE
    }

    private fun setupElementVisibilitiesTaskStateAssigned(assignedToMe: Boolean) {
        binding.tvTaskUser.visibility = View.VISIBLE
        binding.btnAssignTask.visibility = View.GONE

        if (assignedToMe) {
            binding.btnFinishTask.visibility = View.VISIBLE
            binding.btnRejectTask.visibility = View.VISIBLE
        } else {
            binding.btnFinishTask.visibility = View.GONE
            binding.btnRejectTask.visibility = View.GONE
        }
    }

    private fun setupElementVisibilitiesTaskStateDoneAndRejected() {
        binding.tvTaskUser.visibility = View.VISIBLE
        binding.btnAssignTask.visibility = View.GONE
        binding.btnFinishTask.visibility = View.GONE
        binding.btnRejectTask.visibility = View.GONE
    }

    private fun setupTextViews(task: Task) {
        binding.tvTaskAssigned.text =
            if (task.assignedAt == null) "-" else task.assignedAtFormatted()
        binding.tvTaskClosed.text = if (task.closedAt == null) "-" else task.closedAtFormatted()
        binding.tvTaskCreated.text = task.createdAtFormatted()
        binding.tvTaskDescription.text = task.description
        binding.tvTaskName.text = task.title
        binding.tvTaskPayload.text =
            if (task.payload == "") "No additional information" else task.payload
        binding.tvTaskSource.text = task.source
        binding.tvTaskUser.text = task.userEmail
        binding.tvTaskUser.paintFlags = binding.tvTaskUser.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        when (task.status) {
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
            TaskState.Finished -> {
                binding.ivTaskClosedIcon.setImageResource(R.drawable.ic_outline_check_24)
                binding.tvTaskStatus.text = getString(R.string.tvTaskStatus_Done)
                binding.ivTaskStatusIcon.setImageResource(R.drawable.ic_outline_check_24)
            }
        }
    }
}
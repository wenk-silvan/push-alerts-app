package ch.wenksi.pushalerts.ui.tasks

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.wenksi.pushalerts.R
import ch.wenksi.pushalerts.databinding.ItemTaskOpenBinding
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.TaskState
import ch.wenksi.pushalerts.models.User

class OpenTasksAdapter(
    private val tasks: List<Task>,
    private val authenticatedUser: User,
    val onClickBtnAssign: (Task) -> Unit,
    val onClickBtnClose: (Task) -> Unit,
    val onClickBtnReject: (Task) -> Unit,
    val onClickCard: (Task) -> Unit,

    ) :
    RecyclerView.Adapter<OpenTasksAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemTaskOpenBinding.bind(itemView)

        fun databind(task: Task) {
            setupTextViews(binding, task)
            setupOnClickListeners(binding, task)

            if (task.state == TaskState.Opened) {
                binding.btnAssign.visibility = View.VISIBLE
                binding.tvAssigned.visibility = View.GONE
                binding.btnReject.visibility = View.GONE
                binding.btnClose.visibility = View.GONE
                binding.mcvTaskOpen.setCardBackgroundColor(Color.parseColor("#f3f6f4"))
            } else if (task.state == TaskState.Assigned) {
                binding.btnAssign.visibility = View.GONE
                binding.mcvTaskOpen.setCardBackgroundColor(Color.parseColor("#ffffff"))
                if (assignedToMe(task)) {
                    binding.btnReject.visibility = View.VISIBLE
                    binding.btnClose.visibility = View.VISIBLE
                } else {
                    binding.tvAssigned.visibility = View.VISIBLE
                    binding.tvAssigned.text = task.user?.email
                    binding.tvAssigned.paintFlags =
                        binding.tvAssigned.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task_open, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(tasks[position])
    }

    private fun shortDescription(description: String): String {
        return if (description.length > 25) description.substring(0, 25) + "..." else description
    }

    private fun setupOnClickListeners(binding: ItemTaskOpenBinding, task: Task) {
        binding.btnAssign.setOnClickListener { onClickBtnAssign(task) }
        binding.btnClose.setOnClickListener { onClickBtnClose(task) }
        binding.btnReject.setOnClickListener { onClickBtnReject(task) }
        binding.mcvTaskOpen.setOnClickListener { onClickCard(task) }
    }

    private fun setupTextViews(binding: ItemTaskOpenBinding, task: Task) {
        binding.tvTaskName.text = task.title
        binding.tvTaskCreatedAt.text = task.createdAt.toString()
        binding.tvTaskDescription.text = shortDescription(task.description)
        binding.tvTaskSource.text = task.source
    }

    private fun assignedToMe(task: Task): Boolean {
        return task.user?.uuid == authenticatedUser.uuid
    }
}

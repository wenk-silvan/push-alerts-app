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

class OpenTasksAdapter(
    private val tasks: List<Task>,
    private val email: String,
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
            setupVisibilities(binding, task)
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
        binding.tvTaskCreatedAt.text = task.createdAtFormatted()
        binding.tvTaskDescription.text = shortDescription(task.description)
        binding.tvTaskSource.text = task.source
    }

    private fun setupVisibilities(binding: ItemTaskOpenBinding, task: Task) {
        if (task.status == TaskState.Opened) {
            binding.btnAssign.visibility = View.VISIBLE
            binding.tvAssigned.visibility = View.GONE
            binding.btnReject.visibility = View.GONE
            binding.btnClose.visibility = View.GONE
            binding.mcvTaskOpen.setCardBackgroundColor(Color.parseColor("#f3f6f4"))
        } else if (task.status == TaskState.Assigned) {
            binding.btnAssign.visibility = View.GONE
            binding.mcvTaskOpen.setCardBackgroundColor(Color.parseColor("#ffffff"))
            if (assignedToMe(task)) {
                binding.btnReject.visibility = View.VISIBLE
                binding.btnClose.visibility = View.VISIBLE
            } else {
                binding.btnReject.visibility = View.GONE
                binding.btnClose.visibility = View.GONE
                binding.tvAssigned.visibility = View.VISIBLE
                binding.tvAssigned.text = task.userEmail
                binding.tvAssigned.paintFlags =
                    binding.tvAssigned.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }
        }
    }

    private fun assignedToMe(task: Task): Boolean {
        return task.userEmail == email
    }
}

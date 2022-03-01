package ch.wenksi.pushalerts.ui.tasks


import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.wenksi.pushalerts.R
import ch.wenksi.pushalerts.databinding.ItemTaskClosedBinding
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.models.TaskState
import java.lang.Exception

class ClosedTasksAdapter(
    private val tasks: List<Task>,
    val onClickCard: (Task) -> Unit,
) :
    RecyclerView.Adapter<ClosedTasksAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemTaskClosedBinding.bind(itemView)

        fun databind(task: Task) {
            if (task.closedAt == null) {
                throw Exception("Closed task must have a closedAt timestamp.")
            } else if (task.user == null) {
                throw Exception("Closed task must have an assigned user")
            }
            binding.tvTaskNumber.text = "#${task._id}"
            binding.tvTaskName.text = task.title
            binding.tvTaskCreatedAt.text = task.createdAt.toString()
            binding.tvAssigned.text = task.user!!.email
            binding.tvAssigned.paintFlags =
                binding.tvAssigned.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            binding.tvTaskDescription.text = shortDescription(task.description)
            binding.tvTaskSource.text = task.source
            binding.ivStateIcon.setImageResource(
                if (task.state == TaskState.Rejected) R.drawable.ic_outline_remove_circle_outline_24
                else R.drawable.ic_outline_check_circle_24)

            binding.mcvTaskClosed.setOnClickListener { onClickCard(task) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task_closed, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(tasks[position])
    }

    private fun shortDescription(description: String): String {
        return if (description.length > 20) description.substring(0, 20) + "..." else description
    }
}

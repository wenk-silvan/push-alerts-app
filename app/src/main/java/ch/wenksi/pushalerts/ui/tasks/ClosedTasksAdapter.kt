package ch.wenksi.pushalerts.ui.tasks


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.wenksi.pushalerts.R
import ch.wenksi.pushalerts.databinding.ItemTaskClosedBinding
import ch.wenksi.pushalerts.models.Task
import java.lang.Exception

class ClosedTasksAdapter(
    private val tasks: List<Task>,
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
            binding.tvTaskName.text = task.title
            binding.tvTaskCreatedAt.text = task.createdAt.toString()
            binding.tvTaskClosedAt.text = task.closedAt.toString()
            binding.tvAssigned.text = task.user!!.email
            binding.tvTaskDescription.text = shortDescription(task.description)
            binding.tvTaskSource.text = task.source
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

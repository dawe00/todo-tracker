package hu.bme.aut.android.todo.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.todo.databinding.RowTodoBinding
import hu.bme.aut.android.todo.model.Todo

class SimpleItemRecyclerViewAdapter : ListAdapter<Todo, SimpleItemRecyclerViewAdapter.ViewHolder>(ItemCallback) {

    companion object{
        object ItemCallback : DiffUtil.ItemCallback<Todo>(){
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem == newItem
            }
        }
    }

    var itemClickListener: TodoItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RowTodoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = this.getItem(position)

        holder.todo = todo
        holder.binding.tvTitle.text = todo.title
        holder.binding.tvDueDate.text = todo.dueDate
        when (todo.priority) {
            Todo.Priority.LOW -> {
                holder.binding.tvPriority.text = "Low"
                holder.binding.tvPriority.setTextColor(Color.GREEN)

            }
            Todo.Priority.MEDIUM -> {
                holder.binding.tvPriority.text = "Medium"
                holder.binding.tvPriority.setTextColor(Color.YELLOW)
            }
            else -> {
                holder.binding.tvPriority.text = "High"
                holder.binding.tvPriority.setTextColor(Color.RED)
            }
        }
    }

    inner class ViewHolder(val binding: RowTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        var todo: Todo? = null

        init {
            itemView.setOnClickListener {
                todo?.let { todo -> itemClickListener?.onItemClick(todo) }
            }

            itemView.setOnLongClickListener { view ->
                todo?.let { todo -> itemClickListener?.onItemLongClick(adapterPosition, view, todo)}
                true
            }
        }
    }

    interface TodoItemClickListener {
        fun onItemClick(todo: Todo)
        fun onItemLongClick(position: Int, view: View, todo: Todo): Boolean
    }
}
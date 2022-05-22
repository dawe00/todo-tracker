package hu.bme.aut.android.todo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.todo.databinding.TodoDetailBinding
import hu.bme.aut.android.todo.model.Todo

class TodoDetailFragment : DialogFragment() {

    private var selectedTodo: Todo? = null
    private lateinit var binding: TodoDetailBinding

    companion object {

        private const val KEY_TODO_DESCRIPTION = "KEY_TODO_DESCRIPTION"
        private const val KEY_TODO_TITLE = "KEY_TODO_TITLE"

        fun newInstance(todoDesc: String, todoTitle: String): TodoDetailFragment {
            val args = Bundle()
            args.putString(KEY_TODO_DESCRIPTION, todoDesc)
            args.putString(KEY_TODO_TITLE, todoTitle)

            val result = TodoDetailFragment()
            result.arguments = args
            return result
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { args ->
            selectedTodo = Todo(
                id = 0,
                title = args.getString(KEY_TODO_TITLE) ?: "",
                priority = Todo.Priority.LOW,
                dueDate = "",
                description = args.getString(KEY_TODO_DESCRIPTION) ?: ""
            )
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TodoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.todoDetail.text = selectedTodo?.description
    }
}
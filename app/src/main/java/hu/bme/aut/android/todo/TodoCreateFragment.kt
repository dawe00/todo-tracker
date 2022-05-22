package hu.bme.aut.android.todo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import hu.bme.aut.android.todo.databinding.FragmentCreateBinding
import hu.bme.aut.android.todo.model.Todo
import kotlin.random.Random
import androidx.fragment.app.FragmentActivity
import java.util.*

class TodoCreateFragment : DialogFragment(), TodoDatePickerFragment.OnDateSelectedListener {
    companion object {
        private const val KEY_TODO_ID = "KEY_TODO_ID"
        private const val KEY_TODO_TITLE = "KEY_TODO_TITLE"
        private const val KEY_TODO_PRIORITY = "KEY_TODO_PRIORITY"
        private const val KEY_TODO_DUEDATE = "KEY_TODO_DUEDATE"
        private const val KEY_TODO_DESCRIPTION = "KEY_TODO_DESCRIPTION"

        fun newInstance(todo: Todo): TodoCreateFragment {
            val args = Bundle()
            args.putInt(KEY_TODO_ID, todo.id)
            args.putString(KEY_TODO_TITLE, todo.title)
            args.putString(KEY_TODO_PRIORITY, todo.priority.toString())
            args.putString(KEY_TODO_DUEDATE, todo.dueDate)
            args.putString(KEY_TODO_DESCRIPTION, todo.description)

            val result = TodoCreateFragment()
            result.arguments = args
            return result
        }

    }

    private var selectedTodo: Todo? = null
    private lateinit var listener: TodoCreatedListener
    private lateinit var binding: FragmentCreateBinding
    private var fcontext: FragmentActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fcontext = activity as FragmentActivity

        try {
            listener = if (targetFragment != null) {
                targetFragment as TodoCreatedListener
            } else {
                activity as TodoCreatedListener
            }
        } catch (e: ClassCastException) {
            throw RuntimeException(e)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        arguments?.let { args ->
            selectedTodo = Todo(
                id = args.getInt(KEY_TODO_ID),
                title = args.getString(KEY_TODO_TITLE) ?: "",
                priority = ((when {
                    args.getString(KEY_TODO_PRIORITY).equals("LOW") -> {
                        Todo.Priority.LOW
                    }
                    args.getString(KEY_TODO_PRIORITY).equals("MEDIUM") -> {
                        Todo.Priority.MEDIUM
                    }
                    else -> {
                        Todo.Priority.HIGH
                    }
                })),
                dueDate = args.getString(KEY_TODO_DUEDATE) ?: "",
                description = args.getString(KEY_TODO_DESCRIPTION) ?: ""
            )
        }

        binding = FragmentCreateBinding.inflate(inflater, container, false)
        dialog?.setTitle(selectedTodo?.title)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var spinnerStartIdx = 0
        if(selectedTodo?.title != null){
            binding.etTodoTitle.setText(selectedTodo!!.title)
            binding.etTodoDescription.setText(selectedTodo!!.description)
            binding.tvTodoDueDate.text = selectedTodo!!.dueDate
            spinnerStartIdx = when(selectedTodo!!.priority){
                Todo.Priority.LOW -> { 0 }
                Todo.Priority.MEDIUM -> { 1 }
                Todo.Priority.HIGH -> { 2 }
            }
        } else {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = (c.get(Calendar.MONTH)+1)
            val day = c.get(Calendar.DAY_OF_MONTH)

            var monthS = "$month"
            var dayS = "$day"
            if(month < 10) {
                monthS = "0$month"
            }
            if(day < 10) {
                dayS = "0$day"
            }

            val currDate = "$year. $monthS. $dayS."

            binding.tvTodoDueDate.text = currDate
        }

        binding.spnrTodoPriority.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("Low", "Medium", "High")
        )
        binding.spnrTodoPriority.setSelection(spinnerStartIdx)

        binding.btnCreateTodo.setOnClickListener {
            val selectedPriority = when (binding.spnrTodoPriority.selectedItemPosition) {
                0 -> Todo.Priority.LOW
                1 -> Todo.Priority.MEDIUM
                2 -> Todo.Priority.HIGH
                else -> Todo.Priority.LOW
            }

            if(selectedTodo?.title == null) {
                listener.onTodoCreated(
                    Todo(
                        id = Random.nextInt(),
                        title = binding.etTodoTitle.text.toString(),
                        priority = selectedPriority,
                        dueDate = binding.tvTodoDueDate.text.toString(),
                        description = binding.etTodoDescription.text.toString()
                    )
                )
            } else {
                listener.onTodoEdited(
                    Todo(
                        id = selectedTodo!!.id,
                        title = binding.etTodoTitle.text.toString(),
                        priority = selectedPriority,
                        dueDate = binding.tvTodoDueDate.text.toString(),
                        description = binding.etTodoDescription.text.toString()
                    )
                )
            }
            dismiss()
            selectedTodo = null
        }

        binding.btnCancelCreateTodo.setOnClickListener {
            selectedTodo = null
            dismiss()
        }

        binding.tvTodoDueDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSelected(year: Int, month: Int, day: Int) {
        var monthS = "$month"
        var dayS = "$day"
        if(month < 10) {
            monthS = "0$month"
        }
        if(day < 10) {
            dayS = "0$day"
        }

        binding.tvTodoDueDate.text = "$year. $monthS. $dayS."
    }

    private fun showDatePickerDialog() {
        val fragManager: FragmentManager? = fcontext?.supportFragmentManager
        val todoDatePickerFragment = TodoDatePickerFragment()
        todoDatePickerFragment.show(fragManager!!, "DATEPICK")
    }

    interface TodoCreatedListener {
        fun onTodoCreated(todo: Todo)
        fun onTodoEdited(todo: Todo)
    }

}
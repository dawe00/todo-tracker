package hu.bme.aut.android.todo

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.todo.adapter.SimpleItemRecyclerViewAdapter
import hu.bme.aut.android.todo.databinding.ActivityTodoListBinding
import hu.bme.aut.android.todo.model.Todo
import hu.bme.aut.android.todo.notification.NotificationHelper
import hu.bme.aut.android.todo.viewmodel.TodoViewModel
import java.util.*

class TodoListActivity : AppCompatActivity(), TodoCreateFragment.TodoCreatedListener, SimpleItemRecyclerViewAdapter.TodoItemClickListener, TodoDatePickerFragment.OnDateSelectedListener {
    private lateinit var simpleItemRecyclerViewAdapter: SimpleItemRecyclerViewAdapter
    private var twoPane: Boolean = false
    private lateinit var binding: ActivityTodoListBinding
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var todoCreateFragment: TodoCreateFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoViewModel.allTodos.observe(this, { todos ->
            simpleItemRecyclerViewAdapter.submitList(todos)
        })

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = title

        if (findViewById<NestedScrollView>(R.id.todo_detail_container) != null) {
            twoPane = true
        }

        NotificationHelper.createNotificationChannels(this)

        setupRecyclerView()
    }

    override fun onDestroy() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)+1
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

        todoViewModel.allTodos.value!!.forEach {
            if(it.dueDate == currDate){
                NotificationHelper.createTodoNotification(this, it)
            }
        }
        super.onDestroy()
    }

    private fun setupRecyclerView() {
        simpleItemRecyclerViewAdapter = SimpleItemRecyclerViewAdapter()
        simpleItemRecyclerViewAdapter.itemClickListener = this
        binding.root.findViewById<RecyclerView>(R.id.todo_list).adapter = simpleItemRecyclerViewAdapter
    }

    override fun onItemClick(todo: Todo) {
        if (twoPane) {
            val fragment = TodoDetailFragment.newInstance(todo.description, todo.title)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.todo_detail_container, fragment)
                .commit()
        } else {
            val intent = Intent(this, TodoDetailActivity::class.java)
            intent.putExtra(TodoDetailActivity.KEY_DESC, todo.description)
            intent.putExtra(TodoDetailActivity.KEY_TITLE, todo.title)
            startActivity(intent)
        }
    }

    override fun onItemLongClick(position: Int, view: View, todo: Todo): Boolean {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_todo)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    todoViewModel.delete(todo)
                    return@setOnMenuItemClickListener true
                }
                R.id.back -> {
                    todoCreateFragment = TodoCreateFragment.newInstance(todo)
                    todoCreateFragment.show(supportFragmentManager, "CREATE")
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
        popup.show()
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemCreateTodo -> {
                todoCreateFragment = TodoCreateFragment()
                todoCreateFragment.show(supportFragmentManager, "CREATE")
            }
            R.id.itemClearAllTodo -> {
                todoViewModel.clearAll()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onTodoCreated(todo: Todo) {
        todoViewModel.insert(todo)
    }

    override fun onTodoEdited(todo: Todo) {
        todoViewModel.edit(todo)
    }

    override fun onDateSelected(year: Int, month: Int, day: Int) {
        todoCreateFragment.onDateSelected(year, month+1, day)
    }

}
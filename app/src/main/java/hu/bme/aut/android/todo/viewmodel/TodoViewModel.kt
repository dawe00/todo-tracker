package hu.bme.aut.android.todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.todo.TodoApplication
import hu.bme.aut.android.todo.model.Todo
import hu.bme.aut.android.todo.repository.Repository
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {

    private val repository: Repository

    val allTodos: LiveData<List<Todo>>

    init {
        val todoDao = TodoApplication.todoDatabase.todoDao()
        repository = Repository(todoDao)
        allTodos = repository.getAllTodos()
    }

    fun insert(todo: Todo) = viewModelScope.launch {
        repository.insert(todo)
    }

    fun delete(todo: Todo) = viewModelScope.launch {
        repository.delete(todo)
    }

    fun clearAll() = viewModelScope.launch{
        repository.clearAll()
    }

    fun edit(todo: Todo) = viewModelScope.launch {
        repository.edit(todo)
    }
}
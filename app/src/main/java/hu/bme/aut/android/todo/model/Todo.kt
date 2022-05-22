package hu.bme.aut.android.todo.model

data class Todo(
    val id: Int,
    var title: String,
    var priority: Priority,
    var dueDate: String,
    var description: String
) {
    enum class Priority {
        LOW, MEDIUM, HIGH
    }
}
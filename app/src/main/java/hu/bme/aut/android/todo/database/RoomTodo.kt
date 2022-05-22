package hu.bme.aut.android.todo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import hu.bme.aut.android.todo.model.Todo

@Entity(tableName = "todo")
data class RoomTodo(
    @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val title: String,
        val priority: Todo.Priority,
        val dueDate: String,
        val description: String
)

class TodoTypeConverter {

    companion object {
        const val LOW = "LOW"
        const val MEDIUM = "MEDIUM"
        const val HIGH = "HIGH"
    }

    @TypeConverter
    fun toPriority(value: String?): Todo.Priority {
        return when (value) {
            LOW -> Todo.Priority.LOW
            MEDIUM -> Todo.Priority.MEDIUM
            HIGH -> Todo.Priority.HIGH
            else -> Todo.Priority.LOW
        }
    }

    @TypeConverter
    fun toString(enumValue: Todo.Priority): String {
        return enumValue.name
    }

}
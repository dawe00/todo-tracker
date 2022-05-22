package hu.bme.aut.android.todo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
class TodoDetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_DESC = "KEY_DESC"
        const val KEY_TITLE = "KEY_TITLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)
        setSupportActionBar(findViewById(R.id.detail_toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(KEY_TITLE)
        if (savedInstanceState == null) {
            val fragment = TodoDetailFragment.newInstance(intent.getStringExtra(KEY_DESC)!!, intent.getStringExtra(KEY_TITLE)!!)

            supportFragmentManager.beginTransaction()
                .add(R.id.todo_detail_container, fragment)
                .commit()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                navigateUpTo(Intent(this, TodoListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
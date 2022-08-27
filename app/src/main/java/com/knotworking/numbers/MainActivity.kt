package com.knotworking.numbers

import android.app.Notification.EXTRA_TITLE
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

// view class This class represents the basic building block for user interface components.
// A View occupies a rectangular area on the screen and is responsible for drawing and event handling
class MainActivity : AppCompatActivity() , View.OnClickListener {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val NEW_ITEM_DIALOG = "new_item_dialog"
    }


    private var typeIndex: Int = 0
    private var titleText: String = "Converter"
    private lateinit var adapter: NumbersPagerAdapter
    private lateinit var pager: ViewPager
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the Intent that started this activity and extract the string
        val message = intent.getIntExtra(EXTRA_MESSAGE, 0)
        val title = intent.getStringExtra(EXTRA_TITLE)

        typeIndex = message
        titleText = title.toString()

        adapter = NumbersPagerAdapter(supportFragmentManager, typeIndex)
        pager = findViewById<View>(R.id.activity_view_pager) as ViewPager
        pager.adapter = adapter
        fab = findViewById<View>(R.id.activity_fab) as FloatingActionButton
        fab.setOnClickListener(this)
        setupActionBar()
    }

    //setting menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        android.R.id.home ->{
            this.finish()
//            Toast.makeText(this,"Home action",Toast.LENGTH_LONG).show()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun setupActionBar() {

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = titleText
    }


    override fun onClick(view: View) {
        Log.e(TAG, "CLICK EVENT ====================")
        if (view.id == R.id.activity_fab) {
            showNewItemDialog()
        }
    }

    private fun showNewItemDialog() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag(NEW_ITEM_DIALOG)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

    }

}

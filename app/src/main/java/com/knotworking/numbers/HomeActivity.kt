package com.knotworking.numbers

import android.app.ActionBar
import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.fathzer.soft.javaluator.DoubleEvaluator
import com.knotworking.numbers.converter.UnitCode.TYPE_CURRENCY
import com.knotworking.numbers.converter.UnitCode.TYPE_DISTANCE
import com.knotworking.numbers.converter.UnitCode.TYPE_MASS
import com.knotworking.numbers.converter.UnitCode.TYPE_POWER
import com.knotworking.numbers.converter.UnitCode.TYPE_TEMPERATURE
import com.knotworking.numbers.converter.UnitCode.TYPE_VOLUMN

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var e1: EditText? = null
    private var e2: EditText? = null
    private var bb: Button? = null
    private var count = 0
    private var expression = ""
    private var text = ""
    private var result = 0.0
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var typeNames = arrayOf("Weight", "Temperature", "Length", "Volumn", "Power", "Currency")
    var toolba: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        bb = findViewById(R.id.openBracket)
        e1 = findViewById(R.id.editText1)
        e2 = findViewById(R.id.editText2)
        e2!!.setText("")
        drawerLayout = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.design_navigation_view)
        toolba = findViewById(R.id.tool)
        //  navigationView.bringToFront();
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolba, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()
        navigationView!!.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.num0 -> e2!!.setText(e2!!.text.toString() + "0")
            R.id.num1 -> e2!!.setText(e2!!.text.toString() + "1")
            R.id.num2 -> e2!!.setText(e2!!.text.toString() + "2")
            R.id.num3 -> e2!!.setText(e2!!.text.toString() + "3")
            R.id.num4 -> e2!!.setText(e2!!.text.toString() + "4")
            R.id.num5 -> e2!!.setText(e2!!.text.toString() + "5")
            R.id.num6 -> e2!!.setText(e2!!.text.toString() + "6")
            R.id.num7 -> e2!!.setText(e2!!.text.toString() + "7")
            R.id.num8 -> e2!!.setText(e2!!.text.toString() + "8")
            R.id.num9 -> e2!!.setText(e2!!.text.toString() + "9")
            R.id.dot -> if (count == 0 && e2!!.length() != 0) {
                e2!!.setText(e2!!.text.toString() + ".")
                count++
            }
            R.id.clear -> {
                e1!!.setText("")
                e2!!.setText("")
                count = 0
                expression = ""
            }
            R.id.backSpace -> {
                text = e2!!.text.toString()
                if (text.length > 0) {
                    if (text.endsWith(".")) {
                        count = 0
                    }
                    var newText = text.substring(0, text.length - 1)
                    if (text.endsWith(")")) {
                        val a = text.toCharArray()
                        var pos = a.size - 2
                        var counter = 1
                        var i = a.size - 2
                        while (i >= 0) {
                            if (a[i] == ')') {
                                counter++
                            } else if (a[i] == '(') {
                                counter--
                            } else if (a[i] == '.') {
                                count = 0
                            }
                            if (counter == 0) {
                                pos = i
                                break
                            }
                            i--
                        }
                        newText = text.substring(0, pos)
                    }
                    if (newText == "-" || newText.endsWith("sqrt")) {
                        newText = ""
                    } else if (newText.endsWith("^")) newText = newText.substring(0, newText.length - 1)
                    e2!!.setText(newText)
                }
            }
            R.id.plus -> operationClicked("+")
            R.id.minus -> operationClicked("-")
            R.id.divide -> operationClicked("/")
            R.id.multiply -> operationClicked("*")
            R.id.sqrt -> if (e2!!.length() != 0) {
                text = e2!!.text.toString()
                e2!!.setText("sqrt($text)")
            }
            R.id.square -> if (e2!!.length() != 0) {
                text = e2!!.text.toString()
                e2!!.setText("($text)^2")
            }
            R.id.posneg -> if (e2!!.length() != 0) {
                val s = e2!!.text.toString()
                val arr = s.toCharArray()
                if (arr[0] == '-') e2!!.setText(s.substring(1, s.length)) else e2!!.setText("-$s")
            }
            R.id.equal -> {
                if (e2!!.length() != 0) {
                    text = e2!!.text.toString()
                    expression = e1!!.text.toString() + text
                }
                e1!!.setText("")
                if (expression.length == 0) expression = "0.0"
                val evaluator = DoubleEvaluator()
                try {
                    result = ExtendedDoubleEvaluator().evaluate(expression)
                    if (expression != "0.0") e2!!.setText(result.toString() + "")
                } catch (e: Exception) {
                    e2!!.setText("Invalid Expression")
                    e1!!.setText("")
                    expression = ""
                    e.printStackTrace()
                }
            }
            R.id.openBracket -> e1!!.setText(e1!!.text.toString() + "(")
            R.id.closeBracket -> e1!!.setText(e1!!.text.toString() + ")")
        }
    }

    private fun operationClicked(op: String) {
        if (e2!!.length() != 0) {
            val text = e2!!.text.toString()
            e1!!.setText(e1!!.text.toString() + text + op)
            e2!!.setText("")
            count = 0
        } else {
            val text = e1!!.text.toString()
            if (text.length > 0) {
                val newText = text.substring(0, text.length - 1) + op
                e1!!.setText(newText)
            }
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        drawerLayout!!.closeDrawer(GravityCompat.START)

        when(menuItem.itemId) {
            R.id.volume ->{

                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(AlarmClock.EXTRA_MESSAGE, TYPE_VOLUMN)
                    putExtra(Notification.EXTRA_TITLE, typeNames[TYPE_VOLUMN].toString())
                }
                startActivity(intent)
            }
            R.id.currency ->{

                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(AlarmClock.EXTRA_MESSAGE, TYPE_CURRENCY)
                    putExtra(Notification.EXTRA_TITLE, typeNames[TYPE_CURRENCY].toString())
                }
                startActivity(intent)
            }
            R.id.length ->{

                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(AlarmClock.EXTRA_MESSAGE, TYPE_DISTANCE)
                    putExtra(Notification.EXTRA_TITLE, typeNames[TYPE_DISTANCE].toString())
                }
                startActivity(intent)
            }
            R.id.weight ->{

                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(AlarmClock.EXTRA_MESSAGE, TYPE_MASS)
                    putExtra(Notification.EXTRA_TITLE, typeNames[TYPE_MASS].toString())
                }
                startActivity(intent)
            }
            R.id.temperature ->{

                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(AlarmClock.EXTRA_MESSAGE, TYPE_TEMPERATURE)
                    putExtra(Notification.EXTRA_TITLE, typeNames[TYPE_TEMPERATURE].toString())
                }
                startActivity(intent)
            }
            R.id.energy ->{

                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(AlarmClock.EXTRA_MESSAGE, TYPE_POWER)
                    putExtra(Notification.EXTRA_TITLE, typeNames[TYPE_POWER].toString())
                }
                startActivity(intent)
            }
            else -> { // Note the block
                Toast.makeText(this, "No action yet!", Toast.LENGTH_SHORT).show()
            }

        }
        return true
    }
}
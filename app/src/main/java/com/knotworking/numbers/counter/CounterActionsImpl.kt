package com.knotworking.numbers.counter

import android.content.Context
import com.knotworking.numbers.MainActivity
import com.knotworking.numbers.database.DatabaseHelper
import com.knotworking.numbers.database.DatabaseHelperImpl


class CounterActionsImpl(private val context: Context) : CounterActions {
    //TODO inject
    private val databaseHelper: DatabaseHelper

    init {
        this.databaseHelper = DatabaseHelperImpl(context)
    }

    override fun itemLongClick(item: CounterItem): Boolean {
        showEditItemDialog()
        return true
    }

    override fun incrementCount(id: Int) {
        databaseHelper.modifyCount(id, 1)
    }

    override fun decrementCount(id: Int) {
        databaseHelper.modifyCount(id, -1)
    }
// this method modifies the fragments by transactions
    private fun showEditItemDialog() {
        val activity = context as MainActivity
        val fragmentManager = activity.supportFragmentManager
        val ft = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag(EDIT_DIALOG)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

    }

    companion object {
        private const val EDIT_DIALOG = "edit_dialog"
    }

}

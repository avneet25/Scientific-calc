package com.knotworking.numbers.counter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.knotworking.numbers.R
import com.knotworking.numbers.database.DatabaseContract

class CounterFragment : Fragment(), LoaderManager.LoaderCallbacks<List<CounterItem>> {

    private val COUNTER_LOADER = 1
//A view previously used to display data for a specific adapter position may be placed in a
// cache for later reuse to display the same type of data again later.
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CounterAdapter
//Called to have the fragment instantiate its user interface view.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_counter, container, false)
        recyclerView = root.findViewById<View>(R.id.fragment_counter_recyclerView) as RecyclerView
        return root
    }
// Called when the fragment's activity has been created and this fragment's view hierarchy instantiated.
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator?.changeDuration = 0
        adapter = CounterAdapter(context!!)
        recyclerView.adapter = adapter

        val data: List<CounterItem> = listOf(CounterItem(0,"FSDFS",34), CounterItem(0,"FSDFS",34),CounterItem(0,"FSDFS",34))

        adapter.setData(data)
//        loaderManager.initLoader(COUNTER_LOADER, null, this@CounterFragment)
    }
//Instantiate and return a new Loader for the given ID.
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<CounterItem>> {
        val uri = DatabaseContract.Counters.CONTENT_URI
        val projection = arrayOf(DatabaseContract.Counters.COL_ID,
                DatabaseContract.Counters.COL_NAME,
                DatabaseContract.Counters.COL_COUNT)
        return CounterListLoader(activity, uri, projection, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<List<CounterItem>>, data: List<CounterItem>?) {
        adapter.setData(data)
    }

    override fun onLoaderReset(loader: Loader<List<CounterItem>>) {
        adapter.setData(null)
    }
}

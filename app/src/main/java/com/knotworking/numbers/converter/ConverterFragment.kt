package com.knotworking.numbers.converter

import android.content.Context
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.knotworking.numbers.R
import com.knotworking.numbers.converter.history.HistoryCursorConverter
import com.knotworking.numbers.database.DatabaseContract
import com.knotworking.numbers.databinding.FragmentConverterBinding

class ConverterFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private val EXCHANGE_RATE_LOADER = 10
    private val CONVERSION_HISTORY_LOADER = 20
    private lateinit var binding: FragmentConverterBinding

    private var typeCode = 0
// You can now use the instance of the binding class to reference any of the views after this function
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_converter, container, false)
        binding.viewModel = ConverterViewModel(this, typeCode)
        return binding.root
    }
//Called when the fragment's activity has been created and this fragment's view hierarchy instantiated
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//Ensures a loader is initialized and active.
// If the loader doesn't already exist, one is created and (if the activity/fragment is currently started) starts the loader.
        loaderManager.initLoader(CONVERSION_HISTORY_LOADER, null, this)
        loaderManager.initLoader(EXCHANGE_RATE_LOADER, null, this)
    }

    fun loadHistoryItem(item: ConversionItem) {
        binding.viewModel?.setConversionItem(item)
    }
//Instantiate and return a new Loader for the given ID.
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        when (id) {
            EXCHANGE_RATE_LOADER -> {
                val uri = DatabaseContract.ExchangeRates.CONTENT_URI
                val projection = arrayOf(DatabaseContract.ExchangeRates.COL_CURRENCY,
                        DatabaseContract.ExchangeRates.COL_RATE)
                //Creates a fully-specified CursorLoader
                return CursorLoader(context!!, uri, projection, null, null, null)
            }
            else -> {
                val uri = DatabaseContract.ConversionHistory.CONTENT_URI
                val projection = arrayOf(DatabaseContract.ConversionHistory.COL_ID,
                        DatabaseContract.ConversionHistory.COL_UNIT_TYPE_CODE,
                        DatabaseContract.ConversionHistory.COL_INPUT_UNIT_CODE,
                        DatabaseContract.ConversionHistory.COL_INPUT_VALUE,
                        DatabaseContract.ConversionHistory.COL_OUTPUT_UNIT_CODE,
                        DatabaseContract.ConversionHistory.COL_OUTPUT_VALUE)
                val sortOrder = DatabaseContract.ConversionHistory.COL_ID + " DESC"
                return CursorLoader(context!!, uri, projection, null, null, sortOrder)
            }
        }
    }
//Called when a previously created loader has finished its load
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        when (loader.id) {
            EXCHANGE_RATE_LOADER -> {
                data?.let {
                    binding.viewModel!!.exchangeRates = ExchangeRateCursorConverter.getData(it)
                }
            }
            CONVERSION_HISTORY_LOADER -> {
                data?.let {
                    binding.viewModel!!.historyAdapter.setData(HistoryCursorConverter.getData(it))
                }
            }
        }
    }
//Called when a previously created loader is being reset, and thus making its data unavailable.
    override fun onLoaderReset(loader: Loader<Cursor>) {
        when (loader.id) {
            EXCHANGE_RATE_LOADER -> binding.viewModel!!.exchangeRates = emptyMap()
            CONVERSION_HISTORY_LOADER -> binding.viewModel!!.historyAdapter.setData(emptyList())
        }

    }
//Called when a fragment is first attached to its context.
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.getInt("TYPE_CODE")?.let {
            typeCode = it
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(typeCoode: Int) = ConverterFragment().apply {
            //Supply the construction arguments for this fragment.
            // The arguments supplied here will be retained across fragment destroy and creation.
            arguments = Bundle().apply {
                putInt("TYPE_CODE", typeCoode)
            }
        }
    }


}

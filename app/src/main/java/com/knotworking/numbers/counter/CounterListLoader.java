package com.knotworking.numbers.counter;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;


public class CounterListLoader extends AsyncTaskLoader<List<CounterItem>> {

    // We hold a reference to the Loader’s data here.
    private List<CounterItem> data;
    private Cursor cursor;
    private ForceLoadContentObserver observer;

    private Uri uri;
    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;

    /**
     * Creates a fully-specified CursorLoader.  See
     * {@link ContentResolver#query(Uri, String[], String, String[], String)
     * ContentResolver.query()} for documentation on the meaning of the
     * parameters.  These will be passed as-is to that call.
     */
    public CounterListLoader(Context context, Uri uri, String[] projection, String selection,
                          String[] selectionArgs, String sortOrder) {
        super(context);
        this.observer = new ForceLoadContentObserver();
        this.uri = uri;
        this.projection = projection;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.sortOrder = sortOrder;
    }

    /****************************************************/
    /** (1) A task that performs the asynchronous load **/
    /****************************************************/

    @Override
    public List<CounterItem> loadInBackground() {
        // This method is called on a background thread and should generate a
        // new set of data to be delivered back to the client.
        List<CounterItem> data = new ArrayList<>();

        cursor = getContext().getContentResolver().query(
                uri, projection, selection, selectionArgs, sortOrder);
        if (cursor != null) {
            try {
                // Ensure the cursor window is filled.
                int length = cursor.getCount();
                cursor.registerContentObserver(observer);
                if (length > 0) {
                    data = CounterCursorConverter.INSTANCE.getData(cursor);
                } else {
                    data = null;
                }
            } catch (RuntimeException ex) {
                cursor.close();
                throw ex;
            }
        }
        return data;
    }

    /********************************************************/
    /** (2) Deliver the results to the registered listener **/
    /********************************************************/

    @Override
    public void deliverResult(List<CounterItem> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            if (cursor != null) {
                cursor.close();
            }

            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<CounterItem> oldData = this.data;
        this.data = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    /*********************************************************/
    /** (3) Implement the Loader’s state-dependent behavior **/
    /*********************************************************/

    @Override
    protected void onStartLoading() {
        if (data != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(data);
        }

        if (takeContentChanged() || data == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'data'.
        if (data != null) {
            releaseResources(data);
            data = null;
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = null;

        // The Loader is being reset, so we should stop monitoring for changes.
        if (observer != null) {
            observer = null;
        }
    }

    @Override
    public void onCanceled(List<CounterItem> data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);

        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }

    private void releaseResources(List<CounterItem> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
    }
}

package com.bestgood.commons.persist;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by dengdingchun on 15/10/25.
 * A custom Loader that loads all of the installed applications.
 */
public class SerializeObjectLoader<D extends Object> extends AsyncTaskLoader<D> {

    private Context mContext;
    private String mFilename;

    private D mData;

    public SerializeObjectLoader(Context context, String filename) {
        super(context);

        mContext = context;
        mFilename = filename;

        onContentChanged();
    }

    /**
     * This is where the bulk of our work is done.  This function is
     * called in a background thread and should generate a new set of
     * data to be published by the loader.
     */
    @Override
    public D loadInBackground() {
        mData = SerializeUtils.readSerializableObject(mContext, mFilename);
        return mData;
    }

    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
//    @Override
//    public void deliverResult(T data) {
//        super.deliverResult(data);
//        if (isReset()) {
//            // An async query came in while the loader is stopped.  We
//            // don't need the result.
//            if (mObj != null) {
//                onReleaseResources(mObj);
//            }
//        }
//
//
//        if (isStarted()) {
//            // If the Loader is currently started, we can immediately
//            // deliver its results.
//            super.deliverResult(mObj);
//        }
//
//        // At this point we can release the resources associated with
//        // 'oldApps' if needed; now that the new result is delivered we
//        // know that it is no longer in use.
//        if (mObj != null) {
//            onReleaseResources(mObj);
//        }
//    }


    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // If we currently have a result available, deliver it mmediately.
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(D data) {
        super.onCanceled(data);

        // At this point we can release the resources associated with 'apps' if needed.
        onReleaseResources(data);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps' if needed.
        if (mData != null) {
            onReleaseResources(mData);
            mData = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(D data) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }
}

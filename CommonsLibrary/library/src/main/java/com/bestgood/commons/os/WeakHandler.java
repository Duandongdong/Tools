package com.bestgood.commons.os;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;

/**
 * @param <T>
 * @author ddc
 * @date: Jun 15, 2014 12:12:06 AM
 */
public abstract class WeakHandler<T> extends Handler {
    private WeakReference<T> mOwner;

    public WeakHandler(T owner) {
        mOwner = new WeakReference<T>(owner);
    }

    public WeakHandler(Looper looper, T owner) {
        super(looper);
        mOwner = new WeakReference<T>(owner);
    }

    public T getOwner() {
        return mOwner.get();
    }
}

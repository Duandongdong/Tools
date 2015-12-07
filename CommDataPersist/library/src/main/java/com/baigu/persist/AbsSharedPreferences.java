package com.baigu.persist;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * Created by dengdingchun on 15/10/29.
 * <p/>
 * 封装存储数据到SharedPreferences中的方法
 */
public abstract class AbsSharedPreferences {
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPref;

    protected AbsSharedPreferences(Context context, String name, int mode) {
        this.mPref = context.getSharedPreferences(name, mode);
        this.mEditor = mPref.edit();
    }

    //==============================================================================================
    protected void clear() {
        mEditor.clear();
        mEditor.apply();
    }

    protected void remove(String key) {
        mEditor.remove(key);
        mEditor.apply();
    }

    protected void clearCommit() {
        mEditor.clear();
        mEditor.commit();
    }

    protected void removeCommit(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

    //==============================================================================================
    protected void put(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    protected void put(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.apply();
    }

    protected void put(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    protected void put(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.apply();
    }

    protected void put(String key, String value) {
        mEditor.putString(key, value);
        mEditor.apply();
    }

    protected void put(String key, Set<String> values) {
        mEditor.putStringSet(key, values);
        mEditor.apply();
    }
    //==============================================================================================

    protected void putCommit(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    protected void putCommit(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    protected void putCommit(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    protected void putCommit(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    protected void putCommit(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    protected void putCommit(String key, Set<String> values) {
        mEditor.putStringSet(key, values);
        mEditor.commit();
    }

    //==============================================================================================
    protected boolean getBoolean(String key, boolean defValues) {
        return mPref.getBoolean(key, defValues);
    }

    protected float getFloat(String key, float defValues) {
        return mPref.getFloat(key, defValues);
    }

    protected int getInt(String key, int defValues) {
        return mPref.getInt(key, defValues);
    }

    protected long getLong(String key, long defValues) {
        return mPref.getLong(key, defValues);
    }

    protected String getString(String key, String defValues) {
        return mPref.getString(key, defValues);
    }

    protected Set<String> getStringSet(String key, Set<String> defValues) {
        return mPref.getStringSet(key, defValues);
    }

    //==============================================================================================

    /**
     * Checks whether the preferences contains a preference.
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return mPref.contains(key);
    }

    /**
     * Retrieve all values from the preferences.
     *
     * @return
     */
    public Map<String, ?> getAll() {
        return mPref.getAll();
    }


    /**
     * Registers a callback to be invoked when a change happens to a preference.
     *
     * @param listener
     */
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mPref.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Unregisters a previous callback.
     *
     * @param listener
     */
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mPref.unregisterOnSharedPreferenceChangeListener(listener);
    }

}

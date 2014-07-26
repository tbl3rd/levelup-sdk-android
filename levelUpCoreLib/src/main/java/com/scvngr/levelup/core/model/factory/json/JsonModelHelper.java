/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.factory.json;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.model.MonetaryValue;
import com.scvngr.levelup.core.model.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * A class to make it easier to extract LevelUp model information from a JSON object.
 */
public final class JsonModelHelper {

    private final JSONObject mJsonObject;

    /**
     * @param jsonObject the JSON object to retrieve values from.
     */
    public JsonModelHelper(final JSONObject jsonObject) {
        mJsonObject = jsonObject;
    }

    /**
     * @param name the key name.
     * @return the value at the given key.
     * @throws JSONException if no such mapping exists.
     * @see JSONObject#get(String)
     */
    @NonNull
    public Object get(final String name) throws JSONException {
        return mJsonObject.get(name);
    }

    /**
     * @param name the key name.
     * @return the value at the given key.
     * @throws JSONException if the value cannot be coerced into a boolean.
     * @see JSONObject#getBoolean(String)
     */
    public boolean getBoolean(final String name) throws JSONException {
        return mJsonObject.getBoolean(name);
    }

    /**
     * @param name the key name.
     * @return the value at the given key.
     * @throws JSONException if the value cannot be coerced into a double.
     * @see JSONObject#getDouble(String)
     */
    public double getDouble(final String name) throws JSONException {
        return mJsonObject.getDouble(name);
    }

    /**
     * @param key the key name.
     * @return the value at the given key.
     * @throws JSONException if the value cannot be coerced into an int.
     * @see JSONObject#getInt(String)
     */
    public int getInt(final String key) throws JSONException {
        return mJsonObject.getInt(key);
    }

    /**
     * @param name the key name.
     * @return the value at the given key.
     * @throws JSONException if the value cannot be coerced into a JSONArray.
     * @see JSONObject#getJSONArray(String)
     */
    @NonNull
    public JSONArray getJSONArray(final String name) throws JSONException {
        return mJsonObject.getJSONArray(name);
    }

    /**
     * @param name the key name.
     * @return the value at the given key.
     * @throws JSONException if the value cannot be coerced into a JSONObject.
     * @see JSONObject#getJSONObject(String)
     */
    @NonNull
    public JSONObject getJSONObject(final String name) throws JSONException {
        return mJsonObject.getJSONObject(name);
    }

    /**
     * @param name the key name.
     * @return the value at the given key.
     * @throws JSONException if the value cannot be coerced into an long.
     * @see JSONObject#getLong(String)
     */
    public long getLong(final String name) throws JSONException {
        return mJsonObject.getLong(name);
    }

    /**
     * @param name the key name.
     * @return the value at the given key.
     * @throws JSONException if the value isn't a number or is missing.
     */
    @NonNull
    public MonetaryValue getMonetaryValue(final String name) throws JSONException {
        return JsonUtils.getMonetaryValue(mJsonObject, name);
    }

    /**
     * @param name the key name.
     * @return the value at the given key.
     * @throws JSONException if the value cannot be coerced into an String.
     * @see JSONObject#getString(String)
     */
    @NonNull
    public String getString(final String name) throws JSONException {
        return mJsonObject.getString(name);
    }

    /**
     * @param name the name of the object to check for.
     * @return if the {@link JSONObject} has a value for the name passed.
     */
    public boolean has(final String name) {
        return mJsonObject.has(name);
    }

    /**
     * @param name the key name.
     * @return the value at the given key name or {@code null} if it is missing.
     * @see JSONObject#opt(String)
     */
    @Nullable
    public Object opt(final String name) {
        return mJsonObject.opt(name);
    }

    /**
     * @param name the key name.
     * @return the value at the given key or {@code false} if it cannot be coerced into a boolean
     * @see JSONObject#optBoolean(String)
     */
    public boolean optBoolean(final String name) {
        return mJsonObject.optBoolean(name);
    }

    /**
     * @param name the key name.
     * @param fallback the value to be returned if the mapping is missing or null.
     * @return the value at the given key or {@code fallback} if it's null.
     * @see JSONObject#optBoolean(String, boolean)
     */
    public boolean optBoolean(final String name, final boolean fallback) {
        return mJsonObject.optBoolean(name, fallback);
    }

    /**
     * @param name the key name.
     * @return the value at the given key or {@code 0} if it cannot be coerced into a double.
     * @see JSONObject#optDouble(String)
     */
    public double optDouble(final String name) {
        return mJsonObject.optDouble(name);
    }

    /**
     * @param name the key name.
     * @param fallback the value to be returned if the mapping is missing or null.
     * @return the value at the given key or {@code fallback} if it's null.
     * @see JSONObject#optDouble(String, double)
     */
    public double optDouble(final String name, final double fallback) {
        return mJsonObject.optDouble(name, fallback);
    }

    /**
     * @param name the key name.
     * @return a long value at the given key or {@code 0} if it cannot be coerced into an int.
     * @see JSONObject#optInt(String)
     */
    public int optInt(final String name) {
        return mJsonObject.optInt(name);
    }

    /**
     * @param name the key name.
     * @param fallback the value to be returned if the mapping is missing or null.
     * @return a long value at the given key or {@code fallback} if it's null.
     * @see JSONObject#optLong(String, long)
     */
    public int optInt(final String name, final int fallback) {
        return mJsonObject.optInt(name, fallback);
    }

    /**
     * @param name the key name.
     * @return the mapped value at the given key if it's a JSONArray or {@code null} otherwise.
     * @see JSONObject#optJSONArray(String)
     */
    @Nullable
    public JSONArray optJSONArray(final String name) {
        return mJsonObject.optJSONArray(name);
    }

    /**
     * @param name the key name.
     * @return the mapped value at the given key if it's a JSONObject or {@code null} otherwise.
     * @see JSONObject#optJSONObject(String)
     */
    @Nullable
    public JSONObject optJSONObject(final String name) {
        return mJsonObject.optJSONObject(name);
    }

    /**
     * @param name the key name.
     * @return the value at the given key or {@code 0} if it's null.
     */
    public long optLong(final String name) {
        return mJsonObject.optLong(name);
    }

    /**
     * @param name the key name.
     * @param fallback the fallback value if the mapping is missing or null.
     * @return a long value at the given key or {@code fallback} if it's null.
     * @see JSONObject#optLong(String, long)
     */
    public long optLong(final String name, final long fallback) {
        return mJsonObject.optLong(name, fallback);
    }

    /**
     * @param name the key name.
     * @return the value at the given key or {@code null} if it's null.
     */
    @Nullable
    public Long optLongNullable(final String name) {
        Long value;

        if (mJsonObject.isNull(name)) {
            value = null;
        } else {
            value = mJsonObject.optLong(name);
        }
        return value;
    }

    /**
     * @param name the key name.
     * @return the value at the given key or {@code null} if it's null or missing.
     * @throws JSONException if the value isn't a number.
     */
    @Nullable
    public MonetaryValue optMonetaryValue(final String name) throws JSONException {
        return JsonUtils.optMonetaryValue(mJsonObject, name);
    }

    /**
     * @param name the key name.
     * @return the value at the given key or {@code null} if it's null or missing.
     * @see JsonUtils#optString(JSONObject, String)
     */
    @Nullable
    public String optString(final String name) {
        return JsonUtils.optString(mJsonObject, name);
    }

    /**
     * @param name the key name.
     * @param fallback the fallback value if the mapping is missing or null.
     * @return the value at the given key or {@code fallback} if it's null.
     * @see JSONObject#optString(String, String)
     */
    @NonNull
    public String optString(final String name, final String fallback) {
        return mJsonObject.optString(name, fallback);
    }

    /**
     * @param name the key name.
     * @return the value at the given key or {@code null} if it's null or missing.
     * @throws JSONException if the array contents aren't strings
     * @see JsonUtils#optStringSet(JSONObject, String)
     */
    @Nullable
    public Set<String> optStringSet(final String name) throws JSONException {
        return JsonUtils.optStringSet(mJsonObject, name);
    }
}

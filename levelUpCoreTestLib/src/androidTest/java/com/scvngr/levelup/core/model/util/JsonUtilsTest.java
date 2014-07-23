/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model.util;

import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests {@link com.scvngr.levelup.core.model.util.JsonUtils}.
 */
public final class JsonUtilsTest extends SupportAndroidTestCase {

    /**
     * Tests {@link com.scvngr.levelup.core.model.util.JsonUtils#optString(org.json.JSONObject, String)}.
     *
     * @throws org.json.JSONException if there's a parse error.
     */
    @SmallTest
    public void testOptString() throws JSONException {
        final JSONObject object1 = new JSONObject("{ 'test': null }");

        // JsonObject returns a non-null value, we shouldn't.
        assertNotNull(object1.optString("test"));
        assertNull(JsonUtils.optString(object1, "test"));

        final JSONObject object2 = new JSONObject("{ 'test': 'null' }");
        assertEquals("null", JsonUtils.optString(object2, "test"));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.util.JsonUtils#optIntegerSet(org.json.JSONObject, String)}.
     *
     * @throws org.json.JSONException if there's a parse error.
     */
    public void testIntegerSet() throws JSONException {
        final JSONObject object1 = new JSONObject("{ 'test': null }");

        Set<Integer> ints = JsonUtils.optIntegerSet(object1, "test");

        assertNull(ints);

        final JSONObject object2 = new JSONObject("{ 'test': [2,3,5,23] }");

        ints = JsonUtils.optIntegerSet(object2, "test");

        assertNotNull(ints);
        assertEquals(new HashSet<Integer>(Arrays.asList(2, 3, 5, 23)), ints);

        assertEquals(new HashSet<Integer>(Arrays.asList(123)),
                JsonUtils.optIntegerSet(new JSONObject("{ 'test': [123] }"), "test"));

        assertEquals(new HashSet<Integer>(Arrays.asList(0)),
                JsonUtils.optIntegerSet(new JSONObject("{ 'test': [0] }"), "test"));

        assertEquals(new HashSet<Integer>(Arrays.asList(1)),
                JsonUtils.optIntegerSet(new JSONObject("{ 'test': [1,1,1] }"), "test"));

        assertEquals(Collections.emptySet(),
                JsonUtils.optIntegerSet(new JSONObject("{ 'test': [] }"), "test"));
    }

    /**
     * Tests {@link com.scvngr.levelup.core.model.util.JsonUtils#optStringSet(org.json.JSONObject, String)}.
     *
     * @throws org.json.JSONException if there's a parse error.
     */
    public void testStringSet() throws JSONException {
        final JSONObject object1 = new JSONObject("{ 'test': null }");

        Set<String> strings = JsonUtils.optStringSet(object1, "test");

        assertNull(strings);

        final JSONObject object2 = new JSONObject("{ 'test': ['foo','bar', 'baz'] }");

        strings = JsonUtils.optStringSet(object2, "test");

        assertNotNull(strings);
        assertEquals(new HashSet<String>(Arrays.asList("foo", "bar", "baz")), strings);

        assertEquals(new HashSet<String>(Arrays.asList("foo")),
                JsonUtils.optStringSet(new JSONObject("{ 'test': ['foo'] }"), "test"));

        assertEquals(new HashSet<String>(Arrays.asList("")),
                JsonUtils.optStringSet(new JSONObject("{ 'test': [''] }"), "test"));

        assertEquals(new HashSet<String>(Arrays.asList("foo")),
                JsonUtils.optStringSet(new JSONObject("{ 'test': ['foo','foo','foo'] }"), "test"));

        assertEquals(Collections.emptySet(),
                JsonUtils.optStringSet(new JSONObject("{ 'test': [] }"), "test"));
    }

    @SmallTest
    public void testOptMonetaryValue() throws JSONException {
        final JSONObject json = new JSONObject("{ 'amount' : 7000 }");

        JsonUtils.optMonetaryValue(json, "amount");
    }
}

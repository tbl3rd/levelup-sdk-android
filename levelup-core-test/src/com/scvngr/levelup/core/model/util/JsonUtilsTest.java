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
 * Tests {@link JsonUtils}.
 */
public final class JsonUtilsTest extends SupportAndroidTestCase {

    /**
     * Tests {@link JsonUtils#optString(org.json.JSONObject, String)}.
     *
     * @throws JSONException if there's a parse error.
     */
    @SmallTest
    public void testOptString() throws JSONException {
        final JSONObject object1 = new JSONObject("{ 'test': null }"); //$NON-NLS-1$

        // JsonObject returns a non-null value, we shouldn't.
        assertNotNull(object1.optString("test")); //$NON-NLS-1$
        assertNull(JsonUtils.optString(object1, "test")); //$NON-NLS-1$

        final JSONObject object2 = new JSONObject("{ 'test': 'null' }"); //$NON-NLS-1$
        assertEquals("null", JsonUtils.optString(object2, "test")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Tests {@link JsonUtils#optIntegerSet(JSONObject, String)}.
     *
     * @throws JSONException if there's a parse error.
     */
    public void testIntegerSet() throws JSONException {
        final JSONObject object1 = new JSONObject("{ 'test': null }"); //$NON-NLS-1$

        Set<Integer> ints = JsonUtils.optIntegerSet(object1, "test"); //$NON-NLS-1$

        assertNull(ints);

        final JSONObject object2 = new JSONObject("{ 'test': [2,3,5,23] }"); //$NON-NLS-1$

        ints = JsonUtils.optIntegerSet(object2, "test"); //$NON-NLS-1$

        assertNotNull(ints);
        assertEquals(new HashSet<Integer>(Arrays.asList(2, 3, 5, 23)), ints);

        assertEquals(new HashSet<Integer>(Arrays.asList(123)),
                JsonUtils.optIntegerSet(new JSONObject("{ 'test': [123] }"), "test")); //$NON-NLS-1$ //$NON-NLS-2$

        assertEquals(new HashSet<Integer>(Arrays.asList(0)),
                JsonUtils.optIntegerSet(new JSONObject("{ 'test': [0] }"), "test")); //$NON-NLS-1$ //$NON-NLS-2$

        assertEquals(new HashSet<Integer>(Arrays.asList(1)),
                JsonUtils.optIntegerSet(new JSONObject("{ 'test': [1,1,1] }"), "test")); //$NON-NLS-1$ //$NON-NLS-2$

        assertEquals(Collections.emptySet(),
                JsonUtils.optIntegerSet(new JSONObject("{ 'test': [] }"), "test")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Tests {@link JsonUtils#optStringSet(JSONObject, String)}.
     *
     * @throws JSONException if there's a parse error.
     */
    public void testStringSet() throws JSONException {
        final JSONObject object1 = new JSONObject("{ 'test': null }"); //$NON-NLS-1$

        Set<String> strings = JsonUtils.optStringSet(object1, "test"); //$NON-NLS-1$

        assertNull(strings);

        final JSONObject object2 = new JSONObject("{ 'test': ['foo','bar', 'baz'] }"); //$NON-NLS-1$

        strings = JsonUtils.optStringSet(object2, "test"); //$NON-NLS-1$

        assertNotNull(strings);
        assertEquals(new HashSet<String>(Arrays.asList("foo", "bar", "baz")), strings); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        assertEquals(new HashSet<String>(Arrays.asList("foo")), //$NON-NLS-1$
                JsonUtils.optStringSet(new JSONObject("{ 'test': ['foo'] }"), "test")); //$NON-NLS-1$ //$NON-NLS-2$

        assertEquals(new HashSet<String>(Arrays.asList("")), //$NON-NLS-1$
                JsonUtils.optStringSet(new JSONObject("{ 'test': [''] }"), "test")); //$NON-NLS-1$ //$NON-NLS-2$

        assertEquals(new HashSet<String>(Arrays.asList("foo")), //$NON-NLS-1$
                JsonUtils.optStringSet(new JSONObject("{ 'test': ['foo','foo','foo'] }"), "test")); //$NON-NLS-1$ //$NON-NLS-2$

        assertEquals(Collections.emptySet(),
                JsonUtils.optStringSet(new JSONObject("{ 'test': [] }"), "test")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @SmallTest
    public void testOptMonetaryValue() throws JSONException {
        final JSONObject json = new JSONObject("{ 'amount' : 7000 }"); //$NON-NLS-1$

        JsonUtils.optMonetaryValue(json, "amount"); //$NON-NLS-1$
    }
}

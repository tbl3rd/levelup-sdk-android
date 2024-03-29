/*
 * Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.scvngr.levelup.core.model.factory.json;

import android.support.annotation.NonNull;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.AbstractJsonModelFactory}.
 */
public final class AbstractJsonModelFactoryTest extends SupportAndroidTestCase {

    @SmallTest
    public void testGetTypeKey() {
        final String key = "test_key";

        final TestModelFactory factory = new TestModelFactory(key);
        assertEquals(key, factory.getTypeKey());
    }

    @SmallTest
    public void testFromJsonObject_nestedModel() throws JSONException {
        final JSONObject obj = new JSONObject();
        obj.put("test", getValidJSONObject());

        final TestModel model = new TestModelFactory("test").from(obj);
        assertFalse("should not have model key", model.object.has("test"));
    }

    @SmallTest
    public void testFromJsonObject_nonNestedModel() throws JSONException {
        final TestModel model = new TestModelFactory("test").from(getValidJSONObject());
        assertFalse("should not have model key", model.object.has("test"));
    }

    @SmallTest
    public void testFromJsonObject_unrecognizedModelKey() throws JSONException {
        final JSONObject obj = new JSONObject();
        obj.put("no", getValidJSONObject());

        final TestModel model = new TestModelFactory("test").from(obj);
        assertEquals(obj, model.object);
    }

    @SmallTest
    public void testFromJsonObject_singleKeyObject() throws JSONException {
        final JSONObject obj = new JSONObject();
        obj.put("test", "this is a test value");

        final TestModel model = new TestModelFactory("test").from(obj);
        assertEquals(obj, model.object);
    }

    @SmallTest
    public void testFromListJsonArray_nonNestedModels() throws JSONException {
        final JSONObject obj = getValidJSONObject();
        final JSONArray array = new JSONArray();
        array.put(obj);
        array.put(obj);
        array.put(obj);
        array.put(obj);

        final List<TestModel> models = new TestModelFactory("test").fromList(array);
        assertEquals(4, models.size());

        for (final TestModel model : models) {
            assertEquals(obj, model.object);
        }
    }

    @SmallTest
    public void testFromListJsonArray_nestedModels() throws JSONException {
        final JSONObject obj = new JSONObject();
        obj.put("test", getValidJSONObject());
        final JSONArray array = new JSONArray();
        array.put(obj);
        array.put(obj);
        array.put(obj);
        array.put(obj);

        final List<TestModel> models = new TestModelFactory("test").fromList(array);
        assertEquals(4, models.size());

        for (final TestModel model : models) {
            assertFalse("should not have model key", model.object.has("test"));
            assertNotSame(obj, model.object);
        }
    }

    @SmallTest
    public void testFromListJsonArray_varyingNestedAndUnNestedModels() throws JSONException {
        final JSONObject obj = new JSONObject();
        obj.put("test", getValidJSONObject());
        final JSONArray array = new JSONArray();
        array.put(obj);
        array.put(getValidJSONObject());
        array.put(getValidJSONObject());
        array.put(obj);

        final List<TestModel> models = new TestModelFactory("test").fromList(array);
        assertEquals(4, models.size());

        for (final TestModel model : models) {
            assertFalse("should not have model key", model.object.has("test"));
            assertNotSame(obj, model.object);
        }
    }

    /**
     * Gets a valid JSON object.
     *
     * @return {@link org.json.JSONObject}
     * @throws org.json.JSONException if something fails...
     */
    @NonNull
    private JSONObject getValidJSONObject() throws JSONException {
        final JSONObject obj = new JSONObject();
        obj.put("var1", "var1-value");
        obj.put("var2", "var2-value");
        return obj;
    }

    /**
     * Test model impl.
     */
    @NotThreadSafe
    private static final class TestModel {

        public final JSONObject object;

        public TestModel(final JSONObject obj) {
            object = obj;
        }
    }

    /**
     * Test implementation of {@link com.scvngr.levelup.core.model.factory.json.AbstractJsonModelFactory}.
     */
    @Immutable
    private static final class TestModelFactory extends AbstractJsonModelFactory<TestModel> {

        protected TestModelFactory(@NonNull final String typeKey) {
            super(typeKey);
        }

        @Override
        @NonNull
        public TestModel createFrom(@NonNull final JSONObject json) throws JSONException {
            return new TestModel(json);
        }
    }
}

package com.scvngr.levelup.core.model.factory.json;

import android.test.suitebuilder.annotation.SmallTest;

import java.util.List;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

/**
 * Tests {@link AbstractJsonModelFactory}.
 */
public final class AbstractJsonModelFactoryTest extends SupportAndroidTestCase {

    @SmallTest
    public void testGetTypeKey() {
        final String key = "test_key"; //$NON-NLS-1$

        final TestModelFactory factory = new TestModelFactory(key);
        assertEquals(key, factory.getTypeKey());
    }

    @SmallTest
    public void testFromJsonObject_nestedModel() throws JSONException {
        final JSONObject obj = new JSONObject();
        obj.put("test", getValidJSONObject()); //$NON-NLS-1$

        final TestModel model = new TestModelFactory("test").from(obj); //$NON-NLS-1$
        assertFalse("should not have model key", model.object.has("test")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @SmallTest
    public void testFromJsonObject_nonNestedModel() throws JSONException {
        final TestModel model = new TestModelFactory("test").from(getValidJSONObject()); //$NON-NLS-1$
        assertFalse("should not have model key", model.object.has("test")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @SmallTest
    public void testFromJsonObject_unrecognizedModelKey() throws JSONException {
        final JSONObject obj = new JSONObject();
        obj.put("no", getValidJSONObject()); //$NON-NLS-1$

        final TestModel model = new TestModelFactory("test").from(obj); //$NON-NLS-1$
        assertEquals(obj, model.object);
    }

    @SmallTest
    public void testFromJsonObject_singleKeyObject() throws JSONException {
        final JSONObject obj = new JSONObject();
        obj.put("test", "this is a test value"); //$NON-NLS-1$ //$NON-NLS-2$

        final TestModel model = new TestModelFactory("test").from(obj); //$NON-NLS-1$
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

        final List<TestModel> models = new TestModelFactory("test").fromList(array); //$NON-NLS-1$
        assertEquals(4, models.size());

        for (final TestModel model : models) {
            assertEquals(obj, model.object);
        }
    }

    @SmallTest
    public void testFromListJsonArray_nestedModels() throws JSONException {
        final JSONObject obj = new JSONObject();
        obj.put("test", getValidJSONObject()); //$NON-NLS-1$
        final JSONArray array = new JSONArray();
        array.put(obj);
        array.put(obj);
        array.put(obj);
        array.put(obj);

        final List<TestModel> models = new TestModelFactory("test").fromList(array); //$NON-NLS-1$
        assertEquals(4, models.size());

        for (final TestModel model : models) {
            assertFalse("should not have model key", model.object.has("test")); //$NON-NLS-1$ //$NON-NLS-2$
            assertNotSame(obj, model.object);
        }
    }

    @SmallTest
    public void testFromListJsonArray_varyingNestedAndUnNestedModels() throws JSONException {
        final JSONObject obj = new JSONObject();
        obj.put("test", getValidJSONObject()); //$NON-NLS-1$
        final JSONArray array = new JSONArray();
        array.put(obj);
        array.put(getValidJSONObject());
        array.put(getValidJSONObject());
        array.put(obj);

        final List<TestModel> models = new TestModelFactory("test").fromList(array); //$NON-NLS-1$
        assertEquals(4, models.size());

        for (final TestModel model : models) {
            assertFalse("should not have model key", model.object.has("test")); //$NON-NLS-1$ //$NON-NLS-2$
            assertNotSame(obj, model.object);
        }
    }

    /**
     * Gets a valid JSON object.
     *
     * @return {@link JSONObject}
     * @throws JSONException if something fails...
     */
    @NonNull
    private JSONObject getValidJSONObject() throws JSONException {
        final JSONObject obj = new JSONObject();
        obj.put("var1", "var1-value"); //$NON-NLS-1$ //$NON-NLS-2$
        obj.put("var2", "var2-value"); //$NON-NLS-1$ //$NON-NLS-2$
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
     * Test implementation of {@link AbstractModelFactory}.
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

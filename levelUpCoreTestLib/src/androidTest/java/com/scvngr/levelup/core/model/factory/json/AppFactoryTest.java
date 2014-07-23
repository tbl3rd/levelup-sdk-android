package com.scvngr.levelup.core.model.factory.json;

import android.test.suitebuilder.annotation.SmallTest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.scvngr.levelup.core.model.App;
import com.scvngr.levelup.core.model.AppFixture;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;

/**
 * Tests {@link com.scvngr.levelup.core.model.factory.json.AppJsonFactory}.
 */
public final class AppFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests parsing a minimal model.
     *
     * @throws org.json.JSONException upon JSON errors
     */
    @SmallTest
    public void testJsonParse_minimal() throws JSONException {
        final AppJsonFactory factory = new AppJsonFactory();
        final App minimal = factory.createFrom(AppFixture.getMinimalJsonObject());

        assertNotNull(minimal);

        assertEquals(AppFixture.APP_DESCRIPTION_FIXTURE_1, minimal.getDescription());
        assertEquals(AppFixture.APP_NAME_FIXTURE_1, minimal.getName());
        assertEquals(AppFixture.APP_ID_FIXTURE_1, minimal.getId());
        assertNull(minimal.getImageUrl());

        assertEquals(AppFixture.getMinimalModel(), minimal);
    }

    /**
     * Tests parsing a full model.
     *
     * @throws org.json.JSONException upon JSON errors
     */
    @SmallTest
    public void testJsonParse_full() throws JSONException {
        final AppJsonFactory factory = new AppJsonFactory();
        final App full = factory.createFrom(AppFixture.getFullJsonObject());

        assertNotNull(full);

        assertEquals(AppFixture.APP_DESCRIPTION_FIXTURE_1, full.getDescription());
        assertEquals(AppFixture.APP_NAME_FIXTURE_1, full.getName());
        assertEquals(AppFixture.APP_ID_FIXTURE_1, full.getId());
        assertEquals(AppFixture.APP_IMAGE_URL_1, full.getImageUrl());

        assertEquals(AppFixture.getFullModel(), full);
    }

    /**
     * Tests parsing with a missing required field.
     */
    @SmallTest
    public void testJsonParseFail_missing() {
        try {
            final AppJsonFactory factory = new AppJsonFactory();
            final JsonObject invalidApp = AppFixture.getMinimalJsonObject();
            invalidApp.getAsJsonObject(AppJsonFactory.MODEL_ROOT).remove("name");
            factory.createFrom(invalidApp);
            fail("Exception should have been thrown");
        } catch (final JsonParseException e) {
            // Expected exception
        }
    }
}

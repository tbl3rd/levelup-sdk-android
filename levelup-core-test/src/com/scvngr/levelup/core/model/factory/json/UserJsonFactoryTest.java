package com.scvngr.levelup.core.model.factory.json;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.User;
import com.scvngr.levelup.core.model.UserFixture;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link UserJsonFactory}.
 */
public final class UserJsonFactoryTest extends AndroidTestCase {

    /**
     * Tests full User model json parsing.
     *
     * @throws JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_full() throws JSONException {
        final UserJsonFactory factory = new UserJsonFactory();
        final JSONObject object = UserFixture.getFullJsonObject();
        final User user = factory.from(object);
        assertEquals(UserFixture.getFullModel(), user);
    }

    /**
     * Tests minimal User model json parsing.
     *
     * @throws JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_minimal() throws JSONException {
        final UserJsonFactory factory = new UserJsonFactory();
        final JSONObject object = UserFixture.getMinimalJsonObject();
        final User user = factory.from(object);
        assertEquals(UserFixture.getMinimalModel(), user);
    }

    /**
     * Tests basic json parsing with required key missing.
     *
     * @throws JSONException for parsing errors.
     */
    @SmallTest
    public void testJsonParse_missingRequiredKey() throws JSONException {
        try {
            final JSONObject object = UserFixture.getMinimalJsonObject();
            object.getJSONObject(UserJsonFactory.JsonKeys.MODEL_ROOT).remove(
                    UserJsonFactory.JsonKeys.ID);
            new UserJsonFactory().from(object);
            fail("no ID field should throw error"); //$NON-NLS-1$
        } catch (final JSONException e) {
            // Expected exception
        }
    }
}

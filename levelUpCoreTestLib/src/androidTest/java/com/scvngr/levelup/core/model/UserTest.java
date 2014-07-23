/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.UserJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Tests {@link com.scvngr.levelup.core.model.User}.
 */
public final class UserTest extends SupportAndroidTestCase {

    @SmallTest
    public void testParcel_full() throws JSONException {
        {
            final JSONObject object = UserFixture.getFullJsonObject();

            final User user = new UserJsonFactory().from(object);
            final Parcel parcel = Parcel.obtain();
            user.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            final User parceled = User.CREATOR.createFromParcel(parcel);
            assertEquals(user, parceled);
            assertTrue(user.getCustomAttributes().containsKey("test_attr"));
            assertTrue(user.getCustomAttributes().containsKey("test_attr2"));
            assertEquals("0", user.getCustomAttributes().get("test_attr"));
            assertEquals("1", user.getCustomAttributes().get("test_attr2"));
        }

        {
            final JSONObject object = UserFixture.getMinimalJsonObject();

            final User user = new UserJsonFactory().from(object);
            final Parcel parcel = Parcel.obtain();
            user.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            final User parceled = User.CREATOR.createFromParcel(parcel);
            assertEquals(user, parceled);
        }

        {
            final JSONObject object = UserFixture.getFullJsonObject();

            object.remove(UserJsonFactory.JsonKeys.CUSTOM_ATTRIBUTES);
            final User user = new UserJsonFactory().from(object);
            final Parcel parcel = Parcel.obtain();
            user.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            final User parceled = User.CREATOR.createFromParcel(parcel);
            assertEquals(user, parceled);
            assertNull(parceled.getCustomAttributes());
        }
    }

    @SmallTest
    public void testParcel_valid() throws JSONException {
        final JSONObject object = UserFixture.getMinimalJsonObject();

        object.remove(UserJsonFactory.JsonKeys.CUSTOM_ATTRIBUTES);
        final User user = new UserJsonFactory().from(object);
        final Parcel parcel = Parcel.obtain();
        try {
            user.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            final User parceled = User.CREATOR.createFromParcel(parcel);
            assertEquals(user, parceled);
        } finally {
            parcel.recycle();
        }
    }

    /**
     * Tests {@link #equals(Object)} and {@link #hashCode()} methods.
     *
     * @throws org.json.JSONException for parsing errors.
     */
    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test identical objects
        final User user1 = new UserJsonFactory().from(UserFixture.getFullJsonObject());
        User user2 = new UserJsonFactory().from(UserFixture.getFullJsonObject());
        MoreAsserts.checkEqualsAndHashCodeMethods(user1, user2, true);

        /*
         * Test standard variations on all JsonKeys values (and check for any keys we may have added
         * that didn't make their way into equals/hashcode and the tests explicitly).
         *
         * CUSTOM_ATTRIBUTES needs to be tested by hand since it is deserialized into a dictionary.
         * MODEL_ROOT is only used for automatically un-nesting and isn't used for an attribute.
         */
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(UserJsonFactory.JsonKeys.class,
                new UserJsonFactory(), UserFixture.getFullJsonObject(), new String[] {
                        "CUSTOM_ATTRIBUTES", "MODEL_ROOT" });

        // Modifying a single custom attribute should be unequal/give different hashcode
        final HashMap<String, String> customAttributesMap = new HashMap<String, String>();
        // Changed value
        customAttributesMap.put("test_attr", "1");
        customAttributesMap.put("test_attr2", "1");
        final JSONObject customAttributesObject = new JSONObject(customAttributesMap);
        user2 =
                new UserJsonFactory().from(UserFixture.getFullJsonObject().put(
                        UserJsonFactory.JsonKeys.CUSTOM_ATTRIBUTES, customAttributesObject));
        MoreAsserts.checkEqualsAndHashCodeMethods(user1, user2, false);
    }

    @SmallTest
    public void testEqualsAndHashCode_no_total_savings() throws JSONException {
        final User userModelWithNoTotalSavings =
                new UserJsonFactory().from(UserFixture.getMinimalJsonObject());

        final JSONObject userJsonWithTotalSavings = UserFixture.getMinimalJsonObject();
        userJsonWithTotalSavings.put(UserJsonFactory.JsonKeys.TOTAL_SAVINGS_AMOUNT, 2000);
        final User userModelWithTotalSavings =
                new UserJsonFactory().from(userJsonWithTotalSavings);

        MoreAsserts.checkEqualsAndHashCodeMethods(userModelWithNoTotalSavings,
                userModelWithNoTotalSavings, true);
        MoreAsserts.checkEqualsAndHashCodeMethods(userModelWithTotalSavings,
                userModelWithTotalSavings, true);

        MoreAsserts.checkEqualsAndHashCodeMethods(userModelWithNoTotalSavings,
                userModelWithTotalSavings, false);
    }

    /**
     * Tests that {@link com.scvngr.levelup.core.model.User#toString()} gives a value.
     *
     * @throws org.json.JSONException if there was a problem running toString.
     */
    @SmallTest
    public void testToString() throws JSONException {
        final User user = new UserJsonFactory().from(UserFixture.getMinimalJsonObject());
        final String string = user.toString();
        assertTrue(string.length() > 0);
    }
}

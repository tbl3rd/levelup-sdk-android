/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import org.json.JSONException;

/**
 * Tests {@link com.scvngr.levelup.core.model.AccessToken}.
 */
public final class AccessTokenTest extends TestCase {

    @SmallTest
    public void testConstructor_good() {
        final AccessToken model =
                AccessTokenFixture.getFullModel(AccessTokenFixture.ACCESS_TOKEN_FIXTURE_1,
                        AccessTokenFixture.USER_ID_FIXTURE_1);

        assertEquals(AccessTokenFixture.ACCESS_TOKEN_FIXTURE_1, model.getAccessToken());
        assertEquals(AccessTokenFixture.USER_ID_FIXTURE_1, model.getUserId());
    }

    @SmallTest
    public void testConstructor_goodNoUserId() {
        final AccessToken model =
                AccessTokenFixture.getFullModel(AccessTokenFixture.ACCESS_TOKEN_FIXTURE_1, null);

        assertEquals(AccessTokenFixture.ACCESS_TOKEN_FIXTURE_1, model.getAccessToken());
        assertNull(model.getUserId());
    }

    @SmallTest
    public void testConstructor_bad_missing_access_token() {
        try {
            new AccessToken(null, 0L);
            fail();
        } catch (final NullPointerException e) {
            // Expected exception
        }
    }

    @SmallTest
    public void testEqualsAndHashcode() {
        final String accessToken = AccessTokenFixture.ACCESS_TOKEN_FIXTURE_1;
        final long userId = AccessTokenFixture.USER_ID_FIXTURE_1;

        {
            // Same object
            final AccessToken model1 = AccessTokenFixture.getFullModel();
            MoreAsserts.checkEqualsAndHashCodeMethods(model1, model1, true);
        }

        {
            // Different objects, same values
            final AccessToken model1 = AccessTokenFixture.getFullModel(accessToken, userId);
            final AccessToken model2 = AccessTokenFixture.getFullModel(accessToken, userId);
            MoreAsserts.checkEqualsAndHashCodeMethods(model1, model2, true);
        }

        {
            // Different objects, different access tokens
            final AccessToken model1 = AccessTokenFixture.getFullModel(accessToken, userId);
            final AccessToken model2 =
                    AccessTokenFixture.getFullModel(accessToken + "test", userId);
            MoreAsserts.checkEqualsAndHashCodeMethods(model1, model2, false);
        }

        {
            // Different objects, different user ids
            final AccessToken model1 = AccessTokenFixture.getFullModel(accessToken, userId);
            final AccessToken model2 = AccessTokenFixture.getFullModel(accessToken, userId + 1);
            MoreAsserts.checkEqualsAndHashCodeMethods(model1, model2, false);
        }

        {
            // Different objects, different user ids and access tokens
            final AccessToken model1 = AccessTokenFixture.getFullModel(accessToken, userId);
            final AccessToken model2 = AccessTokenFixture.getFullModel(accessToken + "test",
                    userId + 1);
            MoreAsserts.checkEqualsAndHashCodeMethods(model1, model2, false);
        }

        {
            // Null check
            final AccessToken model1 = AccessTokenFixture.getFullModel(accessToken, userId);
            MoreAsserts.checkEqualsAndHashCodeMethods(model1, null, false);
        }
    }

    @SmallTest
    public void testToString() throws JSONException {
        final String accessToken = AccessTokenFixture.ACCESS_TOKEN_FIXTURE_1;
        final long userId = AccessTokenFixture.USER_ID_FIXTURE_1;

        final AccessToken model = AccessTokenFixture.getFullModel(accessToken, userId);

        assertTrue(model.toString().contains(accessToken));
        assertTrue(model.toString().contains(Long.toString(userId)));
    }

    @SmallTest
    public void testParcelable() throws JSONException {
        final AccessToken model = AccessTokenFixture.getFullModel();

        // Parcel this model and unparcel it into a new model
        final Parcel parcel = Parcel.obtain();
        try {
            model.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            final AccessToken parceledModel = AccessToken.CREATOR.createFromParcel(parcel);

            assertEquals(model, parceledModel);
            assertNotSame(model, parceledModel);
        } finally {
            parcel.recycle();
        }
    }
}

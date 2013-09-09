package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import org.json.JSONException;

/**
 * Tests {@link AccessToken}.
 */
public final class AccessTokenTest extends TestCase {

    @SmallTest
    public void testConstructor_good() {
        final String accessToken = AccessTokenFixture.getRandomAccessToken();
        final long userId = AccessTokenFixture.getRandomUserId();
        final AccessToken model = AccessTokenFixture.getFullModel(accessToken, userId);

        assertEquals(accessToken, model.getAccessToken());
        assertEquals(userId, model.getUserId());
    }

    @SmallTest
    public void testConstructor_bad_missing_access_token() {
        try {
            new AccessToken(null, 0);
            fail();
        } catch (final NullPointerException e) {
            // Expected exception
        }
    }

    @SmallTest
    public void testEqualsAndHashcode() {
        final String accessToken = AccessTokenFixture.getRandomAccessToken();
        final long userId = AccessTokenFixture.getRandomUserId();

        {
            // Same object
            final AccessToken model1 = AccessTokenFixture.getFullModel();
            MoreAsserts.checkEqualsAndHashCodeMethods(model1, model1, true);
        }

        {
            // Different objects, same values
            final AccessToken model1 =
                    AccessTokenFixture.getFullModel(accessToken, userId);
            final AccessToken model2 =
                    AccessTokenFixture.getFullModel(accessToken, userId);
            MoreAsserts.checkEqualsAndHashCodeMethods(model1, model2, true);
        }

        {
            // Different objects, different access tokens
            final AccessToken model1 =
                    AccessTokenFixture.getFullModel(accessToken, userId);
            final AccessToken model2 =
                    AccessTokenFixture.getFullModel(accessToken + "test", userId); //$NON-NLS-1$
            MoreAsserts.checkEqualsAndHashCodeMethods(model1, model2, false);
        }

        {
            // Different objects, different user ids
            final AccessToken model1 =
                    AccessTokenFixture.getFullModel(accessToken, userId);
            final AccessToken model2 =
                    AccessTokenFixture.getFullModel(accessToken, userId + 1);
            MoreAsserts.checkEqualsAndHashCodeMethods(model1, model2, false);
        }

        {
            // Different objects, different user ids and access tokens
            final AccessToken model1 =
                    AccessTokenFixture.getFullModel(accessToken, userId);
            final AccessToken model2 =
                    AccessTokenFixture.getFullModel(accessToken + "test", //$NON-NLS-1$
                            userId + 1);
            MoreAsserts.checkEqualsAndHashCodeMethods(model1, model2, false);
        }

        {
            // Null check
            final AccessToken model1 =
                    AccessTokenFixture.getFullModel(accessToken, userId);
            MoreAsserts.checkEqualsAndHashCodeMethods(model1, null, false);
        }
    }

    @SmallTest
    public void testToString() throws JSONException {
        final String accessToken = AccessTokenFixture.getRandomAccessToken();
        final long userId = AccessTokenFixture.getRandomUserId();

        final AccessToken model = AccessTokenFixture.getFullModel(accessToken, userId);

        assertTrue(model.toString().contains(accessToken));
        assertTrue(model.toString().contains(Long.toString(userId)));
    }

    @SmallTest
    public void testParcelable() throws JSONException {
        final String accessToken = AccessTokenFixture.getRandomAccessToken();
        final long userId = AccessTokenFixture.getRandomUserId();

        final AccessToken model = AccessTokenFixture.getFullModel(accessToken, userId);

        // Parcel this model and unparcel it into a new model
        final Parcel parcel = Parcel.obtain();
        model.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        final AccessToken parceledModel = AccessToken.CREATOR.createFromParcel(parcel);

        assertEquals(model, parceledModel);
        assertNotSame(model, parceledModel);
    }
}

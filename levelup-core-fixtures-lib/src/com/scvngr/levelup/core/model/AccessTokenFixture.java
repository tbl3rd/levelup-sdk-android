package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.factory.json.AccessTokenJsonFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.UUID;

/**
 * Fixture for {@link AccessToken}.
 */
public final class AccessTokenFixture {
    /**
     * @return A full {@link AccessToken} model.
     */
    @NonNull
    public static AccessToken getFullModel() {
        return new AccessToken(getRandomAccessToken(), getRandomUserId());
    }

    /**
     * @param accessToken access token string.
     * @param userId web service user ID.
     * @return A full {@link AccessToken} model.
     */
    @NonNull
    public static AccessToken getFullModel(@NonNull final String accessToken, final long userId) {
        return new AccessToken(accessToken, userId);
    }

    /**
     * @return a full JSON representation of an {@link AccessToken}, as would typically be returned
     *         from the web service.
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        return getFullJsonObject(getRandomAccessToken(), getRandomUserId());
    }

    /**
     * @param accessToken access token string.
     * @param userId web service user ID.
     * @return a full JSON representation of an {@link AccessToken}, as would typically be returned
     *         from the web service.
     */
    @NonNull
    public static JSONObject
            getFullJsonObject(@NonNull final String accessToken, final long userId) {
        try {
            final JSONObject object = new JSONObject();
            object.put(AccessTokenJsonFactory.JsonKeys.TOKEN, accessToken);
            object.put(AccessTokenJsonFactory.JsonKeys.USER_ID, userId);
            return object;
        } catch (final JSONException e) {
            throw new AssertionError(e);
        }
   }

    /**
     * @return A random web service user ID.
     */
    public static long getRandomUserId() {
        return new Random().nextInt();
    }

    /**
     * @return A random access token string.
     */
    @NonNull
    public static String getRandomAccessToken() {
        return UUID.randomUUID().toString();
    }
}

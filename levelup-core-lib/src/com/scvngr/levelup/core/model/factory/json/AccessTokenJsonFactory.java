package com.scvngr.levelup.core.model.factory.json;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.AccessToken;

/**
 * Factory for creating {@link AccessToken}s from JSON.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class AccessTokenJsonFactory extends AbstractJsonModelFactory<AccessToken> {

    /**
     * Constructor.
     */
    public AccessTokenJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @NonNull
    @Override
    protected AccessToken createFrom(@NonNull final JSONObject json) throws JSONException {
        final String accessToken = json.getString(JsonKeys.TOKEN);
        final long userId = json.getLong(JsonKeys.USER_ID);

        return new AccessToken(accessToken, userId);
    }

    /**
     * All of the keys in the JSON representation of this model.
     */
    @Immutable
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    public static final class JsonKeys {

        /**
         * The key under which this model can be nested.
         */
        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String MODEL_ROOT = "access_token"; //$NON-NLS-1$

        /**
         * Key in JSON where the token value can be parsed.
         */
        @JsonValueType(JsonType.STRING)
        public static final String TOKEN = "token"; //$NON-NLS-1$

        /**
         * Key in JSON where the user ID for this token can be parsed.
         */
        @JsonValueType(JsonType.LONG)
        public static final String USER_ID = "user_id"; //$NON-NLS-1$

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private JsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
        }
    }
}

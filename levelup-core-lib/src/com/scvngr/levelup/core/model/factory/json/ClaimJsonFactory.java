package com.scvngr.levelup.core.model.factory.json;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.Claim;
import com.scvngr.levelup.core.model.MonetaryValue;
import com.scvngr.levelup.core.model.util.JsonUtils;

/**
 * Factory for parsing {@link Claim}s from JSON.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class ClaimJsonFactory extends AbstractJsonModelFactory<Claim> {

    /**
     * Constructor...
     */
    public ClaimJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @NonNull
    @Override
    protected Claim createFrom(@NonNull final JSONObject json) throws JSONException {
        final long campaignId = json.getLong(JsonKeys.CAMPAIGN_ID);
        final String code = JsonUtils.optString(json, JsonKeys.CODE);
        final long id = json.getLong(JsonKeys.ID);
        final MonetaryValue value = new MonetaryValue(json.getLong(JsonKeys.VALUE));
        final MonetaryValue valueRemaining =
                new MonetaryValue(json.getLong(JsonKeys.VALUE_REMAINING));

        return new Claim(campaignId, code, id, value, valueRemaining);
    }

    /**
     * All of the keys in the JSON representation of this model.
     */
    @Immutable
    @SuppressWarnings("javadoc")
    public static final class JsonKeys {

        /**
         * The key under which this model can be nested.
         */
        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String MODEL_ROOT = "claim"; //$NON-NLS-1$

        @JsonValueType(JsonType.LONG)
        public static final String CAMPAIGN_ID = "campaign_id"; //$NON-NLS-1$

        @JsonValueType(JsonType.STRING)
        public static final String CODE = "code"; //$NON-NLS-1$

        @JsonValueType(JsonType.LONG)
        public static final String ID = "id"; //$NON-NLS-1$

        @JsonValueType(JsonType.LONG)
        public static final String VALUE = "value_amount"; //$NON-NLS-1$

        @JsonValueType(JsonType.LONG)
        public static final String VALUE_REMAINING = "value_remaining_amount"; //$NON-NLS-1$

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

package com.scvngr.levelup.core.model.factory.json;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.JsonValueType;
import com.scvngr.levelup.core.annotation.JsonValueType.JsonType;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.WebLink;
import com.scvngr.levelup.core.model.WebLink.WebLinkBuilder;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Factory for creating {@link WebLink}s from JSON.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class WebLinkJsonFactory extends AbstractJsonModelFactory<WebLink> {

    /**
     * Constructs a factory for parsing WebLink models.
     */
    public WebLinkJsonFactory() {
        super(JsonKeys.MODEL_ROOT);
    }

    @NonNull
    @Override
    protected WebLink createFrom(@NonNull final JSONObject json) throws JSONException {
        final JsonModelHelper mh = new JsonModelHelper(json);
        final WebLinkBuilder builder = WebLink.builder();

        builder.title(mh.optString(JsonKeys.TITLE));
        builder.webLinkTypeId(mh.getLong(JsonKeys.WEB_LINK_TYPE_ID));
        builder.webUrl(mh.optString(JsonKeys.WEB_URL));

        return builder.build();
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
        @NonNull
        @JsonValueType(JsonType.JSON_OBJECT)
        public static final String MODEL_ROOT = "web_link";

        /**
         * Required string field describing the display value.
         *
         * @see WebLink#getTitle()
         */
        @JsonValueType(JsonType.STRING)
        public static final String TITLE = "title";

        /**
         * Required Long field value describing the type of link.
         *
         * @see WebLink#getWebLinkTypeId()
         */
        @JsonValueType(JsonType.INT)
        public static final String WEB_LINK_TYPE_ID = "web_link_type_id";

        /**
         * Required string field value describing the web URL.
         *
         * @see WebLink#getWebUrl()
         */
        @JsonValueType(JsonType.STRING)
        public static final String WEB_URL = "web_url";

        /**
         * Private constructor prevents instantiation.
         *
         * @throws UnsupportedOperationException because this class cannot be instantiated.
         */
        private JsonKeys() {
            throw new UnsupportedOperationException("This class is non-instantiable");
        }
    }
}

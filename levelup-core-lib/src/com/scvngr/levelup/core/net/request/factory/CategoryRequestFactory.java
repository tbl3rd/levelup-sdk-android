package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;

/**
 * Request a list of {@link com.scvngr.levelup.core.model.Category}s from the server.
 */
@LevelUpApi(contract = Contract.INTERNAL)
public final class CategoryRequestFactory extends AbstractRequestFactory {

    /**
     * The API endpoint for categories.
     */
    public static final String ENDPOINT = "categories"; //$NON-NLS-1$

    /**
     * @param context the Application context.
     */
    public CategoryRequestFactory(@NonNull final Context context) {
        super(context, null);
    }

    /**
     * @return a request factory to get the categories.
     */
    @NonNull
    public AbstractRequest getCategories() {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14, ENDPOINT, null, (JSONObject) null);
    }
}

package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.Category;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpV13Request;

/**
 * Request a list of {@link Category}s from the server. Currently this uses API v13.
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
    public AbstractRequest getCategories() {
        return new LevelUpV13Request(getContext(), HttpMethod.GET,
                LevelUpV13Request.API_VERSION_CODE_V13, ENDPOINT, null, null);
    }
}

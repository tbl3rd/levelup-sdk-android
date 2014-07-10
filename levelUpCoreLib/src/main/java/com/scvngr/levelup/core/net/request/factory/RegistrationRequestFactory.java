package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.RequestUtils;
import com.scvngr.levelup.core.util.PreconditionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Request factory for fetching a {@link com.scvngr.levelup.core.model.Registration} from the web
 * service.
 */
@LevelUpApi(contract = LevelUpApi.Contract.ENTERPRISE)
public final class RegistrationRequestFactory extends AbstractRequestFactory {

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @NonNull
    /* package */static final String ENDPOINT_REGISTRATION = "registration"; //$NON-NLS-1$

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    @NonNull
    /* package */static final String PARAM_EMAIL = "email"; //$NON-NLS-1$

    /**
     * @param context the Application context.
     */
    public RegistrationRequestFactory(@NonNull final Context context) {
        super(context, null);
    }

    /**
     * Build a request to get the {@link com.scvngr.levelup.core.model.Registration} for the email
     * passed.
     *
     * @param email the user's email address.
     * @return an {@link AbstractRequest} to use to get the
     *         {@link com.scvngr.levelup.core.model.Registration} for the email passed.
     */
    @NonNull
    @LevelUpApi(contract = LevelUpApi.Contract.ENTERPRISE)
    public AbstractRequest buildRegistrationRequest(@NonNull final String email) {
        PreconditionUtil.assertNotNull(email, "email"); //$NON-NLS-1$

        final Map<String, String> params = new HashMap<String, String>();
        RequestUtils.addApiKeyToRequestQueryParams(getContext(), params);
        params.put(PARAM_EMAIL, email);

        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V15, ENDPOINT_REGISTRATION, params, null);
    }
}

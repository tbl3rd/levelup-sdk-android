package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.AccessToken;
import com.scvngr.levelup.core.model.CauseAffiliation;
import com.scvngr.levelup.core.model.User;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpV13Request;

/**
 * Builds {@link AbstractRequest}s to interact with {@link CauseAffiliation}.
 */
@Immutable
@LevelUpApi(contract = Contract.INTERNAL)
public final class CauseAffiliationRequestFactory extends AbstractRequestFactory {

    /**
     * Cause affiliation ID;
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_CAUSE_ID = "cause_id"; //$NON-NLS-1$

    /**
     * Cause affiliation percent donation value.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_PERCENT_DONATION = "percent_donation"; //$NON-NLS-1$

    /**
     * Constructor.
     *
     * @param context the Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link User}'s {@link AccessToken}.
     */
    public CauseAffiliationRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * Build a {@link AbstractRequest} to retrieve a {@link CauseAffiliation} from the web service.
     *
     * @return a AbstractRequest for retrieving the {@link CauseAffiliation} from the web service.
     */
    @NonNull
    public final AbstractRequest buildGetCauseAffiliation() {
        return new LevelUpV13Request(getContext(), HttpMethod.GET,
                "cause_affiliation", null, null, getAccessTokenRetriever()); //$NON-NLS-1$
    }

    /**
     * Builds a request to update the {@link CauseAffiliation}.
     *
     * @param causeId the ID of the cause on the web service to set.
     * @param percentDonation the decimal representation of the percent to donate to the cause. (IE:
     *        20% is 0.2).
     * @return {@link AbstractRequest} to use to get the cause affiliation.
     */
    @NonNull
    public final AbstractRequest buildCreateCauseAffiliation(final long causeId,
            final double percentDonation) {
        AbstractRequest request = buildGetCauseAffiliation();
        final Map<String, String> params = new HashMap<String, String>();
        params.put(PARAM_CAUSE_ID, Long.toString(causeId));
        params.put(PARAM_PERCENT_DONATION, Double.toString(percentDonation));

        request =
                new LevelUpV13Request(getContext(), HttpMethod.POST,
                        "cause_affiliation", null, params, getAccessTokenRetriever()); //$NON-NLS-1$
        return request;
    }

    /**
     *
     * @return
     */
    @NonNull
    public final AbstractRequest buildDeleteCauseAffiliation() {
        return new LevelUpV13Request(getContext(), HttpMethod.DELETE, "cause_affiliation", //$NON-NLS-1$
                null, null, getAccessTokenRetriever());
    }
}

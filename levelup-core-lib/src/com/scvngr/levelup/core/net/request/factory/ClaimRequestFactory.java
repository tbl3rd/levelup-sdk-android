package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.model.AccessToken;
import com.scvngr.levelup.core.model.Campaign;
import com.scvngr.levelup.core.model.User;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.util.LogManager;

/**
 * Builds requests to claim {@link Campaign}s.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class ClaimRequestFactory extends AbstractRequestFactory {

    /**
     * Outer parameter key for LegacyLoyalty.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String OUTER_PARAM_LEGACY_LOYALTY = "legacy_loyalty"; //$NON-NLS-1$

    /**
     * Parameter key for legacy loyalty card number.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_LEGACY_ID = "legacy_id"; //$NON-NLS-1$

    /**
     * @param context the Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link User}'s {@link AccessToken}.
     */
    public ClaimRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * Build a request to claim a "Legacy Loyalty" campaign. A Legacy Loyalty campaign transfers a
     * user's loyalty data from their old loyalty system to LevelUp.
     *
     * @param loyaltyCampaignId the web service ID of the legacy loyalty campaign.
     * @param loyaltyId the user's ID from their legacy loyalty program.
     * @return the {@link AbstractRequest} to use to claim the legacy loyalty campaign.
     */
    @NonNull
    public AbstractRequest buildClaimLegacyLoyaltyRequest(final int loyaltyCampaignId,
            @NonNull final String loyaltyId) {
        final JSONObject object = new JSONObject();
        final JSONObject legacyLoyaltyObject = new JSONObject();

        try {
            legacyLoyaltyObject.put(PARAM_LEGACY_ID, loyaltyId);
            object.put(OUTER_PARAM_LEGACY_LOYALTY, legacyLoyaltyObject);
        } catch (final JSONException e) {
            LogManager.e("Error Building legacy Loyalty claim request", e); //$NON-NLS-1$
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V14, String.format(Locale.US,
                        "loyalties/legacy/%d/claims", loyaltyCampaignId), null, object, //$NON-NLS-1$
                getAccessTokenRetriever());
    }

    /**
     * Build a request to claim a generic campaign by its cohort code.
     *
     * @param code the code to claim.
     * @return the {@link AbstractRequest} to use to claim the {@link Campaign}.
     */
    @NonNull
    public AbstractRequest buildClaimCampaignRequest(@NonNull final String code) {
        String codeToClaim = code;

        try {
            codeToClaim =
                    URLEncoder.encode(code, "UTF-8"); //$NON-NLS-1$
        } catch (final UnsupportedEncodingException e) {
            LogManager.e("Unsupported encoding when encoding code to claim", e); //$NON-NLS-1$
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V14, String.format(Locale.US,
                        "codes/%s/claims", codeToClaim), null, (JSONObject) null, //$NON-NLS-1$
                getAccessTokenRetriever());
    }
}

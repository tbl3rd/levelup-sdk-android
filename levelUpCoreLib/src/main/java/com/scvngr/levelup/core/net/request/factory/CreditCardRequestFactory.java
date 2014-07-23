/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.AccessTokenRequired;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.RequiresPermission;
import com.scvngr.levelup.core.model.CreditCard;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.JSONObjectRequestBody;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.Permissions;
import com.scvngr.levelup.core.util.LogManager;
import com.scvngr.levelup.core.util.NullUtils;
import com.scvngr.levelup.core.util.PreconditionUtil;

import com.braintreegateway.encryption.Braintree;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * AbstractRequest builder for requests to the Credit Cards endpoint.
 */
@Immutable
@LevelUpApi(contract = Contract.PUBLIC)
public final class CreditCardRequestFactory extends AbstractRequestFactory {
    @NonNull
    private static final String CREDIT_CARDS_ENDPOINT = "credit_cards";

    /**
     * The outer JSON object request parameter.
     */
    @NonNull
    public static final String OUTER_PARAM_CARD = "credit_card";

    /**
     * The encrypted CVV code.
     */
    @NonNull
    public static final String PARAM_ENCRYPTED_CVV = "encrypted_cvv";

    /**
     * The encrypted expiration date month.
     */
    @NonNull
    public static final String PARAM_ENCRYPTED_EXPIRATION_MONTH = "encrypted_expiration_month";

    /**
     * The encrypted expiration date year.
     */
    @NonNull
    public static final String PARAM_ENCRYPTED_EXPIRATION_YEAR = "encrypted_expiration_year";

    /**
     * The encrypted credit card number.
     */
    @NonNull
    public static final String PARAM_ENCRYPTED_NUMBER = "encrypted_number";

    /**
     * The postal/zip code.
     */
    @NonNull
    public static final String PARAM_POSTAL_CODE = "postal_code";

    /**
     * @param context the Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link com.scvngr.levelup.core.model.User}'s
     *        {@link com.scvngr.levelup.core.model.AccessToken}.
     */
    public CreditCardRequestFactory(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever) {
        super(context, retriever);
    }

    /**
     * Builds a request to the server which will add a credit card for the current user.
     *
     * @param cardNumber the card number for the card.
     * @param cvv the CVV (security code) of the card.
     * @param expirationMonth the month the card expires.
     * @param expirationYear the year the card expires.
     * @param postalCode the billing postal code of the card.
     * @return {@link AbstractRequest} to create a card on the server.
     */
    @NonNull
    @LevelUpApi(contract = Contract.PUBLIC)
    @RequiresPermission(Permissions.PERMISSION_CREATE_FIRST_CREDIT_CARD)
    @AccessTokenRequired
    public AbstractRequest buildCreateCardRequest(@NonNull final String cardNumber,
            @NonNull final String cvv, @NonNull final String expirationMonth,
            @NonNull final String expirationYear, @NonNull final String postalCode) {

        PreconditionUtil.assertNotNull(cardNumber, "cardNumber");
        PreconditionUtil.assertNotNull(cvv, "cvv");
        PreconditionUtil.assertNotNull(expirationMonth, "expirationMonth");
        PreconditionUtil.assertNotNull(expirationYear, "expirationYear");
        PreconditionUtil.assertNotNull(postalCode, "postalCode");

        final Braintree braintree =
                new Braintree(getContext().getString(
                        R.string.levelup_braintree_clientside_encryption_key));

        final JSONObject parameters = new JSONObject();
        final JSONObject creditCard = new JSONObject();

        try {
            creditCard.put(PARAM_ENCRYPTED_CVV, braintree.encrypt(cvv));
            creditCard.put(PARAM_ENCRYPTED_EXPIRATION_MONTH, braintree.encrypt(expirationMonth));
            creditCard.put(PARAM_ENCRYPTED_EXPIRATION_YEAR, braintree.encrypt(expirationYear));
            creditCard.put(PARAM_ENCRYPTED_NUMBER, braintree.encrypt(cardNumber));
            creditCard.put(PARAM_POSTAL_CODE, postalCode);

            parameters.put(OUTER_PARAM_CARD, creditCard);
        } catch (final JSONException e) {
            LogManager.e("Error building JSON.", e);
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V15, CREDIT_CARDS_ENDPOINT, null,
                new JSONObjectRequestBody(parameters), getAccessTokenRetriever());
    }

    /**
     * Builds a request to get the credit cards for a user.
     *
     * @return {@link AbstractRequest} to use to get the credit cards for the current user.
     */
    @NonNull
    @LevelUpApi(contract = Contract.ENTERPRISE)
    @AccessTokenRequired
    public AbstractRequest buildGetCardsRequest() {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14, CREDIT_CARDS_ENDPOINT, null, null,
                getAccessTokenRetriever());
    }

    /**
     * Builds a request to promote (set as default) a credit card.
     *
     * @param card the {@link CreditCard} to promote.
     * @return {@link AbstractRequest} to use to promote a credit card.
     */
    @NonNull
    @LevelUpApi(contract = Contract.ENTERPRISE)
    @AccessTokenRequired
    public AbstractRequest buildPromoteCardRequest(@NonNull final CreditCard card) {
        return new LevelUpRequest(getContext(), HttpMethod.PUT,
                LevelUpRequest.API_VERSION_CODE_V14, NullUtils.format(
                        "%s/%d", CREDIT_CARDS_ENDPOINT, card.getId()), null, null,
                getAccessTokenRetriever());
    }

    /**
     * Builds a request to delete a credit card.
     *
     * @param card the {@link CreditCard} to delete.
     * @return {@link AbstractRequest} to use to delete a credit card.
     */
    @NonNull
    @LevelUpApi(contract = Contract.ENTERPRISE)
    @AccessTokenRequired
    public AbstractRequest buildDeleteCardRequest(@NonNull final CreditCard card) {
        return new LevelUpRequest(getContext(), HttpMethod.DELETE,
                LevelUpRequest.API_VERSION_CODE_V14, NullUtils.format(
                        "%s/%d", CREDIT_CARDS_ENDPOINT, card.getId()), null, null,
                getAccessTokenRetriever());
    }
}

/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import java.util.Locale;

import net.jcip.annotations.Immutable;

import org.json.JSONException;
import org.json.JSONObject;

import com.braintreegateway.encryption.Braintree;
import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.AccessToken;
import com.scvngr.levelup.core.model.CreditCard;
import com.scvngr.levelup.core.model.User;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.util.LogManager;
import com.scvngr.levelup.core.util.PreconditionUtil;

/**
 * AbstractRequest builder for requests to the Credit Cards endpoint.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class CreditCardRequestFactory extends AbstractRequestFactory {
    private static final String CREDIT_CARDS_ENDPOINT = "credit_cards"; //$NON-NLS-1$

    /**
     * The outer JSON object request parameter.
     */
    public static final String OUTER_PARAM_CARD = "credit_card"; //$NON-NLS-1$

    /**
     * The encrypted CVV code.
     */
    public static final String PARAM_ENCRYPTED_CVV = "encrypted_cvv"; //$NON-NLS-1$

    /**
     * The encrypted expiration date month.
     */
    public static final String PARAM_ENCRYPTED_EXPIRATION_MONTH = "encrypted_expiration_month"; //$NON-NLS-1$

    /**
     * The encrypted expiration date year.
     */
    public static final String PARAM_ENCRYPTED_EXPIRATION_YEAR = "encrypted_expiration_year"; //$NON-NLS-1$

    /**
     * The encrypted credit card number.
     */
    public static final String PARAM_ENCRYPTED_NUMBER = "encrypted_number"; //$NON-NLS-1$

    /**
     * The postal/zip code.
     */
    public static final String PARAM_POSTAL_CODE = "postal_code"; //$NON-NLS-1$

    /**
     * @param context the Application context.
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link User}'s {@link AccessToken}.
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
    public AbstractRequest buildCreateCardRequest(@NonNull final String cardNumber,
            @NonNull final String cvv, @NonNull final String expirationMonth,
            @NonNull final String expirationYear, @NonNull final String postalCode) {

        PreconditionUtil.assertNotNull(cardNumber, "cardNumber"); //$NON-NLS-1$
        PreconditionUtil.assertNotNull(cvv, "cvv"); //$NON-NLS-1$
        PreconditionUtil.assertNotNull(expirationMonth, "expirationMonth"); //$NON-NLS-1$
        PreconditionUtil.assertNotNull(expirationYear, "expirationYear"); //$NON-NLS-1$
        PreconditionUtil.assertNotNull(postalCode, "postalCode"); //$NON-NLS-1$

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
            LogManager.e("Error building JSON.", e); //$NON-NLS-1$
        }

        return new LevelUpRequest(getContext(), HttpMethod.POST,
                LevelUpRequest.API_VERSION_CODE_V14, CREDIT_CARDS_ENDPOINT, null,
                parameters, getAccessTokenRetriever());
    }

    /**
     * Builds a request to get the credit cards for a user.
     *
     * @return {@link AbstractRequest} to use to get the credit cards for the current user.
     */
    @NonNull
    public AbstractRequest buildGetCardsRequest() {
        return new LevelUpRequest(getContext(), HttpMethod.GET,
                LevelUpRequest.API_VERSION_CODE_V14, CREDIT_CARDS_ENDPOINT, null,
                (JSONObject) null, getAccessTokenRetriever());
    }

    /**
     * Builds a request to promote (set as default) a credit card.
     *
     * @param card the {@link CreditCard} to promote.
     * @return {@link AbstractRequest} to use to promote a credit card.
     */
    @NonNull
    public AbstractRequest buildPromoteCardRequest(@NonNull final CreditCard card) {
        return new LevelUpRequest(getContext(), HttpMethod.PUT,
                LevelUpRequest.API_VERSION_CODE_V14, String.format(Locale.US,
                        "%s/%d", CREDIT_CARDS_ENDPOINT, card.getId()), null, (JSONObject) null, //$NON-NLS-1$
                getAccessTokenRetriever());
    }

    /**
     * Builds a request to delete a credit card.
     *
     * @param card the {@link CreditCard} to delete.
     * @return {@link AbstractRequest} to use to delete a credit card.
     */
    @NonNull
    public AbstractRequest buildDeleteCardRequest(@NonNull final CreditCard card) {
        return new LevelUpRequest(getContext(), HttpMethod.DELETE,
                LevelUpRequest.API_VERSION_CODE_V14, String.format(Locale.US,
                        "%s/%d", CREDIT_CARDS_ENDPOINT, card.getId()), null, (JSONObject) null, //$NON-NLS-1$
                getAccessTokenRetriever());
    }
}

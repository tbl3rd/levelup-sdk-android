package com.scvngr.levelup.core.net.error;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;

/**
 * {@link com.scvngr.levelup.core.model.Error} codes that may be returned by the LevelUp web
 * service.
 */
@LevelUpApi(contract = Contract.PUBLIC)
public enum ErrorCode {

    /**
     * Error code that may be received when the user is debit-only.
     */
    @NonNull
    DEBIT_CARD_ONLY("debit_card_only"),

    /**
     * Error code that may be received when the user has an unpaid balance from a declined card.
     */
    @NonNull
    DELINQUENT_BUNDLE("delinquent_bundle"),

    /**
     * Error code that may be received when the user has too many chargebacks from a card.
     */
    @NonNull
    EXCESSIVE_CHARGEBACKS("excessive_chargebacks"),

    /**
     * Error code that may be received when the user has no card on file.
     */
    @NonNull
    NO_CREDIT_CARD("no_credit_card");

    /**
     * The error code returned by the web service.
     */
    @NonNull
    private final String mValue;

    @Override
    public String toString() {
        return mValue;
    }

    /**
     * Finds the enumeration matching the input text.
     *
     * @param text the enumeration text to find.
     * @return the matching enumeration.
     */
    @Nullable
    public static ErrorCode fromString(@NonNull final String text) {
        for (final ErrorCode current : ErrorCode.values()) {
            if (text.equalsIgnoreCase(current.mValue)) {
                return current;
            }
        }

        return null;
    }

    /**
     * @param value the error code returned by the web service.
     */
    private ErrorCode(@NonNull final String value) {
        mValue = value;
    }
}

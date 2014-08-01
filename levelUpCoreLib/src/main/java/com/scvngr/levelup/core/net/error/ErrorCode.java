/*
 * Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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

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
 * {@link com.scvngr.levelup.core.model.Error} objects that may be returned by the web service.
 */
@LevelUpApi(contract = Contract.PUBLIC)
public enum ErrorObject {

    /**
     * Error object that may be received when the user is debit-only.
     */
    @NonNull
    CREDIT_CARD("credit_card"),

    /**
     * Error object that may be received when the user's card has an issue.
     */
    @NonNull
    PAYMENT_TOKEN("payment_token");

    /**
     * The error object returned by the web service.
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
    public static ErrorObject fromString(@NonNull final String text) {
        for (final ErrorObject current : ErrorObject.values()) {
            if (text.equalsIgnoreCase(current.mValue)) {
                return current;
            }
        }

        return null;
    }

    /**
     * @param value the error object returned by the web service.
     */
    private ErrorObject(@NonNull final String value) {
        mValue = value;
    }
}

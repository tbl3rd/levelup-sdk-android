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

package com.scvngr.levelup.core.net;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.net.StringRequestBodyTest.StringRequestBodyImpl;
import com.scvngr.levelup.core.util.NullUtils;

/**
 * Tests {@link com.scvngr.levelup.core.net.StringRequestBody}.
 */
public final class StringRequestBodyTest extends AbstractRequestBodyTest<StringRequestBodyImpl> {

    /**
     * String fixture.
     */
    @NonNull
    public static final String FIXTURE_BODY =
            "A crowd of people, and at the center, a popular misconception.भा𠜎";

    /**
     * Content type fixture.
     */
    @NonNull
    public static final String FIXTURE_CONTENT_TYPE = "text/rfk";

    /**
     * Concrete implementation of {@link com.scvngr.levelup.core.net.StringRequestBody}.
     */
    protected static final class StringRequestBodyImpl extends StringRequestBody {

        /**
         * Parcelable creator.
         */
        public static final Creator<StringRequestBodyImpl> CREATOR =
                new Creator<StringRequestBodyImpl>() {

                    @Override
                    public StringRequestBodyImpl createFromParcel(final Parcel source) {
                        return new StringRequestBodyImpl(NullUtils.nonNullContract(source));
                    }

                    @Override
                    public StringRequestBodyImpl[] newArray(final int size) {
                        return new StringRequestBodyImpl[size];
                    }
                };

        /**
         * @param in source
         */
        public StringRequestBodyImpl(@NonNull final Parcel in) {
            super(in);
        }

        /**
         * @param body contents
         */
        public StringRequestBodyImpl(@NonNull final String body) {
            super(body);
        }

        @Override
        @NonNull
        public String getContentType() {
            return FIXTURE_CONTENT_TYPE;
        }

    }

    @Override
    @NonNull
    protected StringRequestBodyImpl getFixture() {
        return new StringRequestBodyImpl(FIXTURE_BODY);
    }
}

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

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import org.json.JSONArray;

/**
 * A {@link JSONArray} request body.
 */
@Immutable
@ThreadSafe
@LevelUpApi(contract = LevelUpApi.Contract.DRAFT)
public final class JSONArrayRequestBody extends StringRequestBody {

    /**
     * Part of the {@link android.os.Parcelable} contract.
     */
    public static final Creator<JSONArrayRequestBody> CREATOR =
            new Creator<JSONArrayRequestBody>() {

                @Override
                public JSONArrayRequestBody[] newArray(final int size) {
                    return new JSONArrayRequestBody[size];
                }

                @Override
                public JSONArrayRequestBody createFromParcel(final Parcel source) {
                    return new JSONArrayRequestBody(NullUtils.nonNullContract(source));
                }
            };

    /**
     * @param array the {@link JSONArray} to send.
     */
    public JSONArrayRequestBody(@NonNull final JSONArray array) {
        super(NullUtils.nonNullContract(array.toString()));
    }

    /**
     * {@link android.os.Parcelable} constructor.
     *
     * @param parcel the source parcel
     */
    public JSONArrayRequestBody(@NonNull final Parcel parcel) {
        super(parcel);
    }

    @Override
    @NonNull
    public String getContentType() {
        return RequestUtils.HEADER_CONTENT_TYPE_JSON;
    }
}

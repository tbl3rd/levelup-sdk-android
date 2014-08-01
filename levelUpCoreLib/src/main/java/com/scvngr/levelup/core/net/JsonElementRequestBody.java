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

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

/**
 * A request body with a {@link JsonElement}.
 */
@Immutable
@ThreadSafe
@LevelUpApi(contract = LevelUpApi.Contract.DRAFT)
public final class JsonElementRequestBody extends StringRequestBody {

    /**
     * {@link android.os.Parcelable} creator.
     */
    public static final Creator<JsonElementRequestBody> CREATOR =
            new Creator<JsonElementRequestBody>() {

                @Override
                public JsonElementRequestBody[] newArray(final int size) {
                    return new JsonElementRequestBody[size];
                }

                @Override
                public JsonElementRequestBody createFromParcel(final Parcel source) {
                    return new JsonElementRequestBody(NullUtils.nonNullContract(source));
                }
            };

    /**
     * @param jsonElement the element to serialize to the request body.
     */
    public JsonElementRequestBody(@NonNull final JsonElement jsonElement) {
        super(NullUtils.nonNullContract(jsonElement.toString()));
    }

    /**
     * @param gson the Gson serializer.
     * @param jsonElement the element to serialize to the request body.
     */
    public JsonElementRequestBody(@NonNull final Gson gson, @NonNull final JsonElement jsonElement) {
        super(NullUtils.nonNullContract(gson.toJson(jsonElement)));
    }

    /**
     * @param source parcel to restore from.
     */
    public JsonElementRequestBody(@NonNull final Parcel source) {
        super(source);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    @NonNull
    public String getContentType() {
        return RequestUtils.HEADER_CONTENT_TYPE_JSON;
    }
}

/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.os.Parcel;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.net.request.RequestUtils;
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

/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.os.Parcel;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.NonNull;
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

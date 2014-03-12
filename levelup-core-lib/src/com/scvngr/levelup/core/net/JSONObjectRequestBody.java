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

import org.json.JSONObject;

/**
 * A {@link JSONObject} request body.
 */
@Immutable
@ThreadSafe
@LevelUpApi(contract = LevelUpApi.Contract.DRAFT)
public final class JSONObjectRequestBody extends StringRequestBody {

    /**
     * Part of the {@link android.os.Parcelable} contract.
     */
    public static final Creator<JSONObjectRequestBody> CREATOR =
            new Creator<JSONObjectRequestBody>() {

                @Override
                public JSONObjectRequestBody[] newArray(final int size) {
                    return new JSONObjectRequestBody[size];
                }

                @Override
                public JSONObjectRequestBody createFromParcel(final Parcel source) {
                    return new JSONObjectRequestBody(NullUtils.nonNullContract(source));
                }
            };

    /**
     * @param object the {@link JSONObject} to send.
     */
    public JSONObjectRequestBody(@NonNull final JSONObject object) {
        super(NullUtils.nonNullContract(object.toString()));
    }

    /**
     * {@link android.os.Parcelable} constructor.
     *
     * @param parcel the source parcel
     */
    public JSONObjectRequestBody(@NonNull final Parcel parcel) {
        super(parcel);
    }

    @Override
    @NonNull
    public String getContentType() {
        return RequestUtils.HEADER_CONTENT_TYPE_JSON;
    }
}

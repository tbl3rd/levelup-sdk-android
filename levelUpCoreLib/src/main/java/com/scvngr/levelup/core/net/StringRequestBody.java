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

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 * A simple {@link RequestBody} that stores the body as a {@link String}.
 * </p>
 * <p>
 * As this implements the {@link android.os.Parcelable} interface, subclasses must fully implement
 * the {@link android.os.Parcelable} interface. This includes having both constructors that this
 * class defines, as well as the {@code public static final} {@link android.os.Parcelable.Creator}
 * {@code CREATOR}.
 * </p>
 */
@Immutable
@ThreadSafe
@LevelUpApi(contract = LevelUpApi.Contract.DRAFT)
public abstract class StringRequestBody implements RequestBody {

    @NonNull
    private final String mBody;

    /**
     * @param body the request body
     */
    public StringRequestBody(@NonNull final String body) {
        mBody = body;
    }

    /**
     * @param source the source parcel
     */
    public StringRequestBody(@NonNull final Parcel source) {
        mBody = NullUtils.nonNullContract(source.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mBody);
    }

    @Override
    public void writeToOutputStream(@NonNull final Context context,
            @NonNull final OutputStream outputStream) throws IOException {
        final OutputStreamWriter writer = new OutputStreamWriter(outputStream, "utf-8");
        try {
            writer.write(mBody);
        } finally {
            writer.close();
        }
    }

    @Override
    public int getContentLength() {
        try {
            return mBody.getBytes("UTF-8").length;
        } catch (final UnsupportedEncodingException e) {
         // This is pretty much impossible.
            throw new RuntimeException("The unthinkable happened: there is no UTF-8", e);
        }
    }

    @SuppressWarnings("null") // Generated code.
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mBody == null) ? 0 : mBody.hashCode());
        return result;
    }

    @SuppressWarnings({ "null", "unused" }) // Generated code.
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StringRequestBody other = (StringRequestBody) obj;
        if (mBody == null) {
            if (other.mBody != null) {
                return false;
            }
        } else if (!mBody.equals(other.mBody)) {
            return false;
        }
        return true;
    }

    @Override
    @NonNull
    public String toString() {
        return mBody;
    }
}

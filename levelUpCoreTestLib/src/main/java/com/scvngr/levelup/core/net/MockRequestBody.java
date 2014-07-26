/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * A {@link RequestBody} fixture of content type {@link #FIXTURE_CONTENT_TYPE} with content
 * {@link #BODY_FIXTURE}.
 */
public final class MockRequestBody implements RequestBody {

    /**
     * Request body fixture.
     */
    @NonNull
    public static final String BODY_FIXTURE = "{ \"test\": {\"snowman\": \"☃\"}}";

    /**
     * Parcelable creator.
     */
    public static final Creator<MockRequestBody> CREATOR = new Creator<MockRequestBody>() {

        @Override
        public MockRequestBody createFromParcel(final Parcel source) {
            return new MockRequestBody();
        }

        @Override
        public MockRequestBody[] newArray(final int size) {
            return new MockRequestBody[size];
        }
    };

    /**
     * Request content type fixture.
     */
    @NonNull
    public static final String FIXTURE_CONTENT_TYPE = "text/awesome";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(final Object o) {
        // This is a fixture, so all are equal.
        return o instanceof MockRequestBody;
    }

    @Override
    public int getContentLength() {
        return BODY_FIXTURE.length();
    }

    @Override
    @NonNull
    public String getContentType() {
        return FIXTURE_CONTENT_TYPE;
    }

    @Override
    public int hashCode() {
        // This is a fixture, so this value is as good as any.
        return 59;
    }

    @Override
    @NonNull
    public String toString() {
        return BODY_FIXTURE;
    }

    @Override
    public void writeToOutputStream(@NonNull final Context context,
            @NonNull final OutputStream outputStream) throws IOException {
        final OutputStreamWriter w = new OutputStreamWriter(outputStream);
        try {
            w.write(BODY_FIXTURE);
        } finally {
            w.close();
        }
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        // This method intentionally left blank.
    }
}

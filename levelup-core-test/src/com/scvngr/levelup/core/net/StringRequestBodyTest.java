package com.scvngr.levelup.core.net;

import android.os.Parcel;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.net.StringRequestBodyTest.StringRequestBodyImpl;
import com.scvngr.levelup.core.util.NullUtils;

/**
 * Tests {@link StringRequestBody}.
 */
public final class StringRequestBodyTest extends AbstractRequestBodyTest<StringRequestBodyImpl> {

    /**
     * String fixture.
     */
    @NonNull
    public static final String FIXTURE_BODY =
            "A crowd of people, and at the center, a popular misconception."; //$NON-NLS-1$

    /**
     * Content type fixture.
     */
    @NonNull
    public static final String FIXTURE_CONTENT_TYPE = "text/rfk"; //$NON-NLS-1$

    /**
     * Concrete implementation of {@link StringRequestBody}.
     */
    public static final class StringRequestBodyImpl extends StringRequestBody {

        /**
         * Parcelable creator.
         */
        public static final Creator<StringRequestBodyImpl> CREATOR =
                new Creator<StringRequestBodyTest.StringRequestBodyImpl>() {

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

    @Override
    @NonNull
    protected String getFixtureAsString() {
        return FIXTURE_BODY;
    }
}

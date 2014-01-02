package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.ErrorV13JsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;

/**
 * Tests {@link ErrorV13}.
 */
public final class ErrorV13Test extends SupportAndroidTestCase {

    /**
     * Tests the constructor parameter validation.
     */
    @SmallTest
    public void testConstructor() {
        new ErrorV13("error", "error_description", "sponsor_email"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        new ErrorV13("error", null, "sponsor_email"); //$NON-NLS-1$ //$NON-NLS-2$
        new ErrorV13("error", "error_description", null); //$NON-NLS-1$ //$NON-NLS-2$

        try {
            new ErrorV13(null, "error_description", "sponsor_email"); //$NON-NLS-1$ //$NON-NLS-2$
            fail("null error should throw exception"); //$NON-NLS-1$
        } catch (final AssertionError e) {
            // Expected exception
        }
    }

    /**
     * Tests basic parceling.
     */
    @SmallTest
    public void testParcelable_basic() {
        final ErrorV13 error = new ErrorV13("error", "error_description", null); //$NON-NLS-1$ //$NON-NLS-2$
        final Parcel parcel = Parcel.obtain();
        error.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        final ErrorV13 newError = ErrorV13.CREATOR.createFromParcel(parcel);

        assertEquals(error, newError);
        assertEquals("error", newError.getError()); //$NON-NLS-1$
        assertEquals("error_description", newError.getErrorDescription()); //$NON-NLS-1$
        assertEquals(null, newError.getSponsorEmail());
    }

    /**
     * Tests what happens when the parcel read contains invalid representation.
     */
    @SmallTest
    public void testParcelable_allNullFields() {
        final Parcel parcel = Parcel.obtain();

        // Write all 3 fields as null
        parcel.writeString(null);
        parcel.writeString(null);
        parcel.writeString(null);

        try {
            ErrorV13.CREATOR.createFromParcel(parcel);
            fail("should throw error"); //$NON-NLS-1$
        } catch (final AssertionError e) {
            // Expected exception
        }
    }

    /**
     * Tests {@link ErrorV13#getErrorDescriptionOrError()}.
     */
    @SmallTest
    public void testgetErrorDescriptionOrError() {
        {
            final ErrorV13 error = new ErrorV13("error", "error_description", "sponsor_email"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            assertEquals("error_description", error.getErrorDescriptionOrError()); //$NON-NLS-1$
        }

        {
            final ErrorV13 error = new ErrorV13("error", null, "sponsor_email"); //$NON-NLS-1$ //$NON-NLS-2$
            assertEquals("error", error.getErrorDescriptionOrError()); //$NON-NLS-1$
        }
    }

    /**
     * Tests {@link #equals(Object)} and {@link #hashCode()} methods.
     *
     * @throws JSONException for JSON parsing errors.
     */
    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test similarities
        final ErrorV13 error1 = new ErrorV13("error", "error_description", "sponsor_email"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        final ErrorV13 error2 = new ErrorV13("error", "error_description", "sponsor_email"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MoreAsserts.checkEqualsAndHashCodeMethods(error1, error2, true);

        // Test differences
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(ErrorV13JsonFactory.JsonKeys.class,
                new ErrorV13JsonFactory(), ErrorV13Fixture.getFullJsonObject(),
                new String[] { "MODEL_ROOT" }); //$NON-NLS-1$
    }

    /**
     * Tests {@link ErrorV13#toString()}.
     */
    @SmallTest
    public void testToString() {
        final ErrorV13 error = new ErrorV13("error", "error_description", "sponsor_email"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        final String errorString = error.toString();

        assertTrue(errorString.contains("error")); //$NON-NLS-1$
        assertTrue(errorString.contains("error_description")); //$NON-NLS-1$
        assertTrue(errorString.contains("sponsor_email")); //$NON-NLS-1$
    }
}

package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.test.AndroidTestCase;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.ErrorJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;

import org.json.JSONException;

/**
 * Tests {@link Error}.
 */
public final class ErrorTest extends AndroidTestCase {

    /**
     * Tests the constructor parameter validation.
     */
    @SmallTest
    public void testConstructor() {
        new Error("message", "object", "property"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        new Error("message", null, "property"); //$NON-NLS-1$ //$NON-NLS-2$
        new Error("message", "object", null); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Tests basic parceling.
     */
    @SmallTest
    public void testParcelable_basic() {
        final Error error = new Error("message", "object", "property"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        final Parcel parcel = Parcel.obtain();
        error.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        final byte[] bytes = parcel.marshall();
        parcel.recycle();

        final Parcel readParcel = Parcel.obtain();
        readParcel.unmarshall(bytes, 0, bytes.length);
        readParcel.setDataPosition(0);
        final Error newError = Error.CREATOR.createFromParcel(readParcel);

        assertEquals(error, newError);
        assertEquals("message", newError.getMessage()); //$NON-NLS-1$
        assertEquals("object", newError.getObject()); //$NON-NLS-1$
        assertEquals("property", newError.getProperty()); //$NON-NLS-1$
    }

    /**
     * Tests {@link #equals(Object)} and {@link #hashCode()} methods.
     *
     * @throws JSONException for JSON parsing errors.
     */
    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test similarities
        final Error error1 = new Error("message", "object", "property"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        final Error error2 = new Error("message", "object", "property"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MoreAsserts.checkEqualsAndHashCodeMethods(error1, error2, true);

        // Test differences
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(ErrorJsonFactory.JsonKeys.class,
                new ErrorJsonFactory(), ErrorFixture.getFullJsonObject(),
                new String[] { "MODEL_ROOT" }); //$NON-NLS-1$
    }

    /**
     * Tests {@link Error#toString()}.
     */
    @SmallTest
    public void testToString() {
        final Error error = new Error("message", "object", "property"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        final String errorString = error.toString();

        assertTrue(errorString.contains("message")); //$NON-NLS-1$
        assertTrue(errorString.contains("object")); //$NON-NLS-1$
        assertTrue(errorString.contains("property")); //$NON-NLS-1$
    }
}

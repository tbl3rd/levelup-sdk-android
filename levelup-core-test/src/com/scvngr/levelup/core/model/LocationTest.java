package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.LocationJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link Location}.
 */
public final class LocationTest extends SupportAndroidTestCase {
    @SmallTest
    public void testParcel() throws JSONException {
        final JSONObject object = LocationFixture.getFullJsonObject();

        final Location location = new LocationJsonFactory().from(object);
        final Parcel parcel = Parcel.obtain();
        location.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        final Location parceled = Location.CREATOR.createFromParcel(parcel);
        assertEquals(location, parceled);
        parcel.recycle();
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test differences across variations based on all JSON keys
        JsonTestUtil.checkEqualsAndHashCodeOnJsonVariants(LocationJsonFactory.JsonKeys.class,
                new LocationJsonFactory(), LocationFixture.getFullJsonObject(),
                new String[] { "MODEL_ROOT" }); //$NON-NLS-1$
    }
}

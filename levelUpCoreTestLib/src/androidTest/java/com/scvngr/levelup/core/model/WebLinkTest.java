package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.factory.json.WebLinkJsonFactory;
import com.scvngr.levelup.core.test.JsonTestUtil;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Tests {@link WebLink}.
 */
public final class WebLinkTest extends SupportAndroidTestCase {

    @SmallTest
    public void testParcel_full() {
        final WebLink loyalty = WebLinkFixture.getFullModel(1);
        final Parcel parcel = Parcel.obtain();

        try {
            loyalty.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            final WebLink parceled = WebLink.CREATOR.createFromParcel(parcel);
            assertEquals(loyalty, parceled);
        } finally {
            parcel.recycle();
        }
    }

    @SmallTest
    public void testParcel_minimal() {
        final WebLink loyalty = WebLinkFixture.getMinimalModel(1);
        final Parcel parcel = Parcel.obtain();

        try {
            loyalty.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            final WebLink parceled = WebLink.CREATOR.createFromParcel(parcel);
            assertEquals(loyalty, parceled);
        } finally {
            parcel.recycle();
        }
    }

    @SmallTest
    public void testEqualsAndHashCode() throws JSONException {
        // Test differences across variations based on all JSON keys
        JsonTestUtil
                .checkEqualsAndHashCodeOnJsonVariants(
                        WebLinkJsonFactory.JsonKeys.class,
                        new WebLinkJsonFactory(),
                        WebLinkFixture.getFullJsonObject(1),
                        new String[] {"MODEL_ROOT"});

        {
            final JSONObject object = WebLinkFixture.getFullJsonObject(1);
            final WebLink webLink1 = new WebLinkJsonFactory().from(object);
            object.put(WebLinkJsonFactory.JsonKeys.WEB_URL, "AAAA");
            final WebLink webLink2 = new WebLinkJsonFactory().from(object);
            MoreAsserts.checkEqualsAndHashCodeMethods(webLink1, webLink2, false);
        }

        {
            final JSONObject object = WebLinkFixture.getFullJsonObject(1);
            final WebLink webLink1 = new WebLinkJsonFactory().from(object);
            object.put(WebLinkJsonFactory.JsonKeys.TITLE, "AAA");
            final WebLink webLink2 = new WebLinkJsonFactory().from(object);
            MoreAsserts.checkEqualsAndHashCodeMethods(webLink1, webLink2, false);
        }

        {
            final JSONObject object = WebLinkFixture.getFullJsonObject(1);
            final WebLink webLink1 = new WebLinkJsonFactory().from(object);
            object.put(WebLinkJsonFactory.JsonKeys.WEB_LINK_TYPE_ID, "234");
            final WebLink webLink2 = new WebLinkJsonFactory().from(object);
            MoreAsserts.checkEqualsAndHashCodeMethods(webLink1, webLink2, false);
        }
    }

    @SmallTest
    public void testNullValues_throwException() throws JSONException {
        try {
            final JSONObject object = WebLinkFixture.getFullJsonObject(1);
            final WebLink webLink1 = new WebLinkJsonFactory().from(object);
            object.remove(WebLinkJsonFactory.JsonKeys.WEB_URL);
            final WebLink webLink2 = new WebLinkJsonFactory().from(object);
            fail("Should throw exception.");
        } catch (NullPointerException e) {
            // Do nothing
        }

        try {
            final JSONObject object = WebLinkFixture.getFullJsonObject(1);
            final WebLink webLink1 = new WebLinkJsonFactory().from(object);
            object.remove(WebLinkJsonFactory.JsonKeys.TITLE);
            final WebLink webLink2 = new WebLinkJsonFactory().from(object);
            fail("Should throw exception.");
        } catch (NullPointerException e) {
            // Do nothing
        }
    }
}

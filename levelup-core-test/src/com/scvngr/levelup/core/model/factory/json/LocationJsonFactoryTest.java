package com.scvngr.levelup.core.model.factory.json;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.model.Location;
import com.scvngr.levelup.core.model.LocationFixture;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Tests {@link LocationJsonFactory}.
 */
public final class LocationJsonFactoryTest extends AndroidTestCase {

    /**
     * Tests JSON parsing.
     *
     * @throws JSONException on parse errors.
     */
    @SmallTest
    public void testJsonParse_basic() throws JSONException {
        {
            final Location location =
                    new LocationJsonFactory().from(LocationFixture.getMinimalJsonObject());
            assertEquals(1, location.getId());
        }

        {
            final Location location =
                    new LocationJsonFactory().from(LocationFixture.getFullJsonObject());
            assertEquals(new HashSet<Integer>(Arrays.asList(2, 3, 5, 23)), location.getCategories());
            assertEquals("extended_address", location.getExtendedAddress()); //$NON-NLS-1$
            assertEquals("facebook_url", location.getUrl(Location.URL_FACEBOOK)); //$NON-NLS-1$
            assertEquals("hours", location.getHours()); //$NON-NLS-1$
            assertEquals(1, location.getId());
            assertEquals(LocationFixture.LOCATION_LATITUDE, location.getLatitude());
            assertEquals(LocationFixture.LOCATION_LONGITUDE, location.getLongitude());
            assertEquals("locality", location.getLocality()); //$NON-NLS-1$
            assertEquals(LocationFixture.MERCHANT_ID, location.getMerchantId());
            assertEquals("merchant_name", location.getMerchantName()); //$NON-NLS-1$
            assertEquals("menu_url", location.getUrl(Location.URL_MENU)); //$NON-NLS-1$
            assertEquals("name", location.getName()); //$NON-NLS-1$
            assertEquals("newsletter_url", location.getUrl(Location.URL_NEWSLETTER)); //$NON-NLS-1$
            assertEquals("opentable_url", location.getUrl(Location.URL_OPENTABLE)); //$NON-NLS-1$
            assertEquals("phone", location.getPhone()); //$NON-NLS-1$
            assertEquals("postal_code", location.getPostalCode()); //$NON-NLS-1$
            assertEquals("region", location.getRegion()); //$NON-NLS-1$
            assertEquals("street_address", location.getStreetAddress()); //$NON-NLS-1$
            assertEquals("twitter_url", location.getUrl(Location.URL_TWITTER)); //$NON-NLS-1$
            assertEquals("yelp_url", location.getUrl(Location.URL_YELP)); //$NON-NLS-1$
        }
    }

    /**
     * Tests JSON parsing with missing required keys fails.
     * @throws JSONException  on parse errors.
     */
    @SmallTest
    public void testJsonParse_missingRequiredKeys() throws JSONException {
        {
            final JSONObject object = LocationFixture.getMinimalJsonObject();
            object.remove(LocationJsonFactory.JsonKeys.ID);

            try {
                new LocationJsonFactory().from(object);
                fail("should throw JSONException"); //$NON-NLS-1$
            } catch (final JSONException e) {
                // Expected Exception
            }
        }
    }
}

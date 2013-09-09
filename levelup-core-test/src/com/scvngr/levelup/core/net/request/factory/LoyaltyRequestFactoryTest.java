package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;

import java.util.Locale;

/**
 * Tests {@link LoyaltyRequestFactory}.
 */
public final class LoyaltyRequestFactoryTest extends AndroidTestCase {

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final MockAccessTokenRetriever retriever = new MockAccessTokenRetriever();
        final LoyaltyRequestFactory factory = new LoyaltyRequestFactory(getContext(), retriever);
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertEquals(retriever, factory.getAccessTokenRetriever());
    }

    /**
     * Tests the {@link LoyaltyRequestFactory#buildGetLoyaltyForMerchantRequest(long)} method.
     *
     * @throws BadRequestException thrown by {@link AbstractRequest#getUrl(Context)}.
     */
    @SmallTest
    public void testBuildGetLoyaltyForMerchantRequest() throws BadRequestException {
        final AbstractRequest request =
                new LoyaltyRequestFactory(getContext(), new MockAccessTokenRetriever())
                        .buildGetLoyaltyForMerchantRequest(1);

        assertEquals(HttpMethod.GET, request.getMethod());
        final String path = request.getUrl(getContext()).getPath();
        assertNotNull(path);
        assertTrue(path.contains(LevelUpRequest.API_VERSION_CODE_V14));
        assertTrue(path.endsWith(String.format(Locale.US, "merchants/%d/loyalty", 1))); //$NON-NLS-1$
    }
}

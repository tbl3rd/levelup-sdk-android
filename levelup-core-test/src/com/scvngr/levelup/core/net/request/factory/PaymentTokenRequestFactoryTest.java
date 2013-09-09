package com.scvngr.levelup.core.net.request.factory;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;

import java.util.Locale;
import java.util.Map;

/**
 * Tests {@link PaymentTokenRequestFactory}.
 */
public final class PaymentTokenRequestFactoryTest extends AndroidTestCase {

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final MockAccessTokenRetriever retriever = new MockAccessTokenRetriever();
        final PaymentTokenRequestFactory factory =
                new PaymentTokenRequestFactory(getContext(), retriever);
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertEquals(retriever, factory.getAccessTokenRetriever());
    }

    @SmallTest
    public void testBuildGetPaymentTokenRequest() throws BadRequestException {
        final MockAccessTokenRetriever ret = new MockAccessTokenRetriever();
        final LevelUpRequest request =
                (LevelUpRequest) new PaymentTokenRequestFactory(getContext(), ret)
                        .buildGetPaymentTokenRequest();

        assertEquals(String.format(Locale.US, "/%s/payment_token", //$NON-NLS-1$
                LevelUpRequest.API_VERSION_CODE_V14), request.getUrl(getContext())
                .getPath());
        final Map<String, String> headers = request.getRequestHeaders(getContext());
        assertTrue(headers.containsKey(LevelUpRequest.HEADER_AUTHORIZATION));
        assertTrue(headers.get(LevelUpRequest.HEADER_AUTHORIZATION).contains(
                ret.getAccessToken(getContext()).getAccessToken()));
    }
}

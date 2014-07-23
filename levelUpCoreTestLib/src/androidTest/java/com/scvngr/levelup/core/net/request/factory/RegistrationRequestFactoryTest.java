package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.net.Uri;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.RequestUtils;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.NullUtils;

/**
 * Tests {@link RegistrationRequestFactory}.
 */
public final class RegistrationRequestFactoryTest extends SupportAndroidTestCase {

    @NonNull
    private static final String TEST_EMAIL = "test@example.com";

    /**
     * Test {@link RegistrationRequestFactory#buildRegistrationRequest}.
     *
     * @throws AbstractRequest.BadRequestException from {@link AbstractRequest#getUrl}
     */
    @SmallTest
    public void testBuildRegistrationRequest() throws AbstractRequest.BadRequestException {
        final Context context = NullUtils.nonNullContract(getContext());
        final LevelUpRequest request =
                (LevelUpRequest) new RegistrationRequestFactory(context)
                        .buildRegistrationRequest(TEST_EMAIL);

        assertNotNull(request);
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(0, request.getBodyLength(context));
        assertNull(request.getAccessToken(context));

        final String path = request.getUrl(context).getPath();

        assertTrue(path.contains(LevelUpRequest.API_VERSION_CODE_V15));
        assertTrue(path.contains(RegistrationRequestFactory.ENDPOINT_REGISTRATION));

        final Uri.Builder builder = new Uri.Builder();

        builder.appendQueryParameter(RequestUtils.PARAM_API_KEY, RequestUtils.getApiKey(context));
        builder.appendQueryParameter(RegistrationRequestFactory.PARAM_EMAIL, TEST_EMAIL);

        assertEquals(NullUtils.nonNullContract(builder.build()).getEncodedQuery(),
                request.getUrl(context).getQuery());
    }
}

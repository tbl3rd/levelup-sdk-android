package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpV13Request;

/**
 * Tests {@link CategoryRequestFactory}.
 */
public final class CategoryRequestFactoryTest extends AndroidTestCase {

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final CategoryRequestFactory factory = new CategoryRequestFactory(getContext());
        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertNull(factory.getAccessTokenRetriever());
    }

    /**
     * Tests the {@link CategoryRequestFactory#getCategories()} method.
     * 
     * @throws BadRequestException thrown by {@link AbstractRequest#getUrl(Context)}.
     */
    @SmallTest
    public void testGetCategories() throws BadRequestException {
        final AbstractRequest request = new CategoryRequestFactory(getContext()).getCategories();

        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(0, request.getQueryParams(getContext()).size());

        final String path = request.getUrl(getContext()).getPath();
        assertNotNull(path);
        assertTrue(path.contains(LevelUpV13Request.API_VERSION_CODE_V13));
        assertTrue(path.endsWith("categories.json")); //$NON-NLS-1$
    }
}

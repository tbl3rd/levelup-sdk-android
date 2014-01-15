/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.NullUtils;

/**
 * Tests {@link CategoryRequestFactory}.
 */
public final class CategoryRequestFactoryTest extends SupportAndroidTestCase {

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final CategoryRequestFactory factory =
                new CategoryRequestFactory(NullUtils.nonNullContract(getContext()));
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
        final Context context = NullUtils.nonNullContract(getContext());
        final AbstractRequest request = new CategoryRequestFactory(context).getCategories();

        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals(0, request.getQueryParams(context).size());

        final String path = request.getUrl(context).getPath();
        assertNotNull(path);
        assertTrue(path.contains(LevelUpRequest.API_VERSION_CODE_V14));
        assertTrue(path.endsWith("categories")); //$NON-NLS-1$
    }
}

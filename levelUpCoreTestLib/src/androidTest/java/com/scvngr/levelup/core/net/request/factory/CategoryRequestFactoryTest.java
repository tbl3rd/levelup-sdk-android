/*
 * Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
 * Tests {@link com.scvngr.levelup.core.net.request.factory.CategoryRequestFactory}.
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
     * Tests the {@link com.scvngr.levelup.core.net.request.factory.CategoryRequestFactory#getCategories()} method.
     *
     * @throws com.scvngr.levelup.core.net.AbstractRequest.BadRequestException thrown by {@link com.scvngr.levelup.core.net.AbstractRequest#getUrl(android.content.Context)}.
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
        assertTrue(path.endsWith("categories"));
    }
}

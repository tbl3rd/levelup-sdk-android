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
package com.scvngr.levelup.core.net.factory;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.test.AndroidTestCase;

import com.scvngr.levelup.core.net.request.factory.AbstractPagingRequestFactory.PageCacheRetriever;

import java.util.HashMap;

/**
 * A page cache retriever that stores pages in memory in a {@link HashMap}.
 */
public final class MockPageCacheRetriever implements PageCacheRetriever {
    private final HashMap<String, Uri> mPageMap = new HashMap<String, Uri>();

    /**
     * Asserts that the expected page is set for the given page key.
     *
     * @param pageKey the key under which the page is set.
     * @param expectedPage the page that is expected to be set.
     */
    public void assertNextPageSet(@NonNull final String pageKey,
            @Nullable final Uri expectedPage) {
        AndroidTestCase.assertTrue(mPageMap.containsKey(pageKey));
        AndroidTestCase.assertEquals(expectedPage, mPageMap.get(pageKey));
    }

    /**
     * Asserts that the given key has no next page set.
     *
     * @param pageKey the key under which the page is set.
     */
    public void assertNextPageNotSet(@NonNull final String pageKey) {
        AndroidTestCase.assertFalse(mPageMap.containsKey(pageKey));
    }

    @Override
    @Nullable
    public Uri getNextPageUrl(@NonNull final String pageKey) {
        return mPageMap.get(pageKey);
    }

    @Override
    public void setNextPage(@NonNull final String pageKey, @Nullable final Uri page) {
        mPageMap.put(pageKey, page);
    }
}

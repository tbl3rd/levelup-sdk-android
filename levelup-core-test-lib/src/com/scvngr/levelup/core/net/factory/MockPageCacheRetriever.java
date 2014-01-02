package com.scvngr.levelup.core.net.factory;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
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

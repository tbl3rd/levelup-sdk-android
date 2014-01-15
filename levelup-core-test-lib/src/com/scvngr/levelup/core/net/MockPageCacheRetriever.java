/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.net.Uri;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.net.request.factory.AbstractPagingRequestFactory.PageCacheRetriever;

import java.util.HashMap;
import java.util.Map;

/**
 * Test implementation of {@link PageCacheRetriever}.
 */
public final class MockPageCacheRetriever implements PageCacheRetriever {

    @NonNull
    private final Map<String, String> mPageKeys = new HashMap<String, String>();

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (null == obj) {
            return false;
        }

        if (obj instanceof MockPageCacheRetriever) {
            return true;
        }

        return false;
    }

    @Override
    @Nullable
    public Uri getNextPageUrl(@NonNull final String pageKey) {
        final String resultString = mPageKeys.get(pageKey);
        if (null != resultString) {
            return Uri.parse(resultString);
        }
        return null;
    }

    @Override
    public void setNextPage(@NonNull final String pageKey, @Nullable final Uri page) {
        if (null == page) {
            mPageKeys.remove(pageKey);
        } else {
            mPageKeys.put(pageKey, page.toString());
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

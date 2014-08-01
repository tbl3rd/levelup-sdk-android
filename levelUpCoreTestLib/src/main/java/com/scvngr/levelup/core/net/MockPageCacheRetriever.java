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
package com.scvngr.levelup.core.net;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

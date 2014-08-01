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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.util.NullUtils;
import com.scvngr.levelup.core.util.PreconditionUtil;

import net.jcip.annotations.Immutable;

/**
 * Base class for all AbstractRequest building classes.
 */
@Immutable
@LevelUpApi(contract = Contract.INTERNAL)
public abstract class AbstractRequestFactory {

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_DENSITY = "density";

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_HEIGHT = "height";

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_WIDTH = "width";

    /**
     * We currently only provide a 320dp wide image for locations.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String DEFAULT_WIDTH = "320";

    /**
     * We currently only provide a 212dp high image for locations.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String DEFAULT_HEIGHT = "212";

    /**
     * The Application context.
     */
    @NonNull
    private final Context mContext;

    /**
     * The implementation of {@link AccessTokenRetriever} to use to get the
     * {@link com.scvngr.levelup.core.model.User}'s
     * {@link com.scvngr.levelup.core.model.AccessToken} if needed.
     */
    @Nullable
    private final AccessTokenRetriever mAccessTokenRetriever;

    /**
     * Constructor.
     *
     * @param context Application context
     * @param retriever the implementation of {@link AccessTokenRetriever} to use to get the
     *        {@link com.scvngr.levelup.core.model.User}'s
     *        {@link com.scvngr.levelup.core.model.AccessToken} if needed.
     */
    public AbstractRequestFactory(@NonNull final Context context,
            @Nullable final AccessTokenRetriever retriever) {
        PreconditionUtil.assertNotNull(context, "context");

        mContext = NullUtils.nonNullContract(context.getApplicationContext());
        mAccessTokenRetriever = retriever;
    }

    /**
     * @return Application Context.
     */
    @NonNull
    protected Context getContext() {
        return mContext;
    }

    /**
     * @return the implementation of {@link AccessTokenRetriever} to use to get the
     *         {@link com.scvngr.levelup.core.model.User}'s
     *         {@link com.scvngr.levelup.core.model.AccessToken} if needed.
     */
    @Nullable
    protected AccessTokenRetriever getAccessTokenRetriever() {
        return mAccessTokenRetriever;
    }
}

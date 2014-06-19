/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;

import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.annotation.VisibleForTesting;
import com.scvngr.levelup.core.annotation.VisibleForTesting.Visibility;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.util.NullUtils;
import com.scvngr.levelup.core.util.PreconditionUtil;

/**
 * Base class for all AbstractRequest building classes.
 */
@Immutable
@LevelUpApi(contract = Contract.INTERNAL)
public abstract class AbstractRequestFactory {

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_DENSITY = "density"; //$NON-NLS-1$

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_HEIGHT = "height"; //$NON-NLS-1$

    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String PARAM_WIDTH = "width"; //$NON-NLS-1$

    /**
     * We currently only provide a 320dp wide image for locations.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String DEFAULT_WIDTH = "320"; //$NON-NLS-1$

    /**
     * We currently only provide a 212dp high image for locations.
     */
    @VisibleForTesting(visibility = Visibility.PRIVATE)
    /* package */static final String DEFAULT_HEIGHT = "212"; //$NON-NLS-1$

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
        PreconditionUtil.assertNotNull(context, "context"); //$NON-NLS-1$

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

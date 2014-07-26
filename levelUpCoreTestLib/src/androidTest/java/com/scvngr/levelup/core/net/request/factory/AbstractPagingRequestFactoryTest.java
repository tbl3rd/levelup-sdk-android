/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net.request.factory;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.test.suitebuilder.annotation.SmallTest;

import com.scvngr.levelup.core.net.AbstractRequest;
import com.scvngr.levelup.core.net.AbstractRequest.BadRequestException;
import com.scvngr.levelup.core.net.AccessTokenRetriever;
import com.scvngr.levelup.core.net.HttpMethod;
import com.scvngr.levelup.core.net.LevelUpRequest;
import com.scvngr.levelup.core.net.MockAccessTokenRetriever;
import com.scvngr.levelup.core.net.MockPageCacheRetriever;
import com.scvngr.levelup.core.test.SupportAndroidTestCase;
import com.scvngr.levelup.core.util.NullUtils;

import java.util.concurrent.CountDownLatch;

/**
 * Tests {@link com.scvngr.levelup.core.net.request.factory.AbstractPagingRequestFactory}.
 */
public final class AbstractPagingRequestFactoryTest extends SupportAndroidTestCase {

    @NonNull
    private static final String TEST_PAGE_ENDPOINT_ONE = "test_page_endpoint_one";

    @NonNull
    private static final String TEST_PAGE_ENDPOINT_TWO = "test_page_endpoint_two";

    @NonNull
    private static final String TEST_PAGE_KEY = "test_page_key";

    /**
     * Tests the constructor and expected invariants.
     */
    @SmallTest
    public void testConstructor() {
        final MockAccessTokenRetriever accessTokenRetriever = new MockAccessTokenRetriever();
        final MockPageCacheRetriever pageCacheRetriever = new MockPageCacheRetriever();
        final PagingRequestFactoryUnderTest factory =
                getPagingRequestFactory(accessTokenRetriever, pageCacheRetriever, false);

        assertEquals(getContext().getApplicationContext(), factory.getContext());
        assertEquals(accessTokenRetriever, factory.getAccessTokenRetriever());
        assertEquals(pageCacheRetriever, factory.getPageCacheRetriever());
    }

    /**
     * Tests the {@link com.scvngr.levelup.core.net.request.factory.AbstractPagingRequestFactory#getNextPageRequest()} method.
     */
    @SmallTest
    public void testGetNextPageRequest() {
        final MockAccessTokenRetriever accessTokenRetriever = new MockAccessTokenRetriever();
        final AbstractRequest pageOneRequest =
                getPageRequest(getContext(), accessTokenRetriever, TEST_PAGE_ENDPOINT_ONE);
        final AbstractRequest pageTwoRequest =
                getPageRequest(getContext(), accessTokenRetriever, TEST_PAGE_ENDPOINT_TWO);

        {
            final PagingRequestFactoryUnderTest factory =
                    getPagingRequestFactory(accessTokenRetriever, new MockPageCacheRetriever(),
                            false);

            assertEquals(pageOneRequest, factory.getNextPageRequest());
            assertEquals(0, factory.mFirstPageLatch.getCount());
            assertEquals(1, factory.mPageLatch.getCount());
        }

        {
            final MockPageCacheRetriever pageCacheRetriever = new MockPageCacheRetriever();
            pageCacheRetriever.setNextPage(TEST_PAGE_KEY, getUriForRequest(pageTwoRequest));
            final PagingRequestFactoryUnderTest factory =
                    getPagingRequestFactory(accessTokenRetriever, pageCacheRetriever, false);

            assertEquals(pageTwoRequest, factory.getNextPageRequest());
            assertEquals(1, factory.mFirstPageLatch.getCount());
            assertEquals(0, factory.mPageLatch.getCount());
        }

        {
            // Force an error to test first page fallback.
            final MockPageCacheRetriever pageCacheRetriever = new MockPageCacheRetriever();
            pageCacheRetriever.setNextPage(TEST_PAGE_KEY, getUriForRequest(pageTwoRequest));
            final PagingRequestFactoryUnderTest factory =
                    getPagingRequestFactory(accessTokenRetriever, pageCacheRetriever, true);

            assertEquals(pageOneRequest, factory.getNextPageRequest());
            assertEquals(0, factory.mFirstPageLatch.getCount());
            assertEquals(0, factory.mPageLatch.getCount());
        }
    }

    /**
     * Tests the {@link com.scvngr.levelup.core.net.request.factory.AbstractPagingRequestFactory#setNextPage(android.net.Uri)} method.
     */
    @SmallTest
    public void testSetNextPage() {
        final MockAccessTokenRetriever accessTokenRetriever = new MockAccessTokenRetriever();
        final AbstractRequest pageOneRequest =
                getPageRequest(getContext(), accessTokenRetriever, TEST_PAGE_ENDPOINT_ONE);
        final AbstractRequest pageTwoRequest =
                getPageRequest(getContext(), accessTokenRetriever, TEST_PAGE_ENDPOINT_TWO);
        final PagingRequestFactoryUnderTest factory =
                getPagingRequestFactory(accessTokenRetriever, new MockPageCacheRetriever(), false);

        factory.setNextPage(getUriForRequest(pageTwoRequest));
        assertEquals(pageTwoRequest, factory.getNextPageRequest());
        assertEquals(1, factory.mFirstPageLatch.getCount());
        assertEquals(0, factory.mPageLatch.getCount());

        factory.setNextPage(null);
        assertEquals(pageOneRequest, factory.getNextPageRequest());
        assertEquals(0, factory.mFirstPageLatch.getCount());
    }

    /**
     * Test implementation of a {@link com.scvngr.levelup.core.net.request.factory.AbstractPagingRequestFactory}.
     */
    public static final class PagingRequestFactoryUnderTest extends AbstractPagingRequestFactory {

        @NonNull
        private final CountDownLatch mFirstPageLatch = new CountDownLatch(1);

        @NonNull
        private final CountDownLatch mPageLatch = new CountDownLatch(1);

        private final boolean mForcePageRequestError;

        /**
         * @param context application context
         * @param retriever mock retriever
         * @param pageCacheRetriever mock page cache retriever
         * @param savedPageKey the key under which the page is stored
         * @param forcePageRequestError if true, this causes a page request error
         */
        public PagingRequestFactoryUnderTest(@NonNull final Context context,
                @NonNull final MockAccessTokenRetriever retriever,
                @NonNull final MockPageCacheRetriever pageCacheRetriever,
                @NonNull final String savedPageKey, final boolean forcePageRequestError) {
            super(context, retriever, pageCacheRetriever, savedPageKey);
            mForcePageRequestError = forcePageRequestError;
        }

        @Override
        @NonNull
        public AbstractRequest getFirstPageRequest() {
            mFirstPageLatch.countDown();
            return AbstractPagingRequestFactoryTest.getPageRequest(getContext(),
                    getAccessTokenRetriever(), TEST_PAGE_ENDPOINT_ONE);
        }

        @Override
        @Nullable
        public AbstractRequest getPageRequest(@NonNull final Uri page) {
            mPageLatch.countDown();

            if (mForcePageRequestError) {
                return null;
            }

            return AbstractPagingRequestFactoryTest.getPageRequest(getContext(),
                    getAccessTokenRetriever(), page);
        }

        @Override
        @NonNull
        protected AccessTokenRetriever getAccessTokenRetriever() {
            // This is OK because we always provide a retriever
            return NullUtils.nonNullContract(super.getAccessTokenRetriever());
        }
    }

    /**
     * Get an {@link com.scvngr.levelup.core.net.AbstractRequest} for an endpoint.
     *
     * @param context the Application context.
     * @param retriever an access token retriever.
     * @param endpoint the endpoint of the request.
     * @return The {@link com.scvngr.levelup.core.net.AbstractRequest} for the endpoint.
     */
    @NonNull
    private static AbstractRequest getPageRequest(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever, @NonNull final String endpoint) {
        return new LevelUpRequest(context, HttpMethod.GET, LevelUpRequest.API_VERSION_CODE_V14,
                endpoint, null, null, retriever);
    }

    /**
     * Get an {@link com.scvngr.levelup.core.net.AbstractRequest} for a page.
     *
     * @param context the Application context.
     * @param retriever an access token retriever.
     * @param page the URL of the next page that should be loaded.
     * @return The {@link com.scvngr.levelup.core.net.AbstractRequest} for the page.
     */
    @NonNull
    private static AbstractRequest getPageRequest(@NonNull final Context context,
            @NonNull final AccessTokenRetriever retriever, @NonNull final Uri page) {
        return new LevelUpRequest(context, HttpMethod.GET, page,  null,
                retriever);
    }

    /**
     * Get a {@link com.scvngr.levelup.core.net.request.factory.AbstractPagingRequestFactoryTest.PagingRequestFactoryUnderTest} for testing.
     *
     * @param accessTokenRetriever an access token retriever.
     * @param pageCacheRetriever a page cache retriever.
     * @param forcePageRequestError whether or not to force
     *        {@link com.scvngr.levelup.core.net.request.factory.AbstractPagingRequestFactoryTest.PagingRequestFactoryUnderTest#getPageRequest(android.net.Uri)} to return null, simulating
     *        an error.
     * @return the factory
     */
    @NonNull
    private PagingRequestFactoryUnderTest getPagingRequestFactory(
            @NonNull final MockAccessTokenRetriever accessTokenRetriever,
            @NonNull final MockPageCacheRetriever pageCacheRetriever,
            final boolean forcePageRequestError) {
        return new PagingRequestFactoryUnderTest(getContext(), accessTokenRetriever,
                pageCacheRetriever, TEST_PAGE_KEY, forcePageRequestError);
    }

    @NonNull
    private Uri getUriForRequest(@NonNull final AbstractRequest request) {
        try {
            return NullUtils.nonNullContract(Uri.parse(request.getUrlString(getContext())));
        } catch (final BadRequestException e) {
            throw new RuntimeException(e);
        }
    }
}

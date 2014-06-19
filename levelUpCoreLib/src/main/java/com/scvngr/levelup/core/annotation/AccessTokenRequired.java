/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes a LevelUp web request that requires an {@link com.scvngr.levelup.core.model.AccessToken}.
 * Callers should ensure that they pass a non-null
 * {@link com.scvngr.levelup.core.net.AccessTokenRetriever} to the Request factory that is creating
 * the {@link com.scvngr.levelup.core.net.AbstractRequest}.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
public @interface AccessTokenRequired {
    // This space intentionally left blank.
}

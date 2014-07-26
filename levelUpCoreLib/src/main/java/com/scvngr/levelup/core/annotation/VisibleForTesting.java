/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.annotation;

import android.support.annotation.NonNull;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes code that has a higher visibility for testing purposes.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
public @interface VisibleForTesting {
    /**
     * Documents the intended visibility.
     */
    public static enum Visibility {
        /**
         * The visibility is intended to be public.
         */
        @NonNull
        PUBLIC,

        /**
         * The visibility is intended to be package.
         */
        @NonNull
        PACKAGE,

        /**
         * The visibility is intended to be protected.
         */
        @NonNull
        PROTECTED,

        /**
         * The visibility is intended to be private.
         */
        @NonNull
        PRIVATE
    }

    /**
     * @return the level of visibility for this class.
     */
    Visibility visibility();
}

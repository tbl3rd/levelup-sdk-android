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

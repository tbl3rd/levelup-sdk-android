/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes the state of development of a given API contract.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
public @interface LevelUpApi {
    /**
     * Documents the intended visibility.
     */
    public static enum Contract {

        /**
         * This is a final public API.
         * <p>
         * This API is unlikely to change in future versions of the SDK.
         */
        @NonNull
        PUBLIC,

        /**
         * This is a draft public API that may change.
         */
        @NonNull
        DRAFT,

        /**
         * This is a non-public API that is used internally within the LevelUp SDK. Clients of the
         * SDK should not use APIs annotated with this.
         */
        @NonNull
        INTERNAL,

        /**
         * This is an API that is only available to clients in the <a href="http://developer
         * .thelevelup.com/enterprise-sdk/">LevelUp Enterprise Program</a>.
         */
        @NonNull
        ENTERPRISE
    }

    /**
     * @return the declared API level for this class.
     */
    Contract contract();
}

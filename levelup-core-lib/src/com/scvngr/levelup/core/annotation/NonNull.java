package com.scvngr.levelup.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes that a reference cannot be null.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
public @interface NonNull {
    // this space intentionally left blank
}

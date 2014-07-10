package com.scvngr.levelup.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates methods that return a request requiring at least one of the specified permissions.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface RequiresPermission {
    /**
     * @return the permissions that allow the request to be performed.
     */
    String[] value();
}

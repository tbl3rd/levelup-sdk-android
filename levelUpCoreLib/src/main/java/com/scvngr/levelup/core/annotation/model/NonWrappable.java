/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.annotation.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Indicates to the JSON serializer that this type should not be wrapped with type information when
 * serializing to/from JSON. A wrapped serialization is inside a JSON object with a single key
 * containing the type, e.g.:
 * </p>
 * <pre>
 *            { "typeKey": { "field1": "test", "field2": "value" } }
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NonWrappable {
    // This body intentionally left blank.
}

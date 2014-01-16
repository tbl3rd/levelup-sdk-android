/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documents intended mappings for JSON field key strings regarding the key's associated value's
 * type mapping in JSON.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonValueType {
    /**
     * Field type mappings supported by JSONObject.
     */
    public static enum JsonType {
        /**
         * Value is a JSON true or JSON false that should map to a Java boolean.
         */
        BOOLEAN,

        /**
         * Value is a JSON number and should be read as a Java double.
         */
        DOUBLE,

        /**
         * Value is a JSON number that should be read as a Java int.
         */
        INT,

        /**
         * Value is a JSON array.
         */
        JSON_ARRAY,

        /**
         * Value is a JSON object.
         */
        JSON_OBJECT,

        /**
         * Value is a JSON number that should be read as a Java long.
         */
        LONG,

        /**
         * Value is a JSON string.
         */
        STRING
    }

    /**
     * Documents the intended parser mapping from JSON object fields. This will
     * usually (but not always) match the type of the model's related field, but
     * may not where the model has a more complex type not supported by JSON or
     * JSONObject (e.g. a User's birthday may be a JSON String, but a Java Date
     * in the Model).
     */
    @NonNull
    JsonType value();
}

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
        @NonNull
        BOOLEAN,

        /**
         * Value is a JSON number and should be read as a Java double.
         */
        @NonNull
        DOUBLE,

        /**
         * Value is a JSON number that should be read as a Java int.
         */
        @NonNull
        INT,

        /**
         * Value is a JSON array.
         */
        @NonNull
        JSON_ARRAY,

        /**
         * Value is a JSON object.
         */
        @NonNull
        JSON_OBJECT,

        /**
         * Value is a JSON number that should be read as a Java long.
         */
        @NonNull
        LONG,

        /**
         * Value is a JSON string.
         */
        @NonNull
        STRING
    }

    /**
     * Documents the intended parser mapping from JSON object fields. This will usually (but not
     * always) match the type of the model's related field, but may not where the model has a more
     * complex type not supported by JSON or JSONObject (e.g. a User's birthday may be a JSON
     * String, but a Java Date in the Model).
     */
    @NonNull
    JsonType value();
}

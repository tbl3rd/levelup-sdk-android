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
package com.scvngr.levelup.core.model;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.model.factory.json.OrderJsonFactory;

import net.jcip.annotations.ThreadSafe;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fixture for {@link Order}.
 */
@ThreadSafe
public final class OrderFixture {
    /**
     * A constant UUID fixture, #1.
     */
    @NonNull
    public static final String UUID_FIXTURE_1 = "123456789abcdef0123456789abcdef0";

    /**
     * A constant UUID fixture, #2.
     */
    public static final String UUID_FIXTURE_2 = "fedcba9876543210fedcba9876543210";

    /**
     * A constant UUID fixture, #3.
     */
    public static final String UUID_FIXTURE_3 = "fedcba98765432100123456789abcdef";

    /**
     * A string to use for a date/time fixture.
     */
    public static final String DATE_TIME_FIXTURE_1 = "2012-12-04T18:10:45-05:00";

    /**
     * A second string to use for a date/time fixture.
     */
    public static final String DATE_TIME_FIXTURE_2 = "2009-01-03T18:10:45-05:23";

    /**
     * @return A minimal {@link Order} model.
     */
    @NonNull
    public static Order getMinimalModel() {
        return getMinimalModel(UUID_FIXTURE_1);
    }

    /**
     * @param uuid the UUID of the order on the web service.
     * @return A minimal {@link Order} model.
     */
    @NonNull
    public static Order getMinimalModel(final String uuid) {
        return Order.builder().uuid(uuid).createdAt("2012-12-04T18:10:45-05:00").build();
    }

    /**
     * @return A full {@link Order} model.
     */
    @NonNull
    public static Order getFullModel() {
        return getFullModel(UUID_FIXTURE_1);
    }

    /**
     * @param uuid the UUID of the order on the web service.
     * @return A full {@link Order} model.
     */
    @NonNull
    public static Order getFullModel(final String uuid) {
        return Order
                .builder()
                .uuid(uuid)
                .balanceAmount(MonetaryValueFixture.getFullModel())
                .bundleClosedAt(DATE_TIME_FIXTURE_1)
                .bundleDescriptor("bundle_descriptor")
                .contributionAmount(MonetaryValueFixture.getFullModel())
                .contributionTargetName("contribution_target_name")
                .createdAt(DATE_TIME_FIXTURE_1)
                .creditAppliedAmount(MonetaryValueFixture.getFullModel())
                .creditEarnedAmount(MonetaryValueFixture.getFullModel())
                .locationExtendedAddress("location_extended_address")
                .locationLocality("location_locality")
                .locationName("location_name")
                .locationPostalCode("location_postal_code")
                .locationRegion("location_region")
                .locationStreetAddress("location_street_address")
                .locationWebServiceId(1L)
                .merchantWebServiceId(1L)
                .merchantName("merchant_name")
                .refundedAt(DATE_TIME_FIXTURE_1).spendAmount(MonetaryValueFixture.getFullModel())
                .tipAmount(MonetaryValueFixture.getFullModel())
                .totalAmount(MonetaryValueFixture.getFullModel()).transactedAt(DATE_TIME_FIXTURE_1)
                .build();
    }

    /**
     * @return A minimal {@link Order} model.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject() {
        return getMinimalJsonObject(UUID_FIXTURE_1);
    }

    /**
     * @param uuid the UUID of the order on the web service.
     * @return A minimal {@link Order} model.
     */
    @NonNull
    public static JSONObject getMinimalJsonObject(final String uuid) {
        try {
            final JSONObject object = new JSONObject();
            object.put(OrderJsonFactory.JsonKeys.UUID, uuid);
            object.put(OrderJsonFactory.JsonKeys.CREATED_AT, DATE_TIME_FIXTURE_1);
            return object;
        } catch (final JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return A minimal {@link Order} model.
     */
    @NonNull
    public static JSONObject getFullJsonObject() {
        return getFullJsonObject(UUID_FIXTURE_1);
    }

    /**
     * @param uuid the UUID of the order on the web service.
     * @return A minimal {@link Order} model.
     */
    @NonNull
    public static JSONObject getFullJsonObject(final String uuid) {
        try {
            final JSONObject object = getMinimalJsonObject(uuid);
            object.put(OrderJsonFactory.JsonKeys.BALANCE, MonetaryValueFixture.TEN_DOLLARS);
            object.put(OrderJsonFactory.JsonKeys.BUNDLE_CLOSED_AT, DATE_TIME_FIXTURE_1);
            object.put(OrderJsonFactory.JsonKeys.BUNDLE_DESCRIPTOR, "bundle_descriptor");
            object.put(OrderJsonFactory.JsonKeys.CREDIT_APPLIED, MonetaryValueFixture.TEN_DOLLARS);
            object.put(OrderJsonFactory.JsonKeys.CREDIT_EARNED, MonetaryValueFixture.TEN_DOLLARS);
            object.put(OrderJsonFactory.JsonKeys.CONTRIBUTION, MonetaryValueFixture.TEN_DOLLARS);
            object.put(OrderJsonFactory.JsonKeys.CONTRIBUTION_TARGET_NAME,
                    "contribution_target_name");
            object.put(OrderJsonFactory.JsonKeys.LOCATION_EXTENDED_ADDRESS,
                    "location_extended_address");
            object.put(OrderJsonFactory.JsonKeys.LOCATION_LOCALITY, "location_locality");
            object.put(OrderJsonFactory.JsonKeys.LOCATION_NAME, "location_name");
            object.put(OrderJsonFactory.JsonKeys.LOCATION_POSTAL_CODE, "location_postal_code");
            object.put(OrderJsonFactory.JsonKeys.LOCATION_REGION, "location_region");
            object.put(OrderJsonFactory.JsonKeys.LOCATION_STREET_ADDRESS, "location_street_address");
            object.put(OrderJsonFactory.JsonKeys.LOCATION_WEB_SERVICE_ID, 1L);
            object.put(OrderJsonFactory.JsonKeys.MERCHANT_NAME, "merchant_name");
            object.put(OrderJsonFactory.JsonKeys.MERCHANT_WEB_SERVICE_ID, 1L);
            object.put(OrderJsonFactory.JsonKeys.REFUNDED_AT, DATE_TIME_FIXTURE_1);
            object.put(OrderJsonFactory.JsonKeys.SPEND, MonetaryValueFixture.TEN_DOLLARS);
            object.put(OrderJsonFactory.JsonKeys.TIP, MonetaryValueFixture.TEN_DOLLARS);
            object.put(OrderJsonFactory.JsonKeys.TOTAL, MonetaryValueFixture.TEN_DOLLARS);
            object.put(OrderJsonFactory.JsonKeys.TRANSACTED_AT, DATE_TIME_FIXTURE_1);

            return object;
        } catch (final JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private OrderFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}

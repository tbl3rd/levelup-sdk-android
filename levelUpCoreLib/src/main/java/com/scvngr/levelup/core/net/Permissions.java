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

package com.scvngr.levelup.core.net;

/**
 * A list of permissions known to be supported by LevelUp’s web service.
 *
 * @see <a href="http://developer.thelevelup.com/getting-started/permissions-list/">LevelUp
 * permissions</a>
 */
@SuppressWarnings("unused")
public final class Permissions {

    /**
     * Charge a user on behalf of a merchant.
     */
    public static final String PERMISSION_CREATE_ORDERS = "create_orders";

    /**
     * Read a user’s addresses and add new ones.
     */
    public static final String PERMISSION_MANAGE_USER_ADDRESSES = "manage_user_addresses";

    /**
     * View credit for a user and claim campaigns.
     */
    public static final String PERMISSION_MANAGE_USER_CAMPAIGNS = "manage_user_campaigns";

    /**
     * Read a user’s QR code.
     */
    public static final String PERMISSION_READ_QR_CODE = "read_qr_code";

    /**
     * Read a user’s name, email, birthday and gender.
     */
    public static final String PERMISSION_READ_USER_BASIC_INFO = "read_user_basic_info";

    /**
     * Read a users transaction history, with item level detail.
     */
    public static final String PERMISSION_READ_USER_ORDERS = "read_user_orders";

    /**
     * Create a new credit card entry.
     */
    public static final String PERMISSION_CREATE_FIRST_CREDIT_CARD = "create_first_credit_card";

    /**
     * This is a utility class and cannot be instantiated.
     */
    private Permissions() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}

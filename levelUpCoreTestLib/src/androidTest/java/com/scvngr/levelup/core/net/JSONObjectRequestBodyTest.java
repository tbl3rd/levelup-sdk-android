/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.model.CategoryFixture;

/**
 * Tests {@link com.scvngr.levelup.core.net.JSONObjectRequestBody}.
 */
public final class JSONObjectRequestBodyTest extends AbstractRequestBodyTest<JSONObjectRequestBody> {

    @Override
    @NonNull
    protected JSONObjectRequestBody getFixture() {
        return new JSONObjectRequestBody(CategoryFixture.getFullJsonObject());
    }
}

/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.CategoryFixture;
import com.scvngr.levelup.core.util.NullUtils;

/**
 * Tests {@link JSONObjectRequestBody}.
 */
public final class JSONObjectRequestBodyTest extends AbstractRequestBodyTest<JSONObjectRequestBody> {

    @Override
    @NonNull
    protected JSONObjectRequestBody getFixture() {
        return new JSONObjectRequestBody(CategoryFixture.getFullJsonObject());
    }

    @Override
    @NonNull
    protected String getFixtureAsString() {
        return NullUtils.nonNullContract(CategoryFixture.getFullJsonObject().toString());
    }
}

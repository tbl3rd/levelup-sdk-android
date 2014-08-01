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

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.scvngr.levelup.core.annotation.LevelUpApi;

import net.jcip.annotations.Immutable;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Body content of a request.
 */
@Immutable
@LevelUpApi(contract = LevelUpApi.Contract.DRAFT)
public interface RequestBody extends Parcelable {

    /**
     * Write the body content to the output stream.
     *
     * @param context Android application context.
     * @param outputStream the output stream to write to.
     * @throws IOException if there are any errors writing to the output stream.
     */
    void writeToOutputStream(@NonNull Context context, @NonNull OutputStream outputStream)
            throws IOException;

    /**
     * @return the content length of the body.
     */
    int getContentLength();

    /**
     * @return the MIME type of the content that this body represents.
     */
    @NonNull
    String getContentType();
}

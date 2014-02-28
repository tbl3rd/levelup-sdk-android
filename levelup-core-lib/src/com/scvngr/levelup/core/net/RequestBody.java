/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.net;

import android.content.Context;
import android.os.Parcelable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.NonNull;

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
    public void writeToOutputStream(@NonNull Context context, @NonNull OutputStream outputStream)
            throws IOException;

    /**
     * @return the content length of the body.
     */
    public int getContentLength();

    /**
     * @return the MIME type of the content that this body represents.
     */
    @NonNull
    public String getContentType();
}

/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.model.RequiredField;
import com.scvngr.levelup.core.util.NullUtils;

import net.jcip.annotations.Immutable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.val;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * A request for a set of permissions, made by an {@link App} on behalf of the user. Typically,
 * this is used to construct an interface which presents the set of permissions to the user
 * alongside the {@link App} details, prompting them to accept or reject the {@link App}'s request
 * for the permissions. If accepted, the app is then granted an {@link
 * com.scvngr.levelup.core.model.AccessToken} which lets it wield these permissions.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.DRAFT)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class PermissionsRequest implements Parcelable {

    /**
     * {@link Parcelable} creator.
     */
    @NonNull
    public static final Creator<PermissionsRequest> CREATOR = new PermissionsRequestCreator();

    /**
     * The text to display to the user on the "Accept" affordance.
     */
    @NonNull
    @RequiredField
    String acceptText;

    /**
     * The ID of the app this request is for.
     */
    long appId;

    /**
     * The date this request was created.
     */
    @NonNull
    @RequiredField
    String createdAt;

    /**
     * A short, human-readable description of this permissions request.
     */
    @NonNull
    @RequiredField
    String description;

    /**
     * The web service ID of this request.
     */
    long id;

    /**
     * The set of permissions being requested. All of these must be presented when displaying this
     * permissions request.
     */
    @NonNull
    @RequiredField
    Set<Permission> permissions;

    /**
     * The text to display to the user on the "Reject" affordance.
     */
    @NonNull
    @RequiredField
    String rejectText;

    /**
     * The state of the request.
     */
    @NonNull
    @RequiredField
    State state;

    /**
     * The access token, if one was provided by the web service.
     */
    @Nullable
    String token;

    /**
     * The state of the permissions request, according to the web service.
     */
    public enum State {
        /**
         * The permissions request is pending and the user should be prompted for their preference.
         */
        @NonNull
        PENDING("pending"),

        /**
         * The permissions request has been accepted by the user.
         */
        @NonNull
        ACCEPTED("accepted"),

        /**
         * The permissions request has been rejected by the user.
         */
        @NonNull
        REJECTED("rejected");

        @NonNull
        private final String value;

        /**
         * Creates a new permissions request state.
         *
         * @param value the web service key for the given state.
         */
        State(@NonNull final String value) {
            this.value = value;
        }

        /**
         * Given a web service key, returns the corresponding state.
         *
         * @param value the web service key for the given state.
         * @return the matching state. An {@link java.lang.IllegalArgumentException} will be thrown
         * if there is no match.
         */
        @NonNull
        public static State forString(@NonNull final String value) {
            for (final State state : values()) {
                if (state.value.equals(value)) {
                    return state;
                }
            }

            throw new IllegalArgumentException(
                    NullUtils.format("\'%s\' is not a valid state", value));
        }

        @NonNull
        public String toString() {
            return value;
        }
    }

    /**
     * @deprecated see {@link com.scvngr.levelup.core.model.PermissionsRequest#PermissionsRequest(String,
     * long, String, String, long, java.util.Set, String, com.scvngr.levelup.core.model.PermissionsRequest.State,
     * String)}
     */
    @Deprecated
    public PermissionsRequest(@NonNull final String acceptText, final long appId,
            @NonNull final String createdAt, @NonNull final String description, final long id,
            @NonNull final Set<Permission> permissions, @NonNull final String rejectText,
            @NonNull final State state) {
        this(acceptText, appId, createdAt, description, id, permissions, rejectText, state, null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((PermissionsRequestCreator) CREATOR).writeToParcel(NullUtils.nonNullContract(dest), this);
    }

    /**
     * {@link android.os.Parcelable} Creator.
     */
    /* package */static final class PermissionsRequestCreator
            implements Creator<PermissionsRequest> {

        @Override
        public PermissionsRequest[] newArray(final int size) {
            return new PermissionsRequest[size];
        }

        @Override
        public PermissionsRequest createFromParcel(final Parcel source) {
            val acceptText = NullUtils.nonNullContract(source.readString());
            val appId = source.readLong();
            val createdAt = NullUtils.nonNullContract(source.readString());
            val description = NullUtils.nonNullContract(source.readString());
            val id = source.readLong();
            val permissionsList = new ArrayList<Permission>();
            source.readTypedList(permissionsList, Permission.CREATOR);
            val permissions = new HashSet<Permission>(permissionsList);
            val rejectText = NullUtils.nonNullContract(source.readString());
            val state = State.forString(NullUtils.nonNullContract(source.readString()));
            val token = source.readString();

            return new PermissionsRequest(acceptText, appId, createdAt, description, id,
                    permissions, rejectText, state, token);
        }

        /**
         * Writes the given permissions request to the destination parcel. This order matches that
         * in {@link #createFromParcel}.
         *
         * @param dest the destination parcel.
         * @param permissionsRequest the permissions request to serialize.
         */
        /* package */void writeToParcel(@NonNull final Parcel dest,
                @NonNull final PermissionsRequest permissionsRequest) {
            dest.writeString(permissionsRequest.getAcceptText());
            dest.writeLong(permissionsRequest.getAppId());
            dest.writeString(permissionsRequest.getCreatedAt());
            dest.writeString(permissionsRequest.getDescription());
            dest.writeLong(permissionsRequest.getId());
            dest.writeTypedList(new ArrayList<Permission>(permissionsRequest.getPermissions()));
            dest.writeString(permissionsRequest.getRejectText());
            dest.writeString(permissionsRequest.getState().toString());
            dest.writeString(permissionsRequest.getToken());
        }
    }
}

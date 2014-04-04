// Generated by delombok at Tue May 13 11:08:28 EDT 2014
/**
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;
import com.scvngr.levelup.core.util.NullUtils;
import net.jcip.annotations.Immutable;
import java.lang.Deprecated;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * A request for a set of permissions, made by an {@link App} on behalf of the user. Typically, this
 * is used to construct an interface which presents the set of permissions to the user alongside the
 * {@link App} details, prompting them to accept or reject the {@link App}'s request for the
 * permissions. If accepted, the app is then granted an
 * {@link com.scvngr.levelup.core.model.AccessToken} which lets it wield these permissions.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
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
    private final String acceptText;
    
    /**
     * The ID of the app this request is for.
     */
    private final long appId;
    
    /**
     * The date this request was created.
     */
    @NonNull
    private final String createdAt;
    
    /**
     * A short, human-readable description of this permissions request.
     */
    @NonNull
    private final String description;
    
    /**
     * The web service ID of this request.
     */
    private final long id;
    
    /**
     * The set of permissions being requested. All of these must be presented when displaying this
     * permissions request.
     */
    @NonNull
    private final Set<Permission> permissions;
    
    /**
     * The text to display to the user on the "Reject" affordance.
     */
    @NonNull
    private final String rejectText;
    
    /**
     * The state of the request.
     */
    @NonNull
    private final State state;
    
    /**
     * The access token, if one was provided by the web service.
     */
    @Nullable
    private final String token;

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
         * @param value the web service key for the given state.
         */
        State(@NonNull final String value) {
            this.value = value;
        }
        
        /**
         * Given a web service key, returns the corresponding state.
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
            throw new IllegalArgumentException(NullUtils.format("\'%s\' is not a valid state", value));
        }
        
        @NonNull
        public String toString() {
            return value;
        }
    }

    /**
     * @deprecated see {@link com.scvngr.levelup.core.model.PermissionsRequest#PermissionsRequest(String, long, String, String, long, java.util.Set, String, com.scvngr.levelup.core.model.PermissionsRequest.State, String)}
     */
    @Deprecated
    public PermissionsRequest(@NonNull final String acceptText, final long appId, @NonNull final String createdAt, @NonNull final String description, final long id, @NonNull final Set<Permission> permissions, @NonNull final String rejectText, @NonNull final State state) {
        this(acceptText, appId, createdAt, description, id, permissions, rejectText, state, null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((PermissionsRequestCreator)CREATOR).writeToParcel(NullUtils.nonNullContract(dest), this);
    }
    /* package */

    /**
     * {@link android.os.Parcelable} Creator.
     */
    static final class PermissionsRequestCreator implements Creator<PermissionsRequest> {


        @Override
        public PermissionsRequest[] newArray(final int size) {
            return new PermissionsRequest[size];
        }

        @Override
        public PermissionsRequest createFromParcel(final Parcel source) {
            final java.lang.String acceptText = NullUtils.nonNullContract(source.readString());
            final long appId = source.readLong();
            final java.lang.String createdAt = NullUtils.nonNullContract(source.readString());
            final java.lang.String description = NullUtils.nonNullContract(source.readString());
            final long id = source.readLong();
            final java.util.ArrayList<com.scvngr.levelup.core.model.Permission> permissionsList = new ArrayList<Permission>();
            source.readTypedList(permissionsList, Permission.CREATOR);
            final java.util.HashSet<com.scvngr.levelup.core.model.Permission> permissions = new HashSet<Permission>(permissionsList);
            final java.lang.String rejectText = NullUtils.nonNullContract(source.readString());
            final com.scvngr.levelup.core.model.PermissionsRequest.State state = State.forString(NullUtils.nonNullContract(source.readString()));
            final java.lang.String token = source.readString();
            return new PermissionsRequest(acceptText, appId, createdAt, description, id, permissions, rejectText, state, token);
        }
        /**
         * Writes the given permissions request to the destination parcel. This order matches that
         * in {@link #createFromParcel}.
         *
         * @param dest the destination parcel.
         * @param permissionsRequest the permissions request to serialize.
         */
        /* package */

        void writeToParcel(@NonNull final Parcel dest, @NonNull final PermissionsRequest permissionsRequest) {
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

    /**
     * The text to display to the user on the "Accept" affordance.
     */
    @NonNull
    @SuppressWarnings("all")
    public String getAcceptText() {
        return this.acceptText;
    }

    /**
     * The ID of the app this request is for.
     */
    @SuppressWarnings("all")
    public long getAppId() {
        return this.appId;
    }

    /**
     * The date this request was created.
     */
    @NonNull
    @SuppressWarnings("all")
    public String getCreatedAt() {
        return this.createdAt;
    }

    /**
     * A short, human-readable description of this permissions request.
     */
    @NonNull
    @SuppressWarnings("all")
    public String getDescription() {
        return this.description;
    }

    /**
     * The web service ID of this request.
     */
    @SuppressWarnings("all")
    public long getId() {
        return this.id;
    }

    /**
     * The set of permissions being requested. All of these must be presented when displaying this
     * permissions request.
     */
    @NonNull
    @SuppressWarnings("all")
    public Set<Permission> getPermissions() {
        return this.permissions;
    }

    /**
     * The text to display to the user on the "Reject" affordance.
     */
    @NonNull
    @SuppressWarnings("all")
    public String getRejectText() {
        return this.rejectText;
    }

    /**
     * The state of the request.
     */
    @NonNull
    @SuppressWarnings("all")
    public State getState() {
        return this.state;
    }

    /**
     * The access token, if one was provided by the web service.
     */
    @Nullable
    @SuppressWarnings("all")
    public String getToken() {
        return this.token;
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PermissionsRequest)) return false;
        final PermissionsRequest other = (PermissionsRequest)o;
        final Object this$acceptText = this.getAcceptText();
        final Object other$acceptText = other.getAcceptText();
        if (this$acceptText == null ? other$acceptText != null : !this$acceptText.equals(other$acceptText)) return false;
        if (this.getAppId() != other.getAppId()) return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
        if (this.getId() != other.getId()) return false;
        final Object this$permissions = this.getPermissions();
        final Object other$permissions = other.getPermissions();
        if (this$permissions == null ? other$permissions != null : !this$permissions.equals(other$permissions)) return false;
        final Object this$rejectText = this.getRejectText();
        final Object other$rejectText = other.getRejectText();
        if (this$rejectText == null ? other$rejectText != null : !this$rejectText.equals(other$rejectText)) return false;
        final Object this$state = this.getState();
        final Object other$state = other.getState();
        if (this$state == null ? other$state != null : !this$state.equals(other$state)) return false;
        final Object this$token = this.getToken();
        final Object other$token = other.getToken();
        if (this$token == null ? other$token != null : !this$token.equals(other$token)) return false;
        return true;
    }

    @Override
    @SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 277;
        int result = 1;
        final Object $acceptText = this.getAcceptText();
        result = result * PRIME + ($acceptText == null ? 0 : $acceptText.hashCode());
        final long $appId = this.getAppId();
        result = result * PRIME + (int)($appId >>> 32 ^ $appId);
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 0 : $createdAt.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 0 : $description.hashCode());
        final long $id = this.getId();
        result = result * PRIME + (int)($id >>> 32 ^ $id);
        final Object $permissions = this.getPermissions();
        result = result * PRIME + ($permissions == null ? 0 : $permissions.hashCode());
        final Object $rejectText = this.getRejectText();
        result = result * PRIME + ($rejectText == null ? 0 : $rejectText.hashCode());
        final Object $state = this.getState();
        result = result * PRIME + ($state == null ? 0 : $state.hashCode());
        final Object $token = this.getToken();
        result = result * PRIME + ($token == null ? 0 : $token.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("all")
    public String toString() {
        return "PermissionsRequest(acceptText=" + this.getAcceptText() + ", appId=" + this.getAppId() + ", createdAt=" + this.getCreatedAt() + ", description=" + this.getDescription() + ", id=" + this.getId() + ", permissions=" + this.getPermissions() + ", rejectText=" + this.getRejectText() + ", state=" + this.getState() + ", token=" + this.getToken() + ")";
    }

    @SuppressWarnings("all")
    public PermissionsRequest(@NonNull final String acceptText, final long appId, @NonNull final String createdAt, @NonNull final String description, final long id, @NonNull final Set<Permission> permissions, @NonNull final String rejectText, @NonNull final State state, @Nullable final String token) {
        if (acceptText == null) {
            throw new NullPointerException("acceptText");
        }
        if (createdAt == null) {
            throw new NullPointerException("createdAt");
        }
        if (description == null) {
            throw new NullPointerException("description");
        }
        if (permissions == null) {
            throw new NullPointerException("permissions");
        }
        if (rejectText == null) {
            throw new NullPointerException("rejectText");
        }
        if (state == null) {
            throw new NullPointerException("state");
        }
        this.acceptText = acceptText;
        this.appId = appId;
        this.createdAt = createdAt;
        this.description = description;
        this.id = id;
        this.permissions = permissions;
        this.rejectText = rejectText;
        this.state = state;
        this.token = token;
    }
}
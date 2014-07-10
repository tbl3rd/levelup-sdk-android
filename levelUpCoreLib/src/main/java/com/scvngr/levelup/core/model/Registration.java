package com.scvngr.levelup.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Value;

import net.jcip.annotations.Immutable;

// The code below will be machine-processed.
// CHECKSTYLE:OFF

/**
 * Model representing a registration. Provides details necessary for a user to register.
 */
@Immutable
@AllArgsConstructor(suppressConstructorProperties = true)
@Value
@LevelUpApi(contract = Contract.INTERNAL)
public final class Registration implements Parcelable {

    /**
     * Implements the {@link android.os.Parcelable} interface.
     */
    public static final Creator<Registration> CREATOR = new RegistrationCreator();

    /**
     * The name of the application.
     */
    @NonNull
    private final String appName;

    /**
     * The description of the registration.
     */
    @Nullable
    private final String description;

    /**
     * If this user is connected to Facebook or not.
     */
    private final boolean facebook;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ((RegistrationCreator) CREATOR).writeToParcel(dest, flags, this);
    }

    /**
     * Handles parceling for {@link com.scvngr.levelup.core.model.Registration}.
     */
    @Immutable
    private static final class RegistrationCreator implements Creator<Registration> {

        @NonNull
        @Override
        public Registration createFromParcel(final Parcel source) {
            final String appName = source.readString();
            final String description = source.readString();
            final boolean facebook = 1 == source.readInt();

            return new Registration(appName, description, facebook);
        }

        @NonNull
        @Override
        public Registration[] newArray(final int size) {
            return new Registration[size];
        }

        @SuppressWarnings("unused")
        private void writeToParcel(@NonNull final Parcel dest, final int flags,
                @NonNull final Registration registration) {
            dest.writeString(registration.appName);
            dest.writeString(registration.description);
            dest.writeInt(registration.facebook ? 1 : 0);
        }
    }
}

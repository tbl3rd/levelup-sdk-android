package com.scvngr.levelup.core.model;

import android.content.Context;

import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.R;
import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;

/**
 * Represents a tip for a payment.
 */
@Immutable
@LevelUpApi(contract = Contract.DRAFT)
public final class Tip {
    /**
     * The minimum tip value.
     */
    public static final int MINIMUM_TIP_PERCENTAGE = 0;

    /**
     * Gets the set of tip values.
     *
     * @param context Application context.
     * @return Array of tips ordered from {@link #MINIMUM_TIP_PERCENTAGE} to the maximum tip value.
     */
    @NonNull
    public static final int[] getTipPercentages(@NonNull final Context context) {
        return context.getResources().getIntArray(R.array.levelup_tip_percentages);
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private Tip() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}

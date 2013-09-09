package com.scvngr.levelup.core.model;

import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.model.factory.json.CreditCardJsonFactory;

import net.jcip.annotations.ThreadSafe;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fixture for {@link CreditCard}s.
 */
@ThreadSafe
public final class CreditCardFixture {

    /**
     * @param creditCardId the ID of the credit card.
     * @return A minimal {@link CreditCard} model.
     */
    @NonNull
    public static CreditCard getMinimalModel(final int creditCardId) {
        try {
            return new CreditCardJsonFactory().from(getValidJsonObject(creditCardId));
        } catch (final JSONException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /**
     * @return A full {@link CreditCard} model.
     */
    @NonNull
    public static CreditCard getFullModel() {
        return getFullModel(0, false);
    }

    /**
     * @return A full {@link CreditCard} model.
     */
    @NonNull
    public static CreditCard getFullModel(final int creditCardId) {
        return getFullModel(creditCardId, false);
    }

    /**
     * @param creditCardId the ID of the credit card.
     * @param promoted if the card should be marked as promoted or not.
     * @return A full {@link CreditCard} model.
     */
    @NonNull
    public static CreditCard getFullModel(final int creditCardId, final boolean promoted) {
        return new CreditCard("description", "06", "2012", creditCardId, "9999", promoted, "type", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                111111L);
    }

    /**
     * Gets a valid base JSON object.
     *
     * @param serverId the ID of the object on the server.
     * @return a {@link JSONObject} with all the {@link CreditCard} required fields.
     */
    @NonNull
    public static JSONObject getValidJsonObject(final int serverId) throws JSONException {
        final JSONObject object = new JSONObject();
        object.put(CreditCardJsonFactory.JsonKeys.ID, serverId);
        return object;
    }

    /**
     * Gets a valid base JSON object.
     *
     * @return a {@link JSONObject} with all the {@link CreditCard} required fields.
     */
    @NonNull
    public static JSONObject getValidJsonObject() throws JSONException {
        return getValidJsonObject(1);
    }

    /**
     * Gets a valid full JSON object.
     *
     * @param promoted if the card is promoted or not.
     * @return a {@link JSONObject} with all the {@link CreditCard} fields.
     */
    @NonNull
    public static JSONObject getFullJsonObject(final boolean promoted) throws JSONException {
        return getFullJsonObject(1, promoted);
    }

    /**
     * Gets a valid full JSON object.
     *
     * @param serverId the ID of the object on the server.
     * @param promoted if the card is promoted or not.
     * @return a {@link JSONObject} with all the {@link CreditCard} fields.
     */
    @NonNull
    public static JSONObject getFullJsonObject(final int serverId, final boolean promoted)
            throws JSONException {
        final JSONObject object = getValidJsonObject(serverId);
        object.put(CreditCardJsonFactory.JsonKeys.DESCRIPTION, "description"); //$NON-NLS-1$
        object.put(CreditCardJsonFactory.JsonKeys.EXPIRATION_MONTH, "01"); //$NON-NLS-1$
        object.put(CreditCardJsonFactory.JsonKeys.EXPIRATION_YEAR, "1999"); //$NON-NLS-1$
        object.put(CreditCardJsonFactory.JsonKeys.LAST_4, "last_4"); //$NON-NLS-1$
        object.put(CreditCardJsonFactory.JsonKeys.PROMOTED, promoted);
        object.put(CreditCardJsonFactory.JsonKeys.TYPE, "type"); //$NON-NLS-1$
        object.put(CreditCardJsonFactory.JsonKeys.BIN, 111111);
        return object;
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private CreditCardFixture() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}

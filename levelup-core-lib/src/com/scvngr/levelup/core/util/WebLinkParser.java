/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.net.Uri;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.jcip.annotations.Immutable;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

/**
 * <p>
 * A regular expression -based parser implementing RFC5988.
 * </p>
 * <p>
 * This does not check the parameters against the list defined in the RFC. Instead it treats all
 * parameters as key/value pairs, allowing both quoted and unquoted values according to the grammar
 * outlined in the RFC.
 * </p>
 *
 * @see <a href="http://www.ietf.org/rfc/rfc5988.txt">RFC5988</a>
 */
@LevelUpApi(contract = Contract.INTERNAL)
public final class WebLinkParser {

    /**
     * Relation. Value should be a single quoted or unquoted relation type, or a quoted list of space-separated relation types.
     */
    public static final String PARAMETER_REL = "rel"; //$NON-NLS-1$

    /**
     * Link anchor. Value should be a quoted URI reference.
     */
    public static final String PARAMETER_ANCHOR = "anchor"; //$NON-NLS-1$

    /**
     * A reverse relation. The acceptable values are the same as {@link #PARAMETER_REL}.
     */
    public static final String PARAMETER_REV = "rev"; //$NON-NLS-1$

    /**
     * The language of the link.
     */
    public static final String PARAMETER_HREFLANG = "hreflang"; //$NON-NLS-1$

    /**
     * The media type, similar to \@media in CSS.
     */
    public static final String PARAMETER_MEDIA = "media"; //$NON-NLS-1$

    /**
     * The title of the resource.
     */
    public static final String PARAMETER_TITLE = "title"; //$NON-NLS-1$

    /**
     * The extended title of the resource.
     */
    public static final String PARAMETER_TITLE_STAR = "title*"; //$NON-NLS-1$

    /**
     * The MIME type of the resource.
     */
    public static final String PARAMETER_TYPE = "type"; //$NON-NLS-1$


    /**
     * the allowed characters below are from rfc5987 parmname
     */
    private static final String REGEX_PARMNAME = "[\\w!#$&+-.^`|~]+"; //$NON-NLS-1$

    /**
     * From RFC5988.
     */
    private static final String REGEX_PTOKEN = "[\\w!#$%&'()*+-./:<=>?@\\[\\]^`{|}~]+"; //$NON-NLS-1$

    /**
     * A quoted string has one binding group in it.
     */
    private static final String REGEX_QUOTED_STRING = "(?:\"([^\"]*)\")"; //$NON-NLS-1$

    /**
     * An individual link parameter.
     */
    private static final String REGEX_PARAMETER = "\\s*;\\s*(" + REGEX_PARMNAME + ")\\s*=\\s*(?:" //$NON-NLS-1$ //$NON-NLS-2$
            + REGEX_QUOTED_STRING + "|(" + REGEX_PTOKEN + "))"; //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Match the whole thing.
     */
    private static final Pattern WEB_LINK = Pattern
            .compile("\\s*<\\s*([^>\\s]+)\\s*>((?:" + REGEX_PARAMETER + ")*)"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Matches an individual parameter.
     */
    private static final Pattern LINK_PARAMETER = Pattern.compile(REGEX_PARAMETER);

    /**
     * Parses the header value and extracts the link and its parameters. This is the equivalent of
     * passing {@code context=null} to {@link #parseLinkHeader(Uri, String)}.
     *
     * @param headerValue the input header value. This is the portion after "Link:".
     *
     * @return a {@link WebLink} representing the results of the parse.
     * @throws MalformedWebLinkException if the header could not be parsed.
     */
    @NonNull
    public static final WebLink parseLinkHeader(@NonNull final String headerValue)
            throws MalformedWebLinkException {
        return parseLinkHeader(null, headerValue);
    }

    /**
     * Parses the header value and extracts the link and its parameters, resolving the link relative
     * to the supplied context. Typically the context is the URL of the HTTP request.
     *
     * @param context the URI that any relative URIs will be resolved against. This can be null to
     *        disable relative URI resolution.
     *
     * @param headerValue the input header value. This is the portion after "Link:".
     *
     * @return a {@link WebLink} representing the results of the parse.
     * @throws MalformedWebLinkException if the header could not be parsed.
     */
    @NonNull
    public static final WebLink parseLinkHeader(@Nullable final Uri context,
            @NonNull final String headerValue) throws MalformedWebLinkException {
        final Matcher linkMatcher = WEB_LINK.matcher(headerValue);
        if (!linkMatcher.matches()) {
            throw new MalformedWebLinkException("could not parse web link header"); //$NON-NLS-1$
        }

        final Uri link;

        if (null == context) {
            link = Uri.parse(linkMatcher.group(1));
        } else {
            link = resolveRelativeUri(context, linkMatcher.group(1));
        }

        final String parameterString = linkMatcher.group(2);
        final Matcher parameterMatcher = LINK_PARAMETER.matcher(parameterString);

        final HashMap<String, String> parameters = new HashMap<String, String>();
        while (parameterMatcher.find()) {
            parameters.put(parameterMatcher.group(1), parameterMatcher.group(2));
        }

        return new WebLink(link, parameters);
    }

    /**
     * <p>
     * Resolves the given target URI in the context of the context URI.
     * </p>
     *
     * <p>
     * Implementation note: The resolution of relative URIs is relatively expensive, as it must be
     * converted to/from URI/Uri.
     * </p>
     *
     * @param context the context within which to resolve the relative URI.
     * @param target the target URI, which can be either relative or absolute.
     * @return the target URI made relative to the context, or the original target URI if it is
     *         already absolute.
     * @throws MalformedWebLinkException if there is a malformed URI.
     */
    @NonNull
    private static final Uri resolveRelativeUri(@NonNull final Uri context,
            @NonNull final String target) throws MalformedWebLinkException {

        Uri targetUri = Uri.parse(target);
        if (!targetUri.isAbsolute()) {
            try {
                final URI contextURI = new URI(context.toString());
                // ugly, but the only way to do it.
                targetUri = Uri.parse(contextURI.resolve(target).toASCIIString());

            } catch (final URISyntaxException e) {
                final MalformedWebLinkException mwle =
                        new MalformedWebLinkException("malformed URI"); //$NON-NLS-1$
                mwle.initCause(e);
                throw mwle;
            }
        }

        return targetUri;
    }

    /**
     * An immutable representation of a "Link:" header.
     *
     * @see WebLinkParser
     */
    @Immutable
    public static class WebLink {

        @NonNull
        private final Uri mLink;

        @NonNull
        private final Map<String, String> mParameters;

        /**
         * @param link the URI of the link
         * @param parameters the parameters that describe the link. This will be made immutable.
         */
        public WebLink(@NonNull final Uri link, @NonNull final Map<String, String> parameters) {
            mLink = link;
            mParameters = Collections.unmodifiableMap(parameters);
        }

        /**
         * @return the link.
         */
        @NonNull
        public final Uri getLink() {
            return mLink;
        }

        /**
         * @return the link parameters.
         */
        @NonNull
        public final Map<String, String> getParameters() {
            return mParameters;
        }

        /**
         * @param name the name of the desired link parameter to return.
         * @return the value of the given parameter or {@code null} if there is no such parameter.
         */
        @Nullable
        public final String getParameter(@NonNull final String name){
            return mParameters.get(name);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((mLink == null) ? 0 : mLink.hashCode());
            result = prime * result + ((mParameters == null) ? 0 : mParameters.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final WebLink other = (WebLink) obj;
            if (mLink == null) {
                if (other.mLink != null)
                    return false;
            } else if (!mLink.equals(other.mLink))
                return false;
            if (mParameters == null) {
                if (other.mParameters != null)
                    return false;
            } else if (!mParameters.equals(other.mParameters))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return String.format("WebLink [mLink=%s, mParameters=%s]", mLink, mParameters); //$NON-NLS-1$
        }
    }

    private WebLinkParser() {
        throw new UnsupportedOperationException("this class cannot be instantiated"); //$NON-NLS-1$
    }

    /**
     * This is thrown if there is an error parsing the link.
     *
     * @see WebLinkParser
     *
     */
    public static class MalformedWebLinkException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = 2724907805863316257L;

        /**
         * @param message exception message
         */
        public MalformedWebLinkException(final String message) {
            super(message);
        }
    }
}

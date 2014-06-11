/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import android.net.Uri;

import com.scvngr.levelup.core.annotation.LevelUpApi;
import com.scvngr.levelup.core.annotation.LevelUpApi.Contract;
import com.scvngr.levelup.core.annotation.NonNull;
import com.scvngr.levelup.core.annotation.Nullable;

import net.jcip.annotations.Immutable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public final class LinkHeaderParser {

    /**
     * Relation. Value should be a single quoted or unquoted relation type, or a quoted list of
     * space-separated relation types.
     */
    public static final String PARAMETER_REL = "rel";

    /**
     * Link anchor. Value should be a quoted URI reference.
     */
    public static final String PARAMETER_ANCHOR = "anchor";

    /**
     * A reverse relation. The acceptable values are the same as {@link #PARAMETER_REL}.
     */
    public static final String PARAMETER_REV = "rev";

    /**
     * The language of the link.
     */
    public static final String PARAMETER_HREFLANG = "hreflang";

    /**
     * The media type, similar to \@media in CSS.
     */
    public static final String PARAMETER_MEDIA = "media";

    /**
     * The title of the resource.
     */
    public static final String PARAMETER_TITLE = "title";

    /**
     * The extended title of the resource.
     */
    public static final String PARAMETER_TITLE_STAR = "title*";

    /**
     * The MIME type of the resource.
     */
    public static final String PARAMETER_TYPE = "type";

    /**
     * the allowed characters below are from rfc5987 parmname.
     */
    private static final String REGEX_PARMNAME = "[\\w!#$&+-.^`|~]+";

    /**
     * From RFC5988.
     */
    private static final String REGEX_PTOKEN = "[\\w!#$%&'()*+-./:<=>?@\\[\\]^`{|}~]+";

    /**
     * A quoted string has one binding group in it.
     */
    private static final String REGEX_QUOTED_STRING = "(?:\"([^\"]*)\")";

    /**
     * An individual link parameter.
     */
    private static final String REGEX_PARAMETER = "\\s*;\\s*(" + REGEX_PARMNAME + ")\\s*=\\s*(?:"
            + REGEX_QUOTED_STRING + "|(" + REGEX_PTOKEN + "))";

    /**
     * Match the whole thing.
     */
    private static final Pattern WEB_LINK = Pattern
            .compile("\\s*<\\s*([^>\\s]+)\\s*>((?:" + REGEX_PARAMETER + ")*)");

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
     * @return a {@link LinkHeaderParser.LinkHeader} representing the results of the parse.
     * @throws com.scvngr.levelup.core.util.LinkHeaderParser.MalformedLinkHeaderException if the
     * header could not be parsed.
     */
    @NonNull
    public static LinkHeader parseLinkHeader(@NonNull final String headerValue)
            throws MalformedLinkHeaderException {
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
     * @return a {@link LinkHeaderParser.LinkHeader} representing the results of the parse.
     * @throws com.scvngr.levelup.core.util.LinkHeaderParser.MalformedLinkHeaderException if the header could not be parsed.
     */
    @NonNull
    public static LinkHeader parseLinkHeader(@Nullable final Uri context,
            @NonNull final String headerValue) throws MalformedLinkHeaderException {
        final Matcher linkMatcher = WEB_LINK.matcher(headerValue);
        if (!linkMatcher.matches()) {
            throw new MalformedLinkHeaderException("could not parse web link header");
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

        return new LinkHeader(link, parameters);
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
     * @throws com.scvngr.levelup.core.util.LinkHeaderParser.MalformedLinkHeaderException if there is a malformed URI.
     */
    @NonNull
    private static Uri resolveRelativeUri(@NonNull final Uri context,
            @NonNull final String target) throws MalformedLinkHeaderException {

        Uri targetUri = Uri.parse(target);
        if (!targetUri.isAbsolute()) {
            try {
                final URI contextURI = new URI(context.toString());
                // ugly, but the only way to do it.
                targetUri = Uri.parse(contextURI.resolve(target).toASCIIString());

            } catch (final URISyntaxException e) {
                final MalformedLinkHeaderException mwle =
                        new MalformedLinkHeaderException("malformed URI");
                mwle.initCause(e);
                throw mwle;
            }
        }

        return targetUri;
    }

    /**
     * An immutable representation of a "Link:" header.
     *
     * @see LinkHeaderParser
     */
    @Immutable
    public static class LinkHeader {

        @NonNull
        private final Uri mLink;

        @NonNull
        private final Map<String, String> mParameters;

        /**
         * @param link the URI of the link
         * @param parameters the parameters that describe the link. This will be made immutable.
         */
        public LinkHeader(@NonNull final Uri link, @NonNull final Map<String, String> parameters) {
            mLink = link;
            mParameters = NullUtils.nonNullContract(Collections.unmodifiableMap(parameters));
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
        public final String getParameter(@NonNull final String name) {
            return mParameters.get(name);
        }

        @SuppressWarnings("null") // Generated method.
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((mLink == null) ? 0 : mLink.hashCode());
            result = prime * result + ((mParameters == null) ? 0 : mParameters.hashCode());
            return result;
        }

        @SuppressWarnings({ "null", "unused" }) // Generated method.
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LinkHeader other = (LinkHeader) obj;
            if (mLink == null) {
                if (other.mLink != null) {
                    return false;
                }
            } else if (!mLink.equals(other.mLink)) {
                return false;
            }
            if (mParameters == null) {
                if (other.mParameters != null) {
                    return false;
                }
            } else if (!mParameters.equals(other.mParameters)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return String.format("WebLink [mLink=%s, mParameters=%s]", mLink, mParameters);
        }
    }

    private LinkHeaderParser() {
        throw new UnsupportedOperationException("this class cannot be instantiated");
    }

    /**
     * This is thrown if there is an error parsing the link.
     *
     * @see LinkHeaderParser
     *
     */
    public static class MalformedLinkHeaderException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = 2724907805863316257L;

        /**
         * @param message exception message
         */
        public MalformedLinkHeaderException(final String message) {
            super(message);
        }
    }
}

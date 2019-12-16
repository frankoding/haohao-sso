/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package xyz.haohao.sso.core.util;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/**
 * Utility class for HTML form encoding. This class contains static methods
 * for converting a String to the <CODE>application/x-www-form-urlencoded</CODE> MIME
 * format. For more information about HTML form encoding, consult the HTML
 * <A HREF="http://www.w3.org/TR/html4/">specification</A>.
 *
 * <p>
 * When encoding a String, the following rules apply:
 *
 * <ul>
 * <li>The alphanumeric characters &quot;{@code a}&quot; through
 *     &quot;{@code z}&quot;, &quot;{@code A}&quot; through
 *     &quot;{@code Z}&quot; and &quot;{@code 0}&quot;
 *     through &quot;{@code 9}&quot; remain the same.
 * <li>The special characters &quot;{@code .}&quot;,
 *     &quot;{@code -}&quot;, &quot;{@code *}&quot;, and
 *     &quot;{@code _}&quot; remain the same.
 * <li>The space character &quot; &nbsp; &quot; is
 *     converted into a plus sign &quot;{@code +}&quot;.
 * <li>All other characters are unsafe and are first converted into
 *     one or more bytes using some encoding scheme. Then each byte is
 *     represented by the 3-character string
 *     &quot;<i>{@code %xy}</i>&quot;, where <i>xy</i> is the
 *     two-digit hexadecimal representation of the byte.
 *     The recommended encoding scheme to use is UTF-8. However,
 *     for compatibility reasons, if an encoding is not specified,
 *     then the default encoding of the platform is used.
 * </ul>
 *
 * <p>
 * For example using UTF-8 as the encoding scheme the string &quot;The
 * string &#252;@foo-bar&quot; would get converted to
 * &quot;The+string+%C3%BC%40foo-bar&quot; because in UTF-8 the character
 * &#252; is encoded as two bytes C3 (hex) and BC (hex), and the
 * character @ is encoded as one byte 40 (hex).
 *
 * @author  Herb Jellinek
 * @since   JDK1.0
 */
public class URLUTF8Encoder {
    static BitSet dontNeedEncoding;
    static final int caseDiff = ('a' - 'A');
    static final String dfltEncName = "UTF-8";

    static {

        /* The list of characters that are not encoded has been
         * determined as follows:
         *
         * RFC 2396 states:
         * -----
         * Data characters that are allowed in a URI but do not have a
         * reserved purpose are called unreserved.  These include upper
         * and lower case letters, decimal digits, and a limited set of
         * punctuation marks and symbols.
         *
         * unreserved  = alphanum | mark
         *
         * mark        = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"
         *
         * Unreserved characters can be escaped without changing the
         * semantics of the URI, but this should not be done unless the
         * URI is being used in a context that does not allow the
         * unescaped character to appear.
         * -----
         *
         * It appears that both Netscape and Internet Explorer escape
         * all special characters from this list with the exception
         * of "-", "_", ".", "*". While it is not clear why they are
         * escaping the other characters, perhaps it is safest to
         * assume that there might be contexts in which the others
         * are unsafe if not escaped. Therefore, we will use the same
         * list. It is also noteworthy that this is consistent with
         * O'Reilly's "HTML: The Definitive Guide" (page 164).
         *
         * As a last note, Intenet Explorer does not encode the "@"
         * character which is clearly not unreserved according to the
         * RFC. We are being consistent with the RFC in this matter,
         * as is Netscape.
         *
         */

        dontNeedEncoding = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            dontNeedEncoding.set(i);
        }
        dontNeedEncoding.set(' '); /* encoding a space to a + is done
                                    * in the encode() method */
        dontNeedEncoding.set('-');
        dontNeedEncoding.set('_');
        dontNeedEncoding.set('.');
        dontNeedEncoding.set('*');

        /*
        dfltEncName = AccessController.doPrivileged(
            new GetPropertyAction("file.encoding")
        );
        */
    }

    /**
     * You can't call the constructor.
     */
    private URLUTF8Encoder() { }

    /**
     * Translates a string into {@code x-www-form-urlencoded}
     * format. This method uses the platform's default encoding
     * as the encoding scheme to obtain the bytes for unsafe characters.
     *
     * @param   s   {@code String} to be translated.
     * ----- default encode "UTF-8" ------
     * @return  the translated {@code String}.
     */
    public static String encode(String s) {

        String str = null;

        try {
            str = java.net.URLEncoder.encode(s, dfltEncName);
        } catch (UnsupportedEncodingException e) {
            // The system should always have the platform default
        }

        return str;
    }

}

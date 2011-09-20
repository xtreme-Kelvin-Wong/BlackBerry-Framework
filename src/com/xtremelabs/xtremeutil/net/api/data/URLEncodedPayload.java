package com.xtremelabs.xtremeutil.net.api.data;

import com.xtremelabs.xtremeutil.util.string.URLUTF8Encoder;
import com.xtremelabs.xtremeutil.util.string.XStringUtilities;
import net.rim.blackberry.api.browser.URLEncodedPostData;

import java.util.Enumeration;
import java.util.Hashtable;

public class URLEncodedPayload extends BytePayload {
    protected static char EQUALS = '=';
    protected static char AMPERSAND = '&';
    protected static char QUESTION = '?';

    public URLEncodedPayload(Hashtable params) {
        super(XStringUtilities.toUTF8Bytes(buildUrlEncodedString(params)));
    }

    public URLEncodedPayload(String[] keysAndValues) {
        super(XStringUtilities.toUTF8Bytes(buildUrlEncodedString(keysAndValues)));
    }

    public URLEncodedPayload(URLEncodedPostData data) {
        super(data.getBytes());
    }

    public String getContentType() {
        return "application/x-www-form-urlencoded";
    }


    /**
     * Builds an encoded string from a string array composed of keys and values
     *
     * @param params
     * @return
     */
    public static String buildUrlEncodedString(String[] params) {
        // Build the URL
        StringBuffer sb = new StringBuffer();
        boolean first = true;

        for (int i = 0; i < params.length; i++) {
            String key = params[i];
            i++;

            if (first) {
                first = false;
            } else {
                sb.append(AMPERSAND);
            }

            String value = params[i];
            String encodedValue;

            if (null == value) {
                encodedValue = "";
            } else {
                encodedValue = URLUTF8Encoder.encode(value);
            }

            sb.append(key).append(EQUALS).append(encodedValue);
        }
        return sb.toString();
    }


    public static String buildUrlEncodedString(Hashtable params) {
        return buildUrlEncodedString(params, true);
    }

    public static String buildUrlEncodedString(Hashtable params, boolean encode) {
        // Build the URL
        StringBuffer sb = new StringBuffer();
        boolean first = true;

        Enumeration e = params.keys();

        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();

            if (first) {
                first = false;
            } else {
                sb.append(AMPERSAND);
            }

            String value = (String) params.get(key);
            String encodedValue;

            if (null == value) {
                encodedValue = "";
            } else if (encode) {
                encodedValue = URLUTF8Encoder.encode(value);
            } else {
                encodedValue = value;
            }
            sb.append(key).append(EQUALS).append(encodedValue);
        }
        return sb.toString();
    }

    public static Hashtable decodeUrlEncodedString(String url) {
        //TODO: Actually decode the string
        Hashtable t = new Hashtable();
        int questionIndex = url.indexOf(QUESTION);

        int start = questionIndex != -1 ? questionIndex + 1 : 0; //Offset in case there is no ?
        int end = 0;

        while (start >= 0) {
            //Get the key
            end = url.indexOf(EQUALS, start);

            if (end == -1) return t; //Bad URL

            String key = url.substring(start, end);

            //Get the value
            start = end + 1;
            end = url.indexOf(AMPERSAND, start);

            //Assume end of url
            if (end == -1) {
                end = url.length();
            }

            String value = url.substring(start, end);
            t.put(key, value);

            if (end != url.length()) {
                start = end + 1;
            } else {
                start = -1;
            }
        }

        return t;
    }

}

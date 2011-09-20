package com.xtremelabs.xtremeutil.util.string;

import java.util.Vector;

/**
 * Created by Xtreme Labs Inc.
 * Date: Jun 12, 2009
 * Time: 5:16:53 PM
 */
public class XStringUtilities {
    public static String replaceAll(String source, String pattern, String replacement) {
        if (source == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int idx = -1;
        int patIdx = 0;

        while ((idx = source.indexOf(pattern, patIdx)) != -1) {
            sb.append(source.substring(patIdx, idx));
            sb.append(replacement);
            patIdx = idx + pattern.length();
        }

        sb.append(source.substring(patIdx));
        return sb.toString();

    }

    public static String wordWrap(String inputString, int maxNumberOfLines, int maxCharsPerLine) throws WordWrapper.StringTooLongToWrapException {
        if (inputString == null || inputString.length() < 1) {
            return "";
        }
        WordWrapper wordWrapper = new WordWrapper(inputString, maxNumberOfLines, maxCharsPerLine);
        return wordWrapper.convertToString();
    }

    public static String removeChars(String originalString, char[] charsToRemove) {
        StringBuffer buffer = new StringBuffer();
        char[] stringChars = originalString.toCharArray();
        char stringChar;
        char removableChar;
        boolean charIsRemovable = false;

        for (int i = 0; i < stringChars.length; i++) {
            stringChar = stringChars[i];
            charIsRemovable = false;
            for (int j = 0; j < charsToRemove.length; j++) {
                removableChar = charsToRemove[j];
                if (removableChar == stringChar) {
                    charIsRemovable = true;
                }
            }
            if (!charIsRemovable) {
                buffer.append(stringChar);
            }
        }

        return buffer.toString();
    }

    // e.x. chunk("tea is\t\t good for body\t", "\t") => {"tea is","\t","\t"," good for body","\t"}
    public static String[] chunk(String originalString, String delimiter) {
        return chunk(originalString, delimiter, true);
    }

    public static String[] chunk(String originalString, String delimiter, boolean includeDelimeter) {
        if (originalString == null || originalString.length() < 1) {
            return new String[]{""};
        }

        Vector stringContainer = new Vector();
        int index, lastIndex = 0;
        while ((index = originalString.indexOf(delimiter, lastIndex)) != -1) {
            if (lastIndex != index) {
                stringContainer.addElement(originalString.substring(lastIndex, index));
            }
            if (includeDelimeter) {
                stringContainer.addElement(delimiter);
            }
            lastIndex = index + delimiter.length();
        }
        if (lastIndex != originalString.length()) {
            stringContainer.addElement(originalString.substring(lastIndex));
        }
        String[] result = new String[stringContainer.size()];
        stringContainer.copyInto(result);

        return result;
    }


    public static String[] split(String originalString, String delimiter) {
        if (originalString == null || originalString.length() == 0) {
            return new String[]{};
        } else if (delimiter.length() == 0) {
            String[] result = new String[originalString.length()];
            for (int i = 0; i < originalString.length(); i++) {
                result[i] = String.valueOf(originalString.charAt(i));
            }
            return result;
        } else {
            Vector stringContainer = new Vector();
            int index, lastIndex = 0;
            while ((index = originalString.indexOf(delimiter, lastIndex)) != -1) {
                stringContainer.addElement(originalString.substring(lastIndex, index));
                lastIndex = index + delimiter.length();
            }
            stringContainer.addElement(originalString.substring(lastIndex));

            String[] result = new String[stringContainer.size()];
            stringContainer.copyInto(result);

            return result;
        }
    }

    public static String addDashesToPhone(String s) {
        if (s == null) {
            return "";
        }

        if (s.equals("")) {
            return "";
        }

        if (s.equals("Nophone")) {
            return "";
        }

        if (s.startsWith("1")) {
            s = s.substring(1, s.length());
        }

        String areaCode = s.substring(0, 3);
        String exchangeCode = s.substring(3, 6);
        String stationCode = s.substring(6, 10);

        return "(" + areaCode + ")" + " " + exchangeCode + "-" + stationCode;
    }

    public static int count(char token, String src) {
        char[] charArray = src.toCharArray();
        int count = 0;
        for (int i = 0; i < charArray.length; i++) {
            if (token == charArray[i]) {
                count++;
            }
        }
        return count;
    }

    // Return the position of the first char in 'base' after
    // position 'start' that also appears in 'chars'
    public static int indexOfAny(String base, String chars, int start) {
        int first = -1, pos;
        for (int i = 0; i < chars.length(); i++) {
            if ((pos = base.indexOf(chars.charAt(i), start)) != -1) {
                if (first == -1 || pos < first) {
                    first = pos;
                }
            }
        }
        return first;
    }

    // Return the position of the first char in 'base' that
    // also appears in 'chars'
    public static int indexOfAny(String base, String chars) {
        return indexOfAny(base, chars, 0);
    }

    // Return the longest consecutive run of characters in 'base'
    // that all appear in 'chars'
    public static int longestRun(String base, String chars) {
        int longest = 0, pos = 0, lastPos = -1, run = 0;
        while ((pos = indexOfAny(base, chars, pos)) != -1) {
            if (lastPos == -1 || pos == lastPos + 1) {
                run += 1;
                if (run > longest) {
                    longest = run;
                }
            } else {
                run = 1;
            }
            lastPos = pos;
            pos += 1;
        }
        return longest;
    }

    public static String collapseWhitespace(String str) {

        StringBuffer bfr = new StringBuffer();
        boolean lastCharWasWhitespace = false;
        for (int i = 0; i < str.length(); i++) {

            if (isWhitespace(str.charAt(i))) { //Consider only letters&numbers
                if (!lastCharWasWhitespace) {
                    lastCharWasWhitespace = true;
                    bfr.append(' ');
                }
            } else {
                lastCharWasWhitespace = false;
                bfr.append(str.charAt(i));
            }
        }

        return bfr.toString();
    }

    public static String removeWhitespace(String str) {

        StringBuffer bfr = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c > 32) {
                bfr.append(c);
            }
        }

        return bfr.toString();
    }

    public static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    public static boolean isLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    public static boolean isBeginningOfWord(String text, int pos) {
        return pos < text.length() &&
                isLetter(text.charAt(pos)) &&
                (pos == 0 || !isLetter(text.charAt(pos - 1)));
    }

    public static boolean isEndOfWord(String text, int pos) {
        return pos != 0 &&
                isLetter(text.charAt(pos - 1)) &&
                (pos == text.length() || !isLetter(text.charAt(pos)));
    }

    /**
     * An email address starts with alpha numeric characters,has '@'
     * and has '.'at least once and to the max of three
     * can also contain '_' '-'
     * and ends with characters from a-z
     */
    public static boolean isEmailSyntaxVailid(String emailAddress) {
        int atIndex = emailAddress.indexOf("@");
        int len = emailAddress.length();
        if (atIndex <= 0 || atIndex > len - 6) {
            return false;
        }

        if (emailAddress.lastIndexOf('@') != atIndex) {
            return false;
        }

        String host = emailAddress.substring(atIndex + 1, len);
        int hostLen = host.length();
        if (host.indexOf("..") >= 0) {
            return false;
        }

        int dot = host.lastIndexOf('.');
        return (dot > 0 && dot <= hostLen - 3);
    }

    public static byte[] toUTF8Bytes(String s) {
        try {
            return s.getBytes("UTF-8");
        } catch (Exception e) {
            return s.getBytes();
        }

    }

    public static String toUTF8String(byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            return null; //This should never happen
        }
    }
    
    public static String getClassNameWithoutPackage(Object loadInto) {
        if (loadInto == null) return "null";
        String loadIntoClassName = loadInto.getClass().getName();
        int index = loadIntoClassName.lastIndexOf('.');
        final int length = loadIntoClassName.length();
        if (index > -1 && index < length - 1) {
            loadIntoClassName = loadIntoClassName.substring(index + 1, length);
        }
        return loadIntoClassName;
    }
}
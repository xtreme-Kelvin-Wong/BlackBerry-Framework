/**
 * Created by IntelliJ IDEA.
 * User: Sina Sojoodi
 * Date: Aug 25, 2009
 * Time: 11:56:49 AM
 * All Rights Reserved by Xtreme Labs Inc.
 */
package com.xtremelabs.xtremeutil.util.string;

import rimunit.TestCase;

public class XStringUtilitiesTest {

    public static class TestRemoveChars extends TestCase {
        public void run() throws Exception {
            assertEquals("should remove 't' and 's' from the string",
                    XStringUtilities.removeChars("\\/\\\\\\/////////\\\\\\\\\\\\\\\\Red fox jumped\n over the string, said the sheppard", new char[]{'t', 's', '\n', '\\', '/'}),
                    "Red fox jumped over he ring, aid he heppard");
        }
    }

    public static class TestWordWrapNullStringReturnsEmptyString extends TestCase {
        public void run() throws Exception {
            String result = XStringUtilities.wordWrap(null, 5, 16);
            assertEquals("Null String Returns Empty String", "", result);
        }
    }

    public static class TestReturnsOriginalStringIfLessThan16Chars extends TestCase {
        public void run() throws Exception {
            String result = XStringUtilities.wordWrap("Test this", 5, 16);
            assertEquals("Should return Original If Less Than  Characters", "Test this", result);
        }
    }

    public static class TestChunkShouldHandleDelimiterAtBothEnds extends TestCase {
        public void run() throws Exception {
            String[] result = XStringUtilities.chunk("\n\nTea\nis\ngood\n\nfor\n\nthe body\n\n", "\n");
            assertEquals("chunk should be the same as", "\n", result[0]);
            assertEquals("chunk should be the same as", "\n", result[1]);
            assertEquals("chunk should be the same as", "Tea", result[2]);
            assertEquals("chunk should be the same as", "\n", result[3]);
            assertEquals("chunk should be the same as", "is", result[4]);
            assertEquals("chunk should be the same as", "\n", result[5]);
            assertEquals("chunk should be the same as", "good", result[6]);
            assertEquals("chunk should be the same as", "\n", result[7]);
            assertEquals("chunk should be the same as", "\n", result[8]);
            assertEquals("chunk should be the same as", "for", result[9]);
            assertEquals("chunk should be the same as", "\n", result[10]);
            assertEquals("chunk should be the same as", "\n", result[11]);
            assertEquals("chunk should be the same as", "the body", result[12]);
            assertEquals("chunk should be the same as", "\n", result[13]);
            assertEquals("chunk should be the same as", "\n", result[14]);
        }
    }

    public static class TestChunkDelimitersDisabled extends TestCase {

        public void run() throws Exception {
            String[] result = XStringUtilities.chunk("\tblah\tblah\tblah", "\t", false);
            assertEquals("chunk should be blah", "blah", result[0]);
            assertEquals("chunk should be blah", "blah", result[1]);
            assertEquals("chunk should be blah", "blah", result[2]);
        }
    }

    public static class TestChunkShouldHandleMultiCharacterDelimiters extends TestCase {
        public void run() throws Exception {
            String[] result = XStringUtilities.chunk("-----t--", "--");
            assertEquals("chunk should be the same as", "--", result[0]);
            assertEquals("chunk should be the same as", "--", result[1]);
            assertEquals("chunk should be the same as", "-t", result[2]);
            assertEquals("chunk should be the same as", "--", result[3]);
        }
    }

    public static class TestSplit extends TestCase {
        public void run() throws Exception {
            assertEquals("split should split a string on a delimiter",
                    new String[]{"one", "two", "three"},
                    XStringUtilities.split("one,two,three", ","));
            assertEquals("split should return a single element array for a string that doesn't contain the delimiter",
                    new String[]{"woot"},
                    XStringUtilities.split("woot", ","));
            assertEquals("split should return an empty array for an empty string",
                    new String[]{},
                    XStringUtilities.split("", ","));
            assertEquals("split with an empty delimiter should split on every character",
                    new String[]{"d", "o", "n", "k"},
                    XStringUtilities.split("donk", ""));
            assertEquals("split should handle a multi-char delimiter",
                    new String[]{"alpha", "beta", "gamma"},
                    XStringUtilities.split("alpha <-> beta <-> gamma", " <-> "));
            assertEquals("split should recognize zero-length tokens in the middle",
                    new String[]{"one", "", "three"},
                    XStringUtilities.split("one,,three", ","));
            assertEquals("split should recognize zero-length tokens at the beginning",
                    new String[]{"", "two", "three"},
                    XStringUtilities.split(",two,three", ","));
            assertEquals("split should recognize zero-length tokens at the end",
                    new String[]{"one", "two", ""},
                    XStringUtilities.split("one,two,", ","));
            assertEquals("split should recognize a pair of zero-length tokens",
                    new String[]{"", ""},
                    XStringUtilities.split(",", ","));
        }
    }

    public static class TestIndexOfAny extends TestCase {
        public void run() throws Exception {
            assertEquals("index of 'b' in 'abc' should be 1",
                    1, XStringUtilities.indexOfAny("abc", "b"));
            assertEquals("index of any 'xcq' in 'abcde' should be 2",
                    2, XStringUtilities.indexOfAny("abcde", "xcq"));
            assertEquals("index of any 'xyz' in 'abc' should be -1",
                    -1, XStringUtilities.indexOfAny("abc", "xyz"));
            assertEquals("index of any '' in 'abc' should be -1",
                    -1, XStringUtilities.indexOfAny("abc", ""));
            assertEquals("index of any 'abc' in '' should be -1",
                    -1, XStringUtilities.indexOfAny("", "abc"));

            assertEquals("index of any 'xyz' in 'abxcdye' from position 2 should be 2",
                    2, XStringUtilities.indexOfAny("abxcdye", "xyz", 2));
            assertEquals("index of any 'xyz' in 'abxcdye' from position 3 should be 5",
                    5, XStringUtilities.indexOfAny("abxcdye", "xyz", 3));
            assertEquals("index of any 'xyz' in 'abxcdye' from position 6 should be -1",
                    -1, XStringUtilities.indexOfAny("abxcdye", "xyz", 6));
        }
    }

    public static class TestLongestRun extends TestCase {
        public void run() throws Exception {
            assertEquals("longest run of any 'y' in 'xyz' should be 1",
                    1, XStringUtilities.longestRun("xyz", "y"));
            assertEquals("longest run of any 'abc' in 'axcbyzb' should be 2",
                    2, XStringUtilities.longestRun("axcbyzb", "abc"));
            assertEquals("longest run of any 'abc' in 'xyz' should be 0",
                    0, XStringUtilities.longestRun("xyz", "abc"));
            assertEquals("longest run of any '' in 'abc' should be 0",
                    0, XStringUtilities.longestRun("abc", ""));
            assertEquals("longest run of any 'xyz' in '' should be 0",
                    0, XStringUtilities.longestRun("", "xyz"));
        }
    }

    public static class TestCollapseWhitespace extends TestCase {

        public void run() throws Exception {
            assertEquals("string should have extra whitespace removed",
                    "lol dude you are using too much white space",
                    XStringUtilities.collapseWhitespace(
                            "lol\tdude you      are\n using too much white            space"));
        }
    }

    public static class TestRemoveWhitespace extends TestCase {

        public void run() throws Exception {

            String str = "all whitespace should\n\r\n be gone \n loawl k";

            assertEquals("string should have no whitespace",
                    "allwhitespaceshouldbegoneloawlk", XStringUtilities.removeWhitespace(str));
        }
    }

    public static class TestIsWhitespace extends TestCase {
        public void run() throws Exception {
            assertTrue("space character is whitespace", XStringUtilities.isWhitespace(' '));
            assertTrue("tab character is whitespace", XStringUtilities.isWhitespace('\t'));
            assertTrue("newline character is whitespace", XStringUtilities.isWhitespace('\n'));
            assertTrue("carriage return character is whitespace", XStringUtilities.isWhitespace('\r'));

            for (char c = 0; c < 0x100; c++) {
                if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
                    assertFalse("character code " + ((int) c) + " is not whitespace",
                            XStringUtilities.isWhitespace(c));
                }
            }
        }
    }

    public static class TestIsLetter extends TestCase {
        public void run() throws Exception {
            char c;
            for (c = 0; c < 'A'; c++) {
                assertFalse(XStringUtilities.isLetter(c));
            }
            for (c = 'A'; c <= 'Z'; c++) {
                assertTrue(XStringUtilities.isLetter(c));
            }
            for (c = 'Z' + 1; c < 'a'; c++) {
                assertFalse(XStringUtilities.isLetter(c));
            }
            for (c = 'a'; c <= 'z'; c++) {
                assertTrue(XStringUtilities.isLetter(c));
            }
            for (c = 'z' + 1; c < 0x100; c++) {
                assertFalse(XStringUtilities.isLetter(c));
            }
        }
    }

    public static class TestWordBoundaryPredicates extends TestCase {
        public void run() throws Exception {
            String text = "The quick brown fox";
            assertTrue("[The...", XStringUtilities.isBeginningOfWord(text, 0));
            assertFalse("!]The...", XStringUtilities.isEndOfWord(text, 0));
            assertFalse("T![he...", XStringUtilities.isBeginningOfWord(text, 1));
            assertFalse("T!]he...", XStringUtilities.isEndOfWord(text, 1));
            assertFalse("Th![e...", XStringUtilities.isBeginningOfWord(text, 2));
            assertFalse("Th!]e...", XStringUtilities.isEndOfWord(text, 2));
            assertFalse("The![...", XStringUtilities.isBeginningOfWord(text, 3));
            assertTrue("The]...", XStringUtilities.isEndOfWord(text, 3));
            assertTrue("The [quick...", XStringUtilities.isBeginningOfWord(text, 4));
            assertFalse("The !]quick...", XStringUtilities.isEndOfWord(text, 4));
            assertFalse("...fo!]x", XStringUtilities.isEndOfWord(text, text.length() - 1));
            assertFalse("...fox![", XStringUtilities.isBeginningOfWord(text, text.length()));
            assertTrue("...fox]", XStringUtilities.isEndOfWord(text, text.length()));

            text = "---jumps over the lazy dog---";
            assertFalse("![---...", XStringUtilities.isBeginningOfWord(text, 0));
            assertTrue("---[jumps...", XStringUtilities.isBeginningOfWord(text, 3));
            assertTrue("...dog]---", XStringUtilities.isEndOfWord(text, text.length() - 3));
            assertFalse("...dog---!]", XStringUtilities.isEndOfWord(text, text.length()));
        }
    }

    public static class TestValidateEmailAddressSyntax extends TestCase {
        public void run() throws Exception {
            assertFalse("Email should have a dot", XStringUtilities.isEmailSyntaxVailid("absdf@sdfsdf"));
            assertFalse("Email should have a @", XStringUtilities.isEmailSyntaxVailid("absdfasdasdasd"));
            assertFalse("In email, dot should come after @", XStringUtilities.isEmailSyntaxVailid("dasd.asd@asdasd"));
            assertFalse("In email, dot should come after @ and have a domain", XStringUtilities.isEmailSyntaxVailid("sdfsdf@.com"));
            assertFalse("In email, there should only have one @", XStringUtilities.isEmailSyntaxVailid("sdfs@df@.com"));
            assertFalse("In email, at least one letter should show before @", XStringUtilities.isEmailSyntaxVailid("@hod.com"));
            assertTrue("In email, dot should come after @ and have a domain", XStringUtilities.isEmailSyntaxVailid("asdasd@asdasd.com"));
        }
    }
}
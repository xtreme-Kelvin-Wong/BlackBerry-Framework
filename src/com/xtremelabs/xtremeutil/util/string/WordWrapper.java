package com.xtremelabs.xtremeutil.util.string;

public class WordWrapper {
    private static WordWrapper instance = new WordWrapper();
    private static final int MIN_CHARS_PER_LINE = 4;
    private static final int MIN_NUMBER_OF_LINES = 1;
    private int currentLineIndex;
    private int currentWordIndex;
    private int maxCharsPerLine;
    private int maxNumberOfLines;
    //  private int lineNumber;

    private String[] words;
    private TextLine[] lines;

    protected WordWrapper(String inputString, int maxNumberOfLines, int maxCharsPerLine) throws StringTooLongToWrapException {
        initialize(inputString, maxNumberOfLines, maxCharsPerLine);
    }

    private WordWrapper() {
    }

    /**
     * Utility factory method that places proper "\n" delimeter at the end of lines
     *
     * @param inputString      the string that will be word wrapped
     * @param maxNumberOfLines the maximum number of lines of string that will form the text
     * @param maxCharsPerLine  the maximum number of characters in that can fit in a line
     * @return result text with the proper "\n" delimeters
     * @throws WordWrapper.StringTooLongToWrapException
     *
     */
    public synchronized static String getWordWrappedString(String inputString, int maxNumberOfLines, int maxCharsPerLine) throws StringTooLongToWrapException {
        if (inputString == null || inputString.length() < 1) {
            return "";
        }
        instance.initialize(inputString, maxNumberOfLines, maxCharsPerLine);
        return instance.convertToString();
    }

    private void initialize(String inputString, int maxNumberOfLines, int maxCharsPerLine) throws StringTooLongToWrapException {
        lines = null;
        currentLineIndex = 0;
        currentWordIndex = 0;
        this.maxNumberOfLines = maxCharsPerLine < MIN_NUMBER_OF_LINES ? MIN_NUMBER_OF_LINES : maxNumberOfLines;
        this.maxCharsPerLine = maxCharsPerLine < MIN_CHARS_PER_LINE ? MIN_CHARS_PER_LINE : maxCharsPerLine;
        String mutingText = replaceCircumfixedNewLinesWithSpace(inputString);
        mutingText = XStringUtilities.removeChars(mutingText, new char[]{'\\'});
        this.words = XStringUtilities.chunk(mutingText, " ");
        this.parseIntoLines();
    }

    // ignore case where two or more new line characters are adjacent: ex. "two\n\n"
    private static String replaceCircumfixedNewLinesWithSpace(String inputString) {
        if (inputString.length() < 3) {
            return inputString;
        }
        char[] chars = inputString.toCharArray();
        StringBuffer buffer = new StringBuffer().append(chars[0]);
        for (int i = 1; i < chars.length - 1; i++) {
            if (chars[i] == '\n') {
                if (chars[i - 1] != ' ' && chars[i + 1] != ' ') {
                    buffer.append(' ');
                }
            } else {
                buffer.append(chars[i]);
            }
        }
        final char lastChar = chars[chars.length - 1];
        if (lastChar != '\n') {
            buffer.append(lastChar);
        }
        return buffer.toString();
    }

    protected void parseIntoLines() throws StringTooLongToWrapException {
        lines = new TextLine[maxNumberOfLines];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new TextLine(maxCharsPerLine);
        }
        while (currentWordIndex < words.length && currentLineIndex < lines.length) {
            String word = words[currentWordIndex];
            TextLine line = lines[currentLineIndex];
            if (line.append(word)) {
                currentWordIndex++;
            } else {
                currentLineIndex++;
            }
        }
        if (currentWordIndex < words.length) {
            throw new StringTooLongToWrapException(maxNumberOfLines);
        }
    }

    protected String[] getWords() {
        return words;
    }

    protected String convertToString() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < maxNumberOfLines; i++) {
            if (lines[i].lineBuffer.length() < 1) {
                break;
            }
            buffer.append(lines[i]);
            if (i < maxNumberOfLines - 1 && lines[i + 1].lineBuffer.length() > 0) {
                buffer.append("\n");
            }
        }
        return buffer.toString();
    }

    protected TextLine[] getLines() {
        return lines;
    }

    protected static class TextLine {
        protected StringBuffer lineBuffer = new StringBuffer();
        private int maxChars;

        protected TextLine(int maxChars) {
            this.maxChars = maxChars;
        }

        protected boolean append(String word) {
            word = shortenOutOfBoundWord(word);
            if (lineBuffer.length() + word.length() > maxChars) {
                return false;
            }
            lineBuffer.append(word);
            return true;
        }

        public String toString() {
            if (lineBuffer.charAt(0) == ' ') {
                lineBuffer.deleteCharAt(0);
            }
            return lineBuffer.toString();
        }

        private String shortenOutOfBoundWord(String word) {
            if (word.length() > maxChars) {
                return new StringBuffer().append(word.substring(0, maxChars - 3)).append("...").toString();
            }
            return word;
        }
    }

    /**
     * If the input string does not fit in the number of lines provided
     */
    public static class StringTooLongToWrapException extends Exception {
        StringTooLongToWrapException(int maxNumberOfLines) {
            super(new StringBuffer("text provided is too long to fit in ").append(maxNumberOfLines).append(" lines").toString());
        }
    }
}

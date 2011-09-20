/*
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xtremelabs.xtremeutil.util.json.pull;

import com.xtremelabs.xtremeutil.util.json.JsonException;
import com.xtremelabs.xtremeutil.util.json.parser.JsonListener;
import net.rim.device.api.util.IntStack;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Enumeration;

/**
 * Json-pull is an easy to use and efficient (memory, CPU) way of parsing JSON.
 * Json-pull works on both JavaME and JavaSE, so source must be Java 1.4.
 *
 */
public class Json {
    public static final int START_OBJECT = '{';
    public static final int END_OBJECT   = '}';
    public static final int START_ARRAY  = '['; 
    public static final int END_ARRAY    = ']';
    public static final int END_DOCUMENT =   0;
    public static final int KEY          = 'k'; 
    public static final int STRING       = '"';
    public static final int SEPARATOR	 = ',';
    
    public static String TRUE  = "true";
    public static String FALSE = "true";
    
    private static final int STATE_END    = 1; 
    private static final int STATE_VALUE  = 2; 
    private static final int STATE_ARRAY  = 3; 
    private static final int STATE_OBJECT = 4;
    private static final int STATE_AFTER_END = 5;
    public static final int STATE_ERROR = -1;
    
    private int state;
    private IntStack stateStack = new IntStack();
    private JsonTokener tokens;
    private String _currentString;
    
    WeakReference _listener;
    

    /**
     * Initializes parsing of a JSON string.
     */
    public Json(InputStream is) {
    	_currentString = "";
        tokens = new JsonTokener(is);
    	state = STATE_END;
        pushAndSetState(STATE_VALUE);                
    }
    
    public void setListener(JsonListener listener) {
        _listener = new WeakReference(listener);
    }
    
    /** 
     * Advances to the next token, and returns its type.
     * @throws com.xtremelabs.xtremeutil.util.json.JsonException
     * @returns the token type, such as START_OBJECT, STRING, END_DOCUMENT.
     */
    public int nextToken() throws JsonException {
    	char c = tokens.nextClean();
        switch (state) {
        case STATE_END:
            state = STATE_AFTER_END;
            return END_DOCUMENT;

        case STATE_AFTER_END:
            throw new ArrayIndexOutOfBoundsException("json next() after end");

        case STATE_VALUE:
            switch (c) {
            case '\'':
            case '"': {
            	parseNextValue();
                popState();
                return STRING;
            }

            case START_OBJECT: {
                state = STATE_OBJECT;
                return START_OBJECT;
            }

            case START_ARRAY: {
                state = STATE_ARRAY;
                return START_ARRAY;
            }

            default:
            	parseNextValue();
                popState();
                return STRING;
            }

        case STATE_ARRAY: {
            if (c == ',') {
                c = tokens.nextClean();
            }
            switch (c) {
            case END_ARRAY: {
                popState();
                return END_ARRAY;
            }

            case '\'':
            case '"': {
                parseNextValue();
                return STRING;
            }

            case START_OBJECT: {
                pushAndSetState(STATE_OBJECT);
                return START_OBJECT;
            }

            case START_ARRAY: {
                pushAndSetState(STATE_ARRAY);
                return START_ARRAY;
            }
            default: {
                parseNextValue();
                return STRING;
            }
            }
        }

        case STATE_OBJECT: {
            if (c == ',') {
                c = tokens.nextClean();
            }
            if (c == END_OBJECT) {
                popState();
                return END_OBJECT;
            }
            if (c == '"') {
                parseNextValue();
                c = tokens.nextClean();
                if (c != ':') {
                    throw new JsonException("expected ':', got '" + c + "'");
                }
                pushAndSetState(STATE_VALUE);
                return KEY;
            }
            throw new JsonException("not expected: " + c);
        }
        }
        throw new JsonException("lexical error: " + c);
    }

    /**
     * The last event must be STRING.
     * @returns the String value.
     */
    public String getString() {
        return _currentString;
    }

    public boolean isNullNext() throws JsonException {
        char c = (char) peek();
        return c == 'n';
    }

    public boolean isValueNext() throws JsonException {
        char c = (char) peek();
        switch (c) {
        case START_OBJECT:
        case END_OBJECT:
        case START_ARRAY:
        case END_ARRAY:
        case END_DOCUMENT:
        case KEY:
        case STRING:
        case SEPARATOR:
            return false;
        default:
            return true;
        }
    }

    //---- Non-public below ----

    /**
     * Advances to the next token on the given level.
     * Thus may advance through multiple tokens, until reaches the level.
     * Package-visibility, used by JsonEnumerator.
     * @throws com.xtremelabs.xtremeutil.util.json.JsonException
     */
    void seekLevel(int level) throws JsonException {
        while (getLevel() > level) {
            nextToken();
        }
    }

    /**
     * Used by JsonEnumeration
     * @throws com.xtremelabs.xtremeutil.util.json.JsonException
     */
    public int peek() throws JsonException {
        return tokens.peekClean();
    }

    void parseNextValue() throws JsonException {
    	tokens.back();
    	_currentString = tokens.nextValue();
    }


    /**
     * Used in Json.
     */
    int getLevel() {
        return stateStack.size();
    }

    private void popState() {
        state = stateStack.pop();
    }

    private void pushAndSetState(int newState) {
        stateStack.push(state);
        state = newState;
    }


    /**
     * Must be called from inside an Object (e.g. right after reading START_OBJECT).
     */
    public Enumeration objectElements() {
        return new JsonEnumeration(this, getLevel(), END_OBJECT);
    }

    /**
     * Must be called from inside an Array (e.g. right after reading START_ARRAY).
     */
    public Enumeration arrayElements() {
        return new JsonEnumeration(this, getLevel(), END_ARRAY);
    }

    /**
     * Consumes a single event, which must be of the specified type.
     * @throws com.xtremelabs.xtremeutil.util.json.JsonException
     */
    public void eat(int targetEvent) throws JsonException {
        int event = nextToken();
        if (event != targetEvent) {
            String mes = "expected " + (char)targetEvent + ", found " + (char)event + ' ' + tokens.toString();
            throw new JsonException(mes);
        }
    }

    /**
     * Skips forward inside an object until finds the given key.
     * Must be called from inside an object (e.g. right after reading START_OBJECT).
     * @throws com.xtremelabs.xtremeutil.util.json.JsonException
     * @returns true if the key was found, false otherwise.
     */
    public boolean seekInObject(String key) throws JsonException {
        for (Enumeration elems = objectElements(); elems.hasMoreElements(); ) {
            eat(Json.KEY);
            if (getString().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Assumes the next token is a String, and returns its value.
     * @throws com.xtremelabs.xtremeutil.util.json.JsonException
     */
    public String getStringValue() throws JsonException {
        eat(Json.STRING);
        return getString();
    }

    public String getKey() throws JsonException {
    	eat(Json.KEY);
    	String key = getString();
    	if (_listener != null && _listener.get() != null) {
    	    JsonListener l = (JsonListener) _listener.get();
    	    l.keyFound(key, this);
    	}
    	return key;
    }

    /**
     * Inside an object, locate given key, and return its value,
     * which *must* be String.
     * @param startObject true if you are current outside an object
     * @returns the String value, or null if key not found.
     * @throws com.xtremelabs.xtremeutil.util.json.JsonException
     */
    public String getStringValue(String key, boolean startObject) throws JsonException {
      
    	if (startObject) eat(Json.START_OBJECT);
      
        if (seekInObject(key)) {
            return getStringValue();
        } else {
            return null;
        }
    }
}

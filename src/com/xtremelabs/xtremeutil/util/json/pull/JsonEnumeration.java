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


class JsonEnumeration implements java.util.Enumeration {
    private Json parser;
    private int terminator;
    private int level;

    JsonEnumeration(Json parser, int level, int terminator) {
        this.parser     = parser;
        this.level      = level;
        this.terminator = terminator;
    }

    public boolean hasMoreElements() {
    	try {
	        parser.seekLevel(level);
	        int next = parser.peek();
	        boolean hasMore = next != terminator && next != 0;
	        if (!hasMore) {
	            parser.nextToken();
	        }
	        return hasMore;
    	} catch(Exception e) {
    		return false;
    	}
    }

    /**
     * Only neeed to conform to Enumeration interface, otherwise not useful.
     */
    public Object nextElement() {
    	try {
    		return new Integer(parser.nextToken());
    	} catch(Exception e) {
    		return new Integer(Json.STATE_ERROR);
    	}
    }
}

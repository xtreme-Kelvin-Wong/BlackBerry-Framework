/*
 * Copyright 2009 Xtreme Labs Inc.  All Rights Reserved. 
 */

package com.xtremelabs.xtremeutil.net.api;

import com.xtremelabs.xtremeutil.net.api.exception.InvalidModelException;
import com.xtremelabs.xtremeutil.net.api.exception.ParsingException;

import java.io.IOException;
import java.io.InputStream;

public interface ResponseObject {
    void initializeWithStream(InputStream in) throws InvalidModelException, ParsingException, IOException;
}

/*
 * Copyright 2009 Xtreme Labs Inc.  All Rights Reserved. 
 */

package com.xtremelabs.xtremeutil.net.api;

public interface ResponseHandler {
    /**
     * A request to a server was successful.
     *
     * @param request The original request made to a server
     * @param response The response from the server
     */
    void requestReturned(Request request, ResponseObject response);

    /**
     *
     * @param request The original request made to a server
     * @param exception An exception which may have been thrown while trying to contact the server
     * @param response The response from the server, which may be null
     */
    void requestFailed(Request request, Exception exception, ResponseObject response);
}

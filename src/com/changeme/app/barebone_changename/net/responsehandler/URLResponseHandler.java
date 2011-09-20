package com.changeme.app.barebone_changename.net.responsehandler;

import com.changeme.app.barebone_changename.net.request.URLRequest;
import com.changeme.app.barebone_changename.net.response.URLResponse;
import com.xtremelabs.xtremeutil.net.api.ResponseHandler;
import com.xtremelabs.xtremeutil.net.api.ResponseObject;
import com.xtremelabs.xtremeutil.net.api.Request;

public class URLResponseHandler implements ResponseHandler {
    public void requestReturned(Request request, ResponseObject response) {
        if (request instanceof URLRequest && response instanceof URLResponse) {
            ((URLRequest) request).getView().newResponse(((URLResponse) response).getData());
        }
    }

    public void requestFailed(Request request, Exception exception, ResponseObject response) {
        if (request instanceof URLRequest) {
            ((URLRequest) request).getView().newError(exception.hashCode());
        }
    }
}

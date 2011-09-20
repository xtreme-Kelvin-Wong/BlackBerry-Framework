package com.changeme.app.barebone_changename.net.request;

import com.changeme.app.barebone_changename.net.response.URLResponse;
import com.changeme.app.barebone_changename.net.responsehandler.URLResponseHandler;
import com.changeme.app.barebone_changename.ui.ConnectionTestView;
import com.xtremelabs.xtremeutil.net.api.http.HttpRequest;

public class URLRequest extends HttpRequest{

    ConnectionTestView _view;

    public URLRequest(String  url, ConnectionTestView view) {
        super(new URLResponseHandler());
        _view = view;
        setGetParameters(url, URLResponse.class);
    }

    public ConnectionTestView getView() {
        return _view;
    }
}

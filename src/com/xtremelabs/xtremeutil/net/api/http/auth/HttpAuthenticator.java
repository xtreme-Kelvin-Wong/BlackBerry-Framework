package com.xtremelabs.xtremeutil.net.api.http.auth;

import javax.microedition.io.HttpConnection;
import javax.microedition.io.StreamConnection;

import com.xtremelabs.xtremeutil.net.IConnectionModifier;

public abstract class HttpAuthenticator implements IConnectionModifier {
        abstract void authenticate(HttpConnection connection);

        public void modify(StreamConnection connection) {
                if (connection instanceof HttpConnection) {
                        authenticate((HttpConnection) connection);
                }
        }
}
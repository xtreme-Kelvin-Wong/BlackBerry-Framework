package com.xtremelabs.xtremeutil.net.api.http.auth;

import java.io.IOException;

import javax.microedition.io.HttpConnection;

import net.rim.device.api.io.Base64OutputStream;

public class BasicAuthenticator extends HttpAuthenticator {

        String _authenticationString;
        
        public BasicAuthenticator(String user, String password) {
                byte[] auth = new String(user + ":" + password).getBytes();
                String base64String = "";
                
                try {
                        base64String = Base64OutputStream.encodeAsString(auth, 0, auth.length, false, false);
                } catch(IOException e) {
                        
                }
                
                _authenticationString = new String("Basic " + base64String);
        }
        
        public void authenticate(HttpConnection connection) {
                try {
                        connection.setRequestProperty("Authorization", _authenticationString);
                } catch(Throwable e) {
                        
                }
        }

}

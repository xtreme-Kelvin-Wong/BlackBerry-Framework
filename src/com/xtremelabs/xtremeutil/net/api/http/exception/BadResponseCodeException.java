package com.xtremelabs.xtremeutil.net.api.http.exception;

import java.io.IOException;


public class BadResponseCodeException extends IOException {
        
        int _responseCode;
        
        public BadResponseCodeException(int responseCode) {
                super("Bad response code ("+responseCode+") from server");
                _responseCode = responseCode;
        }
        
        public int getResponseCode() {
                return _responseCode;
        }
}

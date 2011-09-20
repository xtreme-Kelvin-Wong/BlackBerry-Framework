package com.xtremelabs.xtremeutil.net.api.exception;

public class ParsingException extends Exception {
        
        Exception _innerException;
        
        public ParsingException(String description) {
                super(description);
        }

        public ParsingException(Exception e) {
                super(e.getMessage());
                _innerException = e;
        }
        
        public Exception getInnerException() {
                return _innerException;
        }
}

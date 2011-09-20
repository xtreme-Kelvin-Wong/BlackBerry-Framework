package com.xtremelabs.xtremeutil.net.api.response;

import com.xtremelabs.xtremeutil.net.api.ResponseObject;
import com.xtremelabs.xtremeutil.net.api.exception.InvalidModelException;
import com.xtremelabs.xtremeutil.net.api.exception.ParsingException;
import com.xtremelabs.xtremeutil.util.string.XStringUtilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StringResponse implements ResponseObject {

        String _value = null;
        
        public void initializeWithStream(InputStream in) throws InvalidModelException, ParsingException, IOException {
                ByteArrayOutputStream stringStream = new ByteArrayOutputStream(1024);
                
                int b = -1;
                while ((b = in.read()) != -1) {
                        stringStream.write(b);
                }
                
                _value = XStringUtilities.toUTF8String(stringStream.toByteArray());
                
                if (_value == null)
                        throw new ParsingException("Could not convert bytes to string.");
        }
        
        public String getString() {
                return _value;
        }
        
        public String toString() {
                return _value;
        }

}
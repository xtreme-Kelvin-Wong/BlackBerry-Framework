package com.xtremelabs.xtremeutil.net.api.data;

import java.io.IOException;
import java.io.OutputStream;

public interface IRequestPayload {
        void writeData(OutputStream out) throws IOException;
        void cleanUp() throws IOException;        
        long getLength();        
        String getContentType();
}

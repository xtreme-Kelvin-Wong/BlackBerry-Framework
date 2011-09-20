package com.xtremelabs.xtremeutil.net.api.data;

import java.io.IOException;
import java.io.OutputStream;

public class BytePayload implements IRequestPayload {
        
        String _contentType;
        byte[] _bytes;
        
        public BytePayload(byte[] bytes) {
                this(bytes, "application/octet-stream");
        }
        
        public BytePayload(byte[] bytes, String contentType) {
                _bytes = bytes;
                _contentType = contentType;
        }

        public long getLength() {
                return _bytes.length;
        }

        public void writeData(OutputStream out) throws IOException {
                out.write(_bytes);
        }

        public void cleanUp() {
                
        }

        public String getContentType() {
                return _contentType;
        }
}

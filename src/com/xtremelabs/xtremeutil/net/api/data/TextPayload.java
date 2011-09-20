package com.xtremelabs.xtremeutil.net.api.data;



public class TextPayload extends BytePayload {
        public TextPayload(String text) {
                super(text.getBytes());
        }
        
        public String getContentType() {
                return "text/plain";
        }
}

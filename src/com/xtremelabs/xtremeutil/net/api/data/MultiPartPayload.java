package com.xtremelabs.xtremeutil.net.api.data;

import com.xtremelabs.xtremeutil.util.string.XStringUtilities;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

public class MultiPartPayload implements IRequestPayload {
        static final String BOUNDARY_STRING = "----------V2ymHFg03ehbqgZCaKO6jy";
        
        static final byte[] CONTENT_DISPOSITION_FORM_DATA = "Content-Disposition: form-data;".getBytes();
        static final byte[] BOUNDARY           = BOUNDARY_STRING.getBytes();
        static final byte[] END_LINE           = "\r\n".getBytes();
    static final byte[] DOUBLE_HYPHEN = "--".getBytes();
    static final byte[] CONTENT_TYPE  = "Content-Type:".getBytes();
    static final byte[] NAME                   = "name".getBytes();
    static final byte[] SPACE                   = " ".getBytes();
    static final byte[] FILE_NAME           = "filename".getBytes();
    static final byte[] EQUALS                   = "=".getBytes(); 
    static final byte[] QUOTE                   = "\"".getBytes();
    static final byte[] SEMICOLON     = ";".getBytes();
        
        
    Hashtable _requestData = new Hashtable();
        
        public MultiPartPayload() {
                
        }

        /**
         * 
         * @param table table of string key/value pairs
         */
    public MultiPartPayload(Hashtable table) {
        Enumeration e = table.keys();
        
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            addRequestData(key, (String) table.get(key));
        }
    }
        
        public void addRequestData(String name, IRequestPayload payload) {
                _requestData.put(name, payload);
        }
        
        public void addRequestData(String name, String data) {
                _requestData.put(name, new TextPayload(data));
        }        
        
        public void cleanUp() throws IOException {
                Enumeration e = _requestData.elements();
                while(e.hasMoreElements()) {
                        IRequestPayload part = (IRequestPayload) e.nextElement();
                        part.cleanUp();
                }
        }

        public String getContentType() {
                return "multipart/form-data; boundary=" + BOUNDARY_STRING;
        }

        public long getLength() {
                long length = 0;
        
                Enumeration e = _requestData.keys();
                

                // Each element looks similar to
                // --BOUNDARY
                // Content-Disposition: form-data; name="[part name]"
                // Content-Type: [part type]
                //                
                // [part data]
                // --BOUNDARY
                                
                while(e.hasMoreElements()) {                        
                        //Boundary
                        length += DOUBLE_HYPHEN.length + BOUNDARY.length + END_LINE.length;
                        
                        String partName = (String) e.nextElement();
                        IRequestPayload part = (IRequestPayload) _requestData.get(partName);
                        
                        //Content Disposition Line
                        length += CONTENT_DISPOSITION_FORM_DATA.length;
                        length += SPACE.length + NAME.length + EQUALS.length;
                        length += QUOTE.length + partName.length() + QUOTE.length;
                        
                        
                        // file name
            if (part instanceof FilePayload) {
                FilePayload filePayload = (FilePayload) part;
                length += SEMICOLON.length + SPACE.length + FILE_NAME.length + EQUALS.length;
                length += QUOTE.length + utf8(filePayload.getFileName()).length + QUOTE.length;
            }
            
                        length += END_LINE.length;
                        
                        
                        
                        //Content Type Line
                        length += CONTENT_TYPE.length + SPACE.length + part.getContentType().length();
                        length += END_LINE.length + END_LINE.length;
                        
                        //Part content
                        length += part.getLength();                        
                        length += END_LINE.length;
                }
                

                length += DOUBLE_HYPHEN.length + BOUNDARY.length + DOUBLE_HYPHEN.length;
                
                return length;
        }

        public void writeData(OutputStream out) throws IOException {
                Enumeration e = _requestData.keys();
                
                while(e.hasMoreElements()) {                        
                        //Boundary
                        out.write(DOUBLE_HYPHEN);
                        out.write(BOUNDARY);
                        out.write(END_LINE);

                        
                        String partName = (String) e.nextElement();
                        IRequestPayload part = (IRequestPayload) _requestData.get(partName);
                        
                        //Content Disposition Line
                        out.write(CONTENT_DISPOSITION_FORM_DATA);
                        
                        out.write(SPACE);
                        out.write(NAME); 
                        out.write(EQUALS);
                        
                        out.write(QUOTE);
                        out.write(partName.getBytes());
                        out.write(QUOTE);
                        
                         // file name
            if (part instanceof FilePayload) {
                FilePayload filePayload = (FilePayload) part;
                out.write(SEMICOLON);
                out.write(SPACE);
                out.write(FILE_NAME);
                out.write(EQUALS);
                out.write(QUOTE);
                out.write(utf8(filePayload.getFileName()));
                out.write(QUOTE);
            }
                        
                        out.write(END_LINE);
                        
                        
                        //Content Type Line
                        out.write(CONTENT_TYPE);
                        out.write(SPACE);
                        out.write(utf8(part.getContentType()));
                        out.write(END_LINE);
                        out.write(END_LINE);

                        part.writeData(out);                                                                
                        out.write(END_LINE);
                }
                
                out.write(DOUBLE_HYPHEN);
                out.write(BOUNDARY);
                out.write(DOUBLE_HYPHEN);                
                out.write(END_LINE);
        }        


        private static byte[] utf8(String text) {
                return XStringUtilities.toUTF8Bytes(text);
        }
                
}

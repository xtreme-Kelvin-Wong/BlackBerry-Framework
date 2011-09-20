package com.xtremelabs.xtremeutil.net.api.data;

import com.xtremelabs.xtremeutil.util.logger.XLogger;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FilePayload implements IRequestPayload {
    
    String _filePath;
    String _fileName;
    String _contentType;
    
    long _length;
    
    public FilePayload(String filePath, String fileName, String contentType) {
        _filePath = filePath;
        _fileName = fileName;
        _contentType = contentType;
        
        if (_filePath != null) {
            FileConnection conn = null;
            
            try {
                 conn = (FileConnection) Connector.open(filePath);
                _length = conn.fileSize();
            } catch(Exception e) {
                _length = 0;
                XLogger.error(getClass(), e);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch(Exception e) {
                        
                    }
                }
            }
        }
    }

    public String getFileName() {
        return _fileName;
    }
    
    public String getFilePath() {
        return _filePath;
    }
    
    public void cleanUp() throws IOException {
        
    }

    public String getContentType() {
        return _contentType;
    }

    public long getLength() {
        return _length;
    }

    public void writeData(OutputStream out) throws IOException {
        FileConnection conn = null;
        InputStream fileStream = null;
        try {
            conn = (FileConnection) Connector.open(_filePath);
            fileStream = conn.openInputStream();
            
            byte[] bytes = new byte[1024];
            int length = -1;
            
            while ((length = fileStream.read(bytes)) > -1) {
                out.write(bytes, 0, length);
            }
        } finally {
            if (fileStream != null) {
                try { fileStream.close(); } catch (Exception e) {}
            }
            
            if (conn != null) {
                try { conn.close(); } catch (Exception e) {}
            }
        }
        
    }
}

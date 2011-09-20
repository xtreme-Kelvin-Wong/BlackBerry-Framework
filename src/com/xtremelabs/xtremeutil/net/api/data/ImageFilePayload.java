package com.xtremelabs.xtremeutil.net.api.data;

import net.rim.device.api.system.EncodedImage;

import java.io.IOException;
import java.io.OutputStream;

public class ImageFilePayload extends FilePayload {
    BytePayload _payload;

    public ImageFilePayload(EncodedImage image, String fileName) {
        super(null, fileName, image.getMIMEType());
        // XLogger.warn(getClass(), image.getMIMEType());
        _payload = new BytePayload(image.getData(), image.getMIMEType());
    }

    public long getLength() {
        return _payload.getLength();
    }
    
    public void writeData(OutputStream out) throws IOException {
        _payload.writeData(out);
    }

    public void cleanUp() throws IOException {
        _payload.cleanUp();
    }
}
package com.xtremelabs.xtremeutil.net.api.response;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import com.xtremelabs.xtremeutil.net.api.ResponseObject;
import com.xtremelabs.xtremeutil.net.api.exception.InvalidModelException;
import com.xtremelabs.xtremeutil.net.api.exception.ParsingException;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapResponse implements ResponseObject {
        Bitmap _bitmap = null;

        public Bitmap getBitmap() {
                return _bitmap;
        }


        public void initializeWithStream(InputStream in) throws InvalidModelException, ParsingException, IOException {
                ByteArrayOutputStream imageData = new ByteArrayOutputStream(1024);
        
        int b = -1;
        try {
                while ((b = in.read()) != -1) {
                    imageData.write(b);
                }
        } catch (IOException e) {
                XLogger.info(this.getClass(), "writing image data stopped from IOException: " + e.getMessage());
                throw e;
        }
        
        try {
                        EncodedImage image = EncodedImage.createEncodedImage(imageData.toByteArray(), 0, imageData.size());
                        
                        if (image == null) {
                                throw new ParsingException("Could not create EncodedImage.");
                        }
                        
                        _bitmap = image.getBitmap();
                        
                        if (image == null) {
                                throw new ParsingException("Could not create Bitmap.");
                        }
                        
                } catch(IllegalArgumentException e) {
                        throw new ParsingException(e);
                }
                
        }
}

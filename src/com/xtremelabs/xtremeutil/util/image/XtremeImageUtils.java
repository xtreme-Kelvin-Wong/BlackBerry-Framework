package com.xtremelabs.xtremeutil.util.image;

import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;

import java.io.InputStream;



public class XtremeImageUtils{


	public static final boolean IS_LOW_RESOLUTION = !(Math.max(Display.getWidth(), Display.getHeight())>=400);
	public static final boolean IS_HD_RESOLUTION = Math.max(Display.getWidth(), Display.getHeight())>480;

	private static final String SMALL_PNG_SUFFIX = "_small.png";
	private static final String HD_PNG_SUFFIX = "_hd.png";

	private static final int FOUR_THIRDS = Fixed32.div(Fixed32.ONE * 4, Fixed32.ONE*3);
	private static final int THREE_FOURTHS = Fixed32.div(Fixed32.ONE * 3,Fixed32.ONE * 4);


	public static int getDeviceParameter(int lowResParam,int highResParam,int hdResParam){
		if(IS_HD_RESOLUTION){
			return hdResParam;
		}else if(IS_LOW_RESOLUTION){
			return lowResParam;
		}
		else{
			return highResParam;
		}	
	}
	

	public static Bitmap getBitmapForString(String pngFileName){

		String suffix = null;
		int scaleFactor = 1;
		if(IS_HD_RESOLUTION){
			suffix = HD_PNG_SUFFIX;
			scaleFactor = THREE_FOURTHS;
		}else if(IS_LOW_RESOLUTION){
			suffix = SMALL_PNG_SUFFIX;
			scaleFactor = FOUR_THIRDS;
		}
		return getBitmapForResolution(pngFileName, suffix, scaleFactor);
	}
	
	private static Bitmap getBitmapForResolution(String pngFileName, String suffix, int scaleFactor){
		
		Bitmap image = null;
		if(suffix != null && scaleFactor != 1){
			String pngFileNameWithSuffix = appendSuffix(pngFileName, suffix);
			image = Bitmap.getBitmapResource(pngFileNameWithSuffix);
			if(image== null){
				image = getScaledBitmapForString(pngFileName, scaleFactor);
			}
		}else{
			image = Bitmap.getBitmapResource(pngFileName);
		}
		return image;
		
	}

    private static Bitmap getScaledBitmapForString(String pngFileName, int scaleFactor){
	
	final byte[] bytes;
        InputStream stream = null;
        try {
            String resourceURL = new StringBuffer().append('/').append(pngFileName).toString();
            stream = XtremeImageUtils.class.getResourceAsStream(resourceURL);
            if (stream == null) throw new NullPointerException();
            bytes = IOUtilities.streamToBytes(stream);
        } catch (Exception e) {
            //EXCEPTION!!!
            return null;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
        }
        EncodedImage image = EncodedImage.createEncodedImage(bytes, 0, bytes.length);
        image = image.scaleImage32(scaleFactor, scaleFactor);
        return image.getBitmap();
		
    }

    private static String appendSuffix(String pngFileName, String suffix) {
        final int dotIndex = pngFileName.indexOf('.');
        String pngName = pngFileName;
        if (dotIndex > 0) {
            pngName = new StringBuffer().append(pngFileName.substring(0, dotIndex)).append(suffix).toString();
        }
        return pngName;
    }
	


	
}
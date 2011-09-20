package com.xtremelabs.xtremeutil.ui.component.button;

import com.xtremelabs.xtremeutil.net.api.RequestQueue;
import net.rim.device.api.system.Bitmap;

public class BitmapFromUrlField extends BitmapButtonFromUrlField {

    public BitmapFromUrlField(Bitmap noImageBitmap, RequestQueue requestQueue, String imageUrl) {
        super(null, noImageBitmap, requestQueue, imageUrl, USE_ALL_WIDTH | USE_ALL_HEIGHT, false);
    }

    public BitmapFromUrlField(String imageUrl) {
        super(null, null, null, imageUrl, USE_ALL_WIDTH | USE_ALL_HEIGHT, false);
    }

    public BitmapFromUrlField(Bitmap loadingImage, String imageUrl) {
        super(null, null, loadingImage, null, imageUrl, USE_ALL_WIDTH | USE_ALL_HEIGHT, false);
    }
}

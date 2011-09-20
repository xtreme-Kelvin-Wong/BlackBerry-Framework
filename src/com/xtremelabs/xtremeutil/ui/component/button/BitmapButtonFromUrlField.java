package com.xtremelabs.xtremeutil.ui.component.button;

import com.xtremelabs.xtremeutil.application.XtremeApplication;
import com.xtremelabs.xtremeutil.net.api.Request;
import com.xtremelabs.xtremeutil.net.api.RequestQueue;
import com.xtremelabs.xtremeutil.net.api.ResponseHandler;
import com.xtremelabs.xtremeutil.net.api.ResponseObject;
import com.xtremelabs.xtremeutil.net.api.http.HttpRequest;
import com.xtremelabs.xtremeutil.net.api.response.BitmapResponse;
import com.xtremelabs.xtremeutil.ui.ActionHandler;
import com.xtremelabs.xtremeutil.ui.UpdateListener;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;

import java.util.Enumeration;
import java.util.Vector;

public class BitmapButtonFromUrlField extends Manager implements ResponseHandler {
    private final Bitmap DEFAULT_LOADING_BITMAP = Bitmap.getBitmapResource(XtremeApplication.LOADING_PNG_NAME);
    private Bitmap noImageBitmap = Bitmap.getBitmapResource(XtremeApplication.NO_IMAGE_PNG_NAME);

    private volatile BitmapField bitmapField;
    //  update listeners are notified when image loading is complete or can't start
    private Vector updateListeners = new Vector();
    private volatile ActionHandler actionHandler;

    public void addListener(UpdateListener listener) {
        updateListeners.addElement(listener);
    }

    protected void notifyListeners() {
        Enumeration e = updateListeners.elements();
        while (e.hasMoreElements()) {
            ((UpdateListener) e.nextElement()).update();
        }
    }

    public BitmapButtonFromUrlField(ActionHandler handler, String imageUrl) {
        this(handler, null, null, imageUrl, USE_ALL_WIDTH | USE_ALL_HEIGHT, true);
    }

    public BitmapButtonFromUrlField(ActionHandler handler, Bitmap noImageBitmap,
                                    RequestQueue requestQueue, String imageUrl) {
        this(handler, noImageBitmap, requestQueue, imageUrl, USE_ALL_WIDTH | USE_ALL_HEIGHT, true);
    }

    public BitmapButtonFromUrlField(ActionHandler handler, Bitmap noImageBitmap,
                                    RequestQueue requestQueue, String imageUrl, long l, boolean focusable) {
        this(handler, noImageBitmap, null, requestQueue, imageUrl, l, focusable);
    }

    public BitmapButtonFromUrlField(ActionHandler handler, Bitmap noImageBitmap, Bitmap loadingBitmap,
                                    RequestQueue requestQueue, String imageUrl, long l, boolean focusable) {
        super(l);
        this.noImageBitmap = (noImageBitmap == null) ? this.noImageBitmap : noImageBitmap;
        loadingBitmap = (loadingBitmap != null) ? loadingBitmap : DEFAULT_LOADING_BITMAP;
        bitmapField = new BitmapField(loadingBitmap, focusable ? Field.FOCUSABLE : Field.NON_FOCUSABLE) {
            protected void onUnfocus() {
                super.onUnfocus();
                invalidate();
            }

            protected void drawFocus(Graphics graphics, boolean on) {
                if (on) {
                    graphics.pushContext(0, 0, getBitmapWidth(), getBitmapHeight(), 0, 0);
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(0, 0, getBitmapWidth(), getBitmapHeight());
                    graphics.setColor(Color.WHITE);
                    graphics.drawRect(1, 1, getBitmapWidth() - 2, getBitmapHeight() - 2);
                    graphics.setColor(Color.WHITE);
                    graphics.drawRect(2, 2, getBitmapWidth() - 4, getBitmapHeight() - 4);
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(3, 3, getBitmapWidth() - 6, getBitmapHeight() - 6);
                    graphics.popContext();
                }
            }
        };

        add(bitmapField);
        if (imageUrl == null || imageUrl.length() < 10) {
            bitmapField.setBitmap(this.noImageBitmap);
            notifyListeners();
            actionHandler = null;
            return;
        }
        actionHandler = handler;
        HttpRequest httpRequest = new HttpRequest(this);
        httpRequest.setGetParameters(imageUrl, BitmapResponse.class);
        if (requestQueue == null) {
            httpRequest.executeRequest();
        } else {
            requestQueue.send(httpRequest);
        }
    }

    public int getPreferredWidth() {
        return Math.max(bitmapField.getBitmapWidth(), DEFAULT_LOADING_BITMAP.getWidth());
    }

    public int getPreferredHeight() {
        return Math.max(bitmapField.getBitmapHeight(), DEFAULT_LOADING_BITMAP.getHeight());
    }


    protected void sublayout(int width, int height) {
        setPositionChild(bitmapField, 0, 0);
        width = bitmapField.getBitmapWidth() > Display.getWidth() ? Display.getWidth() : bitmapField.getBitmapWidth();
        height = bitmapField.getBitmapHeight() > Display.getHeight() ? Display.getHeight() : bitmapField.getBitmapHeight();
        layoutChild(bitmapField, width, height);
        setExtent(width, height);
    }

    protected boolean keyChar(char c, int status, int time) {
        if (c == Characters.ENTER && actionHandler != null) {
            return actionHandler.executePressCommand();
        }
        return super.keyChar(c, status, time);
    }

    protected boolean trackwheelUnclick(int status, int time) {
        return (actionHandler != null && actionHandler.executePressCommand());
    }

    public boolean isFocus() {
        return bitmapField.isFocus();
    }

	public boolean handleConnectionFailure() {
		return true; // Prevent connection failure alert from being shown 		
	}

    public void requestReturned(Request request, ResponseObject bitmapResponse) {
        if (bitmapResponse != null && bitmapResponse instanceof BitmapResponse && ((BitmapResponse)bitmapResponse).getBitmap() != null) {
            try {
                Bitmap bitmap = ((BitmapResponse) bitmapResponse).getBitmap();
                bitmapField.setBitmap(bitmap);
            } catch (Exception e) {
                bitmapField.setBitmap(noImageBitmap);
            }
        } else {
            bitmapField.setBitmap(noImageBitmap);
        }
        notifyListeners();    }

    public void requestFailed(Request request, Exception exception, ResponseObject response) {
        bitmapField.setBitmap(noImageBitmap);
        notifyListeners();
    }
}
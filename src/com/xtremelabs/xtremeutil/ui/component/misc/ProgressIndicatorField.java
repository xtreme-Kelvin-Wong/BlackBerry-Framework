package com.xtremelabs.xtremeutil.ui.component.misc;

import com.xtremelabs.xtremeutil.util.concurrency.UITask;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.component.BitmapField;

/**
 * Created by Xtreme Labs Inc.
 * All Rights Reserved
 * Date: 30-Jun-2009
 * Time: 3:21:49 PM
 */
public abstract class ProgressIndicatorField extends BitmapField {
    protected Bitmap[] images;
    private int currentIndex = 0;

    // for factory method in FieldFactory
    protected ProgressIndicatorField() {
    }

    protected void onDisplay() {
        super.onDisplay();
        loadPNGs();
        loadPNGs();
        Bitmap bitmap = images[0];
        bitmap = bitmap != null ? bitmap : new Bitmap(0, 0);
        setBitmap(bitmap);
    }

    protected abstract void loadPNGs();

    // if increment successful return true
    public synchronized boolean increment() {
        if (currentIndex == (images.length - 1)) {
            return false;
        }
        Bitmap bitmap = images[++currentIndex];
        bitmap = bitmap != null ? bitmap : new Bitmap(0, 0);
        setBitmap(bitmap);
        return true;
    }

    // if decrement successful return true
    public synchronized boolean decrement() {
        if (currentIndex == 0) {
            return false;
        }
        Bitmap bitmap = images[--currentIndex];
        bitmap = bitmap != null ? bitmap : new Bitmap(0, 0);
        setBitmap(bitmap);
        return true;
    }

    public void setBitmap(final Bitmap bitmap) {
        new UITask() {
            public void execute() {
                ProgressIndicatorField.super.setBitmap(bitmap);
            }
        }.invokeAndWait();
    }

    public int getPreferredWidth() {
        return getBitmapWidth();
    }

    public int getPreferredHeight() {
        return getBitmapHeight();
    }

}

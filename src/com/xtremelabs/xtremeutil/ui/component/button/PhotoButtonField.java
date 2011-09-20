package com.xtremelabs.xtremeutil.ui.component.button;

import com.xtremelabs.xtremeutil.util.concurrency.UITask;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;


public abstract class PhotoButtonField extends Manager {

    private BitmapField bitmapField;
    public static final int MARGIN = Display.getWidth() / 64;

    public PhotoButtonField(Bitmap b) {
        this(b, Field.FOCUSABLE | FIELD_HCENTER);
    }

    public PhotoButtonField(Bitmap b, long style) {
        super(style);
        bitmapField = new BitmapField(b, style) {
            protected void onUnfocus() {
                super.onUnfocus();
                invalidate();
            }

            protected void drawFocus(Graphics graphics, boolean on) {
                int width = getBitmapWidth() - 2 * MARGIN;
                int height = getBitmapHeight() - 2 * MARGIN;
                graphics.pushContext(0, 0, width, height, 0, 0);

                graphics.setColor(Color.RED);
                graphics.drawRect(0, 0, width, height);
                graphics.drawRect(1, 1, width - 2, height - 2);
                graphics.popContext();
            }
        };
        add(bitmapField);
    }

    public int getPreferredWidth() {
        return bitmapField.getBitmapWidth() > Display.getWidth() ? Display.getWidth() : bitmapField.getBitmapWidth();
    }

    public int getPreferredHeight() {
        return bitmapField.getBitmapHeight() > Display.getHeight() ? Display.getHeight() : bitmapField.getBitmapHeight();
    }

    public boolean trackwheelUnclick(int status, int time) {
        return handleAction();
    }

    protected abstract boolean handleAction();

    protected void sublayout(int availableWidth, int availableHeight) {
        setPositionChild(bitmapField, MARGIN, MARGIN);
        layoutChild(bitmapField, getPreferredWidth() - 2 * MARGIN, getPreferredHeight() - 2 * MARGIN);
        setExtent(getPreferredWidth(), getPreferredHeight());
    }

    public void setBitmap(final Bitmap bitmap) {
        new UITask() {
            public void execute() {
                bitmapField.setBitmap(bitmap);
            }
        }.invokeLater();
    }
}
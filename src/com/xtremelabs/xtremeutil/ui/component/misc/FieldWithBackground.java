package com.xtremelabs.xtremeutil.ui.component.misc;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;

/**
 * Created by Xtreme Labs Inc.
 * Date: Mar 12, 2009
 * Time: 11:58:36 AM
 */
public class FieldWithBackground extends Manager {
    protected Bitmap backgroundBitmap;
    private Field field;


    public FieldWithBackground(Bitmap backgroundBitmap, Field field) {
        super(NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL);
        this.backgroundBitmap = backgroundBitmap;
        this.field = field;
        add(this.field);
    }


    public int getPreferredWidth() {
        return backgroundBitmap.getWidth();
    }

    public int getPreferredHeight() {
        return backgroundBitmap.getHeight();
    }

    protected void sublayout(int w, int h) {
        int horizontalPadding = (getPreferredWidth()
                - Math.min(field.getPreferredWidth(), getPreferredWidth())) / 2;
        int verticalPadding = (getPreferredHeight()
                - Math.min(field.getPreferredHeight(), getPreferredHeight())) / 2;
        setPositionChild(field, horizontalPadding, verticalPadding);
        layoutChild(field, getPreferredWidth() - 2 * horizontalPadding,
                getPreferredHeight() - 2 * verticalPadding);
        setExtent(getPreferredWidth(), getPreferredHeight());
    }

    public void paintBackground(Graphics graphics) {
        graphics.drawBitmap(0, 0, getPreferredWidth(), getPreferredHeight(), backgroundBitmap, 0, 0);
    }

    private int getBitmapHeight() {
        return backgroundBitmap.getHeight();
    }

    private int getBitmapWidth() {
        return backgroundBitmap.getWidth();
    }

    public Field getField() {
        return field;
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (field.isFocus()) {
            int temp = graphics.getColor();
            graphics.setColor(Color.DARKGREEN);
            graphics.drawRect(2, 2, getBitmapWidth() - 6, getBitmapHeight() - 6);
            graphics.setColor(Color.GREEN);
            graphics.drawRect(3, 3, getBitmapWidth() - 6, getBitmapHeight() - 6);
            graphics.setColor(temp);
        }
    }


    public void onFocus(int direction) {
        super.onFocus(direction);
        invalidate();
    }

    public void onUnfocus() {
        super.onUnfocus();
        invalidate();
    }
}
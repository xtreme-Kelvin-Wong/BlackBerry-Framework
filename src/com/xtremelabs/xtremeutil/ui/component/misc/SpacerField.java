package com.xtremelabs.xtremeutil.ui.component.misc;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

public class SpacerField extends Field {

    int _width;
    int _height;

    public SpacerField(long style, int width, int height) {
        super(style);
        if (width < 0) {
            width = 0;
        }
        if (height < 0) {
            height = 0;
        }
        _width = width;
        _height = height;

    }

    public SpacerField(int width, int height) {
        this(NON_FOCUSABLE, width, height);
    }

    protected void layout(int width, int height) {
        setExtent(getPreferredWidth(), getPreferredHeight());
    }

    public void drawFocus(Graphics g, boolean on) {

    }

    public void paintBackground(Graphics g) {

    }

    public void paint(Graphics g) {
    }

    public int getPreferredWidth() {
        return _width;
    }

    public int getPreferredHeight() {
        return _height;
    }
}
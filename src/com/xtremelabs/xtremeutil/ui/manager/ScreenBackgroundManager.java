package com.xtremelabs.xtremeutil.ui.manager;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;

public class ScreenBackgroundManager extends Manager {
    protected Bitmap backgroundBitmap;

    public ScreenBackgroundManager(Bitmap backgroundBitmap, Field content) {
        super(NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL | NO_VERTICAL_SCROLLBAR);
        this.backgroundBitmap = backgroundBitmap;
        add(content);
    }

    protected void sublayout(int i, int i1) {
        final Field field0 = getField(0);
        setPositionChild(field0, 0, 0);
        final int width = Display.getWidth();
        final int height = Display.getHeight();
        layoutChild(field0, width, height);

        setExtent(width, height);
        setVirtualExtent(width, height);
    }

    protected void paintBackground(Graphics graphics) {
        graphics.drawBitmap(0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), backgroundBitmap, 0, 0);
    }
}

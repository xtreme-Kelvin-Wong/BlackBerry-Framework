package com.xtremelabs.xtremeutil.ui.component.text;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;


public class ColorTaggedLabelField extends Field {
    protected int color;
    protected String labelText;
    protected Font font;

    protected boolean absoluteSizeSet = false;
    protected int absoluteXSize = 0;
    protected int absoluteYSize = 0;

    protected boolean boxEnabled = true;

    public ColorTaggedLabelField(int color, String labelText, Font font) {
        this.color = color;
        this.labelText = labelText;
        this.font = font;
    }

    public void setAbsoluteWidth(int x) {
        absoluteSizeSet = true;
        absoluteXSize = x;
        absoluteYSize = font.getHeight();
    }

    protected void layout(int i, int i1) {
        if (!absoluteSizeSet)
            setExtent(font.getHeight() + 2 + font.getAdvance(labelText), font.getHeight());
            // setExtent(font.getHeight() + 2 + font.getAdvance(labelText) * 2 , font.getHeight());
        else
            setExtent(absoluteXSize, absoluteYSize);
    }

    public void setBoxEnabled(boolean enabled) {
        boxEnabled = enabled;
    }

    public int getPreferredHeight() {
        return Display.getHeight() / 30;
    }

    public int getPreferredWidth() {
        return Display.getWidth() / 2;
    }

    protected void paint(Graphics graphics) {

        if (labelText.length() == 0) {
            return;
        }
        graphics.setColor(Color.WHITE);
        graphics.setFont(font);
        graphics.drawText(labelText, font.getHeight() + 2, 0);
        graphics.setColor(color);
        // graphics.drawText(labelText, font.getAdvance(labelText) + font.getHeight() + 2 , 0);

        if (boxEnabled) {
            graphics.fillRect(0, 0, font.getHeight(), font.getHeight());
            graphics.setColor(Color.BLACK);
            graphics.setStrokeWidth(2);
            graphics.drawRect(0, 0, font.getHeight(), font.getHeight());
        }
    }
}

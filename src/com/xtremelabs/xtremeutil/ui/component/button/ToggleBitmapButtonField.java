package com.xtremelabs.xtremeutil.ui.component.button;

import com.xtremelabs.xtremeutil.ui.GuiUtils;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Graphics;

/**
 * Created by Xtreme Labs Inc.
 * All Rights Reserved
 * Date: 29-Jun-2009
 * Time: 3:44:21 PM
 */
public abstract class ToggleBitmapButtonField extends BitmapButtonField {
    public static final int SELECT = 0;
    public static final int DESELECT = 1;


    protected boolean selected = false;

    //   import highlighted and unhighlighted bitmaps in constructor
//   private boolean selected;

    protected ToggleBitmapButtonField(final Bitmap highlighted, final Bitmap unHighlighted) {
        super(unHighlighted, unHighlighted);
        this.highlighted = highlighted;
        this.unHighlighted = unHighlighted;
    }

    protected void onFocus(int direction) {
        focused = true;
    }

    protected void onUnfocus() {
        focused = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void toggleSelected() {
        setSelected(selected ? 1 : 0);
    }

    public void setSelected(int selected) {
//      this.selected = selected;
        if (selected == SELECT) {
            this.selected = true;
            setBitmap(highlighted);
        } else if (selected == DESELECT) {
            this.selected = false;
            setBitmap(unHighlighted);
        }
    }

    protected void drawFocus(Graphics graphics, boolean on) {
        if (on) {
            int lightBlue = GuiUtils.getApplicationColor("LightBlue");
            int blue = GuiUtils.getApplicationColor("Blue");
            int darkBlue = GuiUtils.getApplicationColor("DarkBlue");
            graphics.pushContext(0, 0, getBitmapWidth(), getBitmapHeight(), 0, 0);
            graphics.setColor(lightBlue);
            graphics.drawRoundRect(0, 0, getBitmapWidth(), getBitmapHeight(), 10, 10);
            graphics.setColor(blue);
            graphics.drawRoundRect(1, 1, getBitmapWidth() - 2, getBitmapHeight() - 2, 9, 9);
            graphics.setColor(darkBlue);
            graphics.drawRoundRect(2, 2, getBitmapWidth() - 3, getBitmapHeight() - 3, 8, 8);
            graphics.popContext();
        }
    }

    protected boolean trackwheelUnclick(int status, int time) {
        toggleSelected();
        return super.trackwheelUnclick(status, time);
    }

    protected boolean keyChar(char c, int status, int time) {
        if (c == Characters.ENTER) {
            toggleSelected();
        }
        return super.keyChar(c, status, time);
    }
}

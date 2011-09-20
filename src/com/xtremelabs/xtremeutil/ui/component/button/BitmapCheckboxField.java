package com.xtremelabs.xtremeutil.ui.component.button;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.CheckboxField;

public class BitmapCheckboxField extends CheckboxField {
    private Bitmap checked,
            unchecked,
            checkedFocussed,
            uncheckedFocussed;

    public BitmapCheckboxField(Bitmap checked,
                               Bitmap unchecked,
                               Bitmap checkedFocussed,
                               Bitmap uncheckedFocussed) {
        this.checked = checked;
        this.unchecked = unchecked;
        this.checkedFocussed = checkedFocussed;
        this.uncheckedFocussed = uncheckedFocussed;
    }

    private Bitmap getSkinBitmap() {
        if (getChecked()) if (isFocus()) return checkedFocussed;
        else return checked;
        else if (isFocus()) return uncheckedFocussed;
        else return unchecked;
    }

    public int getPreferredWidth() {
        return getSkinBitmap().getWidth();
    }

    public int getPreferredHeight() {
        return getSkinBitmap().getHeight();
    }

    protected void layout(int w, int h) {
        Bitmap skin = getSkinBitmap();
        setExtent(skin.getWidth(), skin.getHeight());
        invalidate();
    }

    protected void paint(Graphics graphics) {
        Bitmap bitmap = getSkinBitmap();
        graphics.drawBitmap(0, 0, bitmap.getWidth(), bitmap.getHeight(),
                bitmap, 0, 0);
    }
}

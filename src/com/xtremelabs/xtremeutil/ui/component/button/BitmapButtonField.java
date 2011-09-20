package com.xtremelabs.xtremeutil.ui.component.button;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;

public class BitmapButtonField extends BitmapField {

    protected Bitmap highlighted;
    protected Bitmap unHighlighted;
    protected boolean focused;
    private FieldChangeListener changeListener;
    protected boolean focusable = true;

    //   import highlighted and unhighlighted bitmaps in constructor
    public void importBitmaps() {
    }

    public boolean handleAction() {
        if (changeListener != null)
            changeListener.fieldChanged(this, 0);
        return true;
    }

    public BitmapButtonField() {
        super(new Bitmap(0, 0), FIELD_HCENTER | FIELD_VCENTER | FOCUSABLE);
        importBitmaps();
        setBitmap(unHighlighted);
    }

    public BitmapButtonField(Bitmap highlighted, Bitmap unHighlighted) {
        super(unHighlighted, FIELD_HCENTER | FIELD_VCENTER | FOCUSABLE);
        this.highlighted = highlighted;
        this.unHighlighted = unHighlighted;
    }

    public int getPreferredWidth() {
        return getBitmapWidth();
    }

    public int getPreferredHeight() {
        return getBitmapHeight();
    }

    public boolean isFocus() {
        return focused;
    }

    protected void onFocus(int direction) {
        focused = true;
        setBitmap(highlighted);
    }

    protected void onUnfocus() {
        focused = false;
        setBitmap(unHighlighted);
    }

    protected void layout(int i, int i1) {
        super.layout(i, i1);
        setExtent(getBitmapWidth(), getBitmapHeight());
    }

    protected void drawFocus(Graphics graphics, boolean on) {
    }

    protected boolean trackwheelUnclick(int status, int time) {
        return handleAction();
    }

    protected boolean keyChar(char c, int status, int time) {
        if (c == Characters.ENTER) {
            return handleAction();
        }
        return super.keyChar(c, status, time);
    }

    public FieldChangeListener getChangeListener() {
        return changeListener;
    }

    public void setChangeListener(FieldChangeListener fieldChangeListener) {
        changeListener = fieldChangeListener;
    }

    public boolean isFocusable() {
        return focusable;
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }
}

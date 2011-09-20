package com.xtremelabs.xtremeutil.ui.component.misc;

import com.xtremelabs.xtremeutil.ui.GuiUtils;
import com.xtremelabs.xtremeutil.ui.Theme;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ObjectChoiceField;

public class XtremeObjectChoiceField extends ObjectChoiceField {
    public XtremeObjectChoiceField(Object[] choices, int defaultChoice) {
        super("", choices, defaultChoice, Field.FIELD_LEFT);
        setFont(Theme.SETTINGS_CHOICE_FONT);
    }

    public void paint(Graphics graphics) {
        graphics.pushContext(graphics.getClippingRect(), 0, 0);
        if (isFocus()) {
            graphics.setColor(Color.WHITE);
        } else {
            graphics.setColor(GuiUtils.getApplicationColor("LightBlue"));
        }
        super.paint(graphics);
        graphics.popContext();
    }

//    public void paintBackground(Graphics graphics) {
//        XYRect clippingRect = graphics.getClippingRect();
//        graphics.pushContext(clippingRect, 0, 0);
//        graphics.setColor(GuiUtils.getApplicationColor("LighterGrey"));
//        graphics.fillRect(0, 0, clippingRect.width, clippingRect.height);
//        graphics.popContext();
//    }

    public void onExposed() {
        super.onExposed();
        invalidate();
    }

    public void onUnfocus() {
        super.onUnfocus();
        invalidate();
    }

}

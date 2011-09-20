package com.xtremelabs.xtremeutil.ui.component.misc;

import com.xtremelabs.xtremeutil.ui.Theme;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.CheckboxField;


public class ColorCheckboxField extends CheckboxField {

    private int color;
    private String label;

    public ColorCheckboxField(String label, int color, boolean selected) {
        super(" " + label, selected);
        this.label = label;
        this.color = color;
        setFont(Theme.DEFAULT_FONT);
    }

    public ColorCheckboxField(String label, int color, boolean selected, long style) {
        super(" " + label, selected, style);
        this.label = label;
        this.color = color;
        setFont(Theme.DEFAULT_FONT);
    }

    protected void paint(Graphics g) {
        g.setColor(color);
        super.paint(g);
    }

}

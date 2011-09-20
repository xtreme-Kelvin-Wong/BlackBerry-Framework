package com.xtremelabs.xtremeutil.ui.component.piechart;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;

public class PieChartField extends Field {

    private double total;
    private Slice[] slices;
    private int size = 90;

    private int backgroundColor;

    public PieChartField(Slice[] slices, double total, int backgroundColor, int size) {
        this.slices = slices;
        this.total = total;
        this.backgroundColor = backgroundColor;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    protected void layout(int i, int j) {
        setExtent(size, size);
    }

    public int getPreferredHeight() {
        return size;
    }

    public int getPreferredWidth() {
        return size;
    }

    public void setSlices(Slice[] slices) {
        this.slices = slices;
        this.invalidate();
    }

    protected void paint(Graphics graphics) {

        XYRect rect = graphics.getClippingRect();
        graphics.pushContext(rect, 0, 0);

        //Draw base pie
        graphics.setColor(backgroundColor);
        graphics.fillArc(0, 0, this.getPreferredWidth(), this.getPreferredWidth(), 0, 360);

        int from = 180;
        //Draw peices
        for (int i = 0; i < slices.length; i++) {
            Slice slice = slices[i];
            int degrees = (int) (slice.getSliceValue() / total * 360);
            if (degrees != 0) {
                graphics.setColor(slice.getColor());
                graphics.fillArc(0, 0, this.getPreferredWidth(), this.getPreferredWidth(), from, degrees);
                from += degrees;
            }
        }
        graphics.popContext();
    }
}

package com.xtremelabs.xtremeutil.ui.component.piechart;

public class Slice {
    double value;
    int color;

    public Slice(double value, int color) {
        this.value = value;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public double getSliceValue() {
        return value;
    }


}

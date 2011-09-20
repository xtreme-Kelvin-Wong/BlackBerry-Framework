package com.xtremelabs.xtremeutil.ui.manager;

import net.rim.device.api.ui.*;

/*
 *  Wraps a single field with padding, a border and a margin. The padding and margin size
 *  can be set independently for each edge. The border width and color can also be set.
 */

public class BoxManager extends Manager {

    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int paddingLeft;
    private int marginTop;
    private int marginRight;
    private int marginBottom;
    private int marginLeft;
    private int borderWidth;
    private int borderColor;

    public BoxManager() {
        super(0);
        build(null,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, Color.BLACK);
    }

    public BoxManager(Field child) {
        super(0);
        build(child,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, Color.BLACK);
    }

    public BoxManager(Field child, int padding, int margin, int border, int borderColor) {
        super(0);
        build(child,
                padding, padding, padding, padding,
                margin, margin, margin, margin,
                border, borderColor);
    }

    public BoxManager(Field child,
                      int padTop, int padRight, int padBottom, int padLeft,
                      int marginTop, int marginRight, int marginBottom, int marginLeft,
                      int borderWidth, int borderColor) {
        super(0);
        build(child,
                padTop, padRight, padBottom, padLeft,
                marginTop, marginRight, marginBottom, marginLeft,
                borderWidth, borderColor);
    }

    void build(Field child,
               int padTop, int padRight, int padBottom, int padLeft,
               int marginTop, int marginRight, int marginBottom, int marginLeft,
               int borderWidth, int borderColor) {
        if (child != null && child.getIndex() < 0) {
            add(child);
        }
        this.paddingTop = padTop;
        this.paddingRight = padRight;
        this.paddingBottom = padBottom;
        this.paddingLeft = padLeft;
        this.marginTop = marginTop;
        this.marginRight = marginRight;
        this.marginBottom = marginBottom;
        this.marginLeft = marginLeft;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
    }

    public int getPreferredHeight() {
        int extra = marginTop + paddingTop + borderWidth * 2 + paddingBottom + marginBottom;
        if (getFieldCount() > 0) {
            extra += getPreferredHeightOfChild(getField(0));
        }
        return extra;
    }

    public int getPreferredWidth() {
        int extra = marginLeft + paddingLeft + borderWidth * 2 + paddingRight + marginRight;
        if (getFieldCount() > 0) {
            extra += getPreferredWidthOfChild(getField(0));
        }
        return extra;
    }

    protected void sublayout(int width, int height) {
        // Determine the size of the child as the lesser of its preferred size and
        // the available space passed to this method minus the surrounding elements.

        int extraLeft = marginLeft + borderWidth + paddingLeft,
                extraRight = paddingRight + borderWidth + marginRight,
                extraTop = marginTop + borderWidth + paddingTop,
                extraBottom = paddingBottom + borderWidth + marginBottom,
                availableChildWidth = width - extraLeft - extraRight,
                availableChildHeight = height - extraTop - extraBottom,
                childWidth,
                childHeight;

        if (getFieldCount() > 0) {
            Field child = getField(0);
            setPositionChild(child, extraLeft, extraTop);

            childWidth = Math.min(getPreferredWidthOfChild(child), availableChildWidth);
            childHeight = Math.min(getPreferredHeightOfChild(child), availableChildHeight);

            // Child fields will not necessarily stay within the dimensions
            // passed to layoutChild. For this reason, we update our variables
            // with its actual extent afterward.
            layoutChild(child, childWidth, childHeight);
            childWidth = child.getWidth();
            childHeight = child.getHeight();
        } else {
            childWidth = childHeight = 0;
        }

        setExtent(childWidth + extraLeft + extraRight,
                childHeight + extraTop + extraBottom);
    }

    protected void subpaint(Graphics graphics) {
        if (getFieldCount() > 0) {
            paintChild(graphics, getField(0));
        }

        if (borderWidth != 0) {
            XYRect ourRect = getExtent();
            graphics.setStrokeWidth(borderWidth);
            graphics.setColor(borderColor);
            graphics.drawRoundRect(marginLeft + borderWidth / 2,
                    marginTop + borderWidth / 2,
                    ourRect.width - marginLeft - marginRight - borderWidth,
                    ourRect.height - marginTop - marginBottom - borderWidth, 6, 6);
        }
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public void setPadding(int padding) {
        paddingTop = paddingRight = paddingBottom = paddingLeft = padding;
    }

    public void setMargin(int margin) {
        marginTop = marginRight = marginBottom = marginLeft = margin;
    }
}

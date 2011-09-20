package com.xtremelabs.xtremeutil.ui.component.text;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.*;

/**
 * Custom button field that shows how to use images as button backgrounds. The user can also provide a field text, and
 * must decide whether that text will be draw or not when calling the constructor. The preferred height is calculated as
 * the max of the image height and the text height (if text is used). The same goes for width.
 * <p/>
 * This class is modified from a RIM sample.
 */
public class TextOnBackgroundButtonField extends Field {
    private String _label;
    private int _labelHeight;
    private int _labelWidth;
    private Font _font;

    private Bitmap _currentPicture, _onPicture, _offPicture;
    private boolean clearBeforePaint = false; // clear button background before every paint
    private int clearColor = Color.WHITE; // color to use if clearBeforePaint set to true;
    private boolean drawText = false;

    public static final boolean DO_NOT_DRAW_TEXT = false;
    public static final boolean DO_DRAW_TEXT = true;

    public boolean isDrawText() {
        return drawText;
    }

    public void setDrawText(boolean drawText) {
        this.drawText = drawText;
    }

    public int getClearColor() {
        return clearColor;
    }

    public void setClearColor(int clearColor) {
        this.clearColor = clearColor;
    }

    public boolean isClearBeforePaint() {
        return clearBeforePaint;
    }

    public void setClearBeforePaint(boolean clearBeforePaint) {
        this.clearBeforePaint = clearBeforePaint;
    }

    public void setBitmap(boolean on) {
        _currentPicture = on ? _onPicture : _offPicture;
        invalidate();
    }

    /**
     * Constructor that accepts the on picture and off bitmaps, the resulting field will use the off picture to start
     *
     * @param _onPicture  picture when field is in focus
     * @param _offPicture picture when field is out of focus
     */
    public TextOnBackgroundButtonField(Bitmap _onPicture, Bitmap _offPicture) {
        this("", Field.FOCUSABLE, _onPicture, _offPicture, DO_NOT_DRAW_TEXT);
    }

    /**
     * Alternate constructor allowing users to also set a text, set whether text is drawn, and set a style.
     *
     * @param text        - the text to be displayed on the button
     * @param style       - combination of field style bits to specify display attributes
     * @param _onPicture  - picture while in focus
     * @param _offPicture - picture while out of focus (default)
     */
    public TextOnBackgroundButtonField(String text, long style, Bitmap _onPicture, Bitmap _offPicture, boolean drawText) {
        super(style);

        _font = getFont();
        _label = text;
        if (drawText == TextOnBackgroundButtonField.DO_NOT_DRAW_TEXT) {
            _labelHeight = _offPicture.getHeight();
            _labelWidth = _offPicture.getWidth();
        } else {
            setDrawText(true);
            _labelHeight = Math.max(_offPicture.getHeight(), _font.getHeight());
            _labelWidth = Math.max(_offPicture.getWidth(), _font.getAdvance(_label));
        }
        this._onPicture = _onPicture;
        this._offPicture = _offPicture;
        _currentPicture = _offPicture;
    }

    /**
     * @return The text on the button
     */
    public String getText() {
        return _label;
    }

    /**
     * Field implementation.
     *
     * @see net.rim.device.api.ui.Field#getPreferredHeight()
     */
    public int getPreferredHeight() {
        return _labelHeight;
    }

    /**
     * Field implementation.
     *
     * @see net.rim.device.api.ui.Field#getPreferredWidth()
     */
    public int getPreferredWidth() {
        return _labelWidth;
    }

    /**
     * Field implementation.  Changes the picture when focus is gained.
     *
     * @see net.rim.device.api.ui.Field#onFocus(int)
     */
    protected void onFocus(int direction) {
        _currentPicture = _onPicture;
        invalidate();
    }

    /**
     * Field implementation.  Changes picture back when focus is lost.
     *
     * @see net.rim.device.api.ui.Field#onUnfocus()
     */
    protected void onUnfocus() {
        _currentPicture = _offPicture;
        invalidate();
    }

    /**
     * Field implementation.
     *
     * @see net.rim.device.api.ui.Field#drawFocus(Graphics, boolean)
     */
    protected void drawFocus(Graphics graphics, boolean on) {
        // Do nothing
    }

    /**
     * Field implementation.
     *
     * @see net.rim.device.api.ui.Field#layout(int, int)
     */
    protected void layout(int width, int height) {
        setExtent(Math.min(width, getPreferredWidth()),
                Math.min(height, getPreferredHeight()));
    }

    /**
     * Field implementation.
     *
     * @see net.rim.device.api.ui.Field#paint(Graphics)
     */
    protected void paint(Graphics graphics) {
        // First draw the background colour and picture
        if (isClearBeforePaint()) {
            graphics.setColor(getClearColor());
            graphics.fillRect(0, 0, getWidth(), getHeight());
        }
        graphics.drawBitmap(0, 0, getWidth(), getHeight(), _currentPicture, 0, 0);

        // Then draw the text
        if (isDrawText()) {
            graphics.setColor(Color.BLACK);
            graphics.setFont(_font);
            graphics.drawText(_label, 4, 2,
                    (int) (getStyle() & DrawStyle.ELLIPSIS | DrawStyle.HALIGN_MASK),
                    getWidth() - 6);
        }
    }

    /**
     * Overridden so that the Event Dispatch thread can catch this event
     * instead of having it be caught here..
     *
     * @see net.rim.device.api.ui.Field#navigationClick(int, int)
     */
    protected boolean navigationClick(int status, int time) {
        fieldChangeNotify(133);
        return true;
    }

}

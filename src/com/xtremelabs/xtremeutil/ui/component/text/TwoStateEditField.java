package com.xtremelabs.xtremeutil.ui.component.text;


import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.VerticalFieldManager;

public abstract class TwoStateEditField extends Manager {
    private static final int HORIZONTAL_MARGIN = 10;
    private static final int VERTICAL_PADDING = 6;
    private static final int HORIZONTAL_PADDING = 5;

    private volatile boolean previouslyEditable = false;


    private VerticalFieldManager verticalFieldManager;
    protected Bitmap backgroundBitmap;
    private EditField editField;
    private RichTextField richTextField;

    public TwoStateEditField(boolean startEditable, EditField baseEditField) {
        super(NO_HORIZONTAL_SCROLLBAR | NO_VERTICAL_SCROLLBAR);
        editField = baseEditField;
        verticalFieldManager = new VerticalFieldManager(HORIZONTAL_SCROLL);
        setBackgroundBitmap();
        richTextField = new RichTextField("", NON_FOCUSABLE);
        setFont("BBClarity", Font.BOLD, 12);
        verticalFieldManager.add(richTextField);
        editField.setText("");

        setChildEditable(startEditable);
        add(verticalFieldManager);
    }

    protected abstract void setBackgroundBitmap();

    public void paintBackground(Graphics graphics) {
        graphics.drawBitmap(0, 0, getPreferredWidth(), getPreferredHeight(), backgroundBitmap, 0, 0);
    }

    public int getPreferredWidth() {
        return backgroundBitmap.getWidth() + 2 * HORIZONTAL_MARGIN;
    }

    public int getPreferredHeight() {
        return backgroundBitmap.getHeight() + 2 * VERTICAL_PADDING;
    }

    protected void sublayout(int width, int height) {
        setPositionChild(verticalFieldManager, HORIZONTAL_MARGIN, VERTICAL_PADDING);
        layoutChild(verticalFieldManager,
                getPreferredWidth() - 3 * HORIZONTAL_MARGIN - HORIZONTAL_PADDING,
                getPreferredHeight());

        setExtent(getPreferredWidth(), getPreferredHeight());
    }

    public synchronized void setChildEditable(boolean editable) {
//        editField.setEditable(editable);
        if (editable) {
            if (previouslyEditable) {
                return;
            }
            verticalFieldManager.delete(richTextField);
            verticalFieldManager.add(editField);
            previouslyEditable = true;
        } else {
            if (!previouslyEditable) {
                return;
            }
            richTextField.setText(editField.getText());
            verticalFieldManager.delete(editField);
            verticalFieldManager.add(richTextField);
            previouslyEditable = false;
        }
    }

    protected boolean trackwheelUnclick(int status, int time) {
        return true;
    }


    public String getText() {
        if (editField == null) {
            return richTextField.getText();
        }
        String editFieldText = editField.getText();
        return editFieldText != null ? editFieldText : richTextField.getText();
    }

    protected boolean keyChar(char c, int status, int time) {
        if (c == Characters.ENTER) {
            return true;
        }
        return super.keyChar(c, status, time);
    }

    protected void setFont(String fontFamilyName, int fontStyle, int fontHeight) {
        try {
            editField.setFont(FontFamily.forName(fontFamilyName).getFont(fontStyle, fontHeight));
            richTextField.setFont(FontFamily.forName(fontFamilyName)
                    .getFont(Font.ITALIC | fontStyle | Font.ENGRAVED_EFFECT, fontHeight));
        } catch (ClassNotFoundException e) {
            XLogger.error(getClass(), "can't set custom font");
        }
    }
}

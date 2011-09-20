package com.xtremelabs.xtremeutil.ui.component.text;


import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.VerticalFieldManager;


public class EditFieldWithBackground extends Manager {
    private static final int LEFT_MARGIN = 10;
    private static final int RIGHT_MARGIN = 5;

    private VerticalFieldManager verticalFieldManager;
    protected Bitmap backgroundBitmap;
    private EditField editField = new EditField(EDITABLE) {
        public int getPreferredHeight() {
            return Math.min(backgroundBitmap.getWidth(), getFont().getHeight());
        }
    };

    protected EditFieldWithBackground(Bitmap backgroundBitmap) {
        super(NO_HORIZONTAL_SCROLLBAR | NO_VERTICAL_SCROLLBAR);
        verticalFieldManager = new VerticalFieldManager(HORIZONTAL_SCROLL);
        this.backgroundBitmap = backgroundBitmap;

        setFont("BBClarity", Font.PLAIN, 12);
        verticalFieldManager.add(editField);
        editField.setText("");
        add(verticalFieldManager);
    }

    public void paintBackground(Graphics graphics) {
        graphics.drawBitmap(0, 0, getPreferredWidth(), getPreferredHeight(), backgroundBitmap, 0, 0);
    }

    protected void sublayout(int i, int i1) {
        setPositionChild(verticalFieldManager,
                LEFT_MARGIN, (getPreferredHeight() - editField.getPreferredHeight()) / 2);
        layoutChild(verticalFieldManager,
                getPreferredWidth() - LEFT_MARGIN - RIGHT_MARGIN, getPreferredHeight());

        setExtent(getPreferredWidth(), getPreferredHeight());
    }

    public int getPreferredWidth() {
        return backgroundBitmap.getWidth();
    }

    public int getPreferredHeight() {
        return backgroundBitmap.getHeight();
    }

    protected void setFont(String fontFamilyName, int fontStyle, int fontHeight) {
        try {
            editField.setFont(FontFamily.forName(fontFamilyName).getFont(fontStyle, fontHeight));
        } catch (ClassNotFoundException e) {
            XLogger.error(getClass(), "can't set custom font");
        }
    }

    public String getText() {
        return editField.getText();
    }
}

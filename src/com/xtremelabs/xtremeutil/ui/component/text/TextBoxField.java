package com.xtremelabs.xtremeutil.ui.component.text;


import com.xtremelabs.xtremeutil.util.logger.XLogger;
import com.xtremelabs.xtremeutil.util.concurrency.UITask;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.VerticalFieldManager;

public abstract class TextBoxField extends Manager {
    protected int margin;
    private RichTextField textField;
    protected Bitmap backgroundBitmap;
    private VerticalFieldManager verticalManager = new VerticalFieldManager(VERTICAL_SCROLL | FIELD_HCENTER);

    public TextBoxField(final boolean selectable, String fontFamilyName, int fontStyle, int fontHeight) {
        super(NO_HORIZONTAL_SCROLL);
        loadBackgroundBitmap();
        backgroundBitmap = (backgroundBitmap == null) ? new Bitmap(300, 200) : backgroundBitmap;
        setMargin();
        textField = new TextField(selectable);
        setFont(fontFamilyName, fontStyle, fontHeight);
        verticalManager.add(textField);
        add(verticalManager);
    }

    public TextBoxField() {
        this(true, "BBClarity", Font.PLAIN, 12);
    }

    protected abstract void setMargin();

    protected abstract void loadBackgroundBitmap();


    protected void sublayout(int availableWidth, int availableHeight) {
        setPositionChild(verticalManager, margin, margin);
        layoutChild(verticalManager, getPreferredWidth() - 2 * margin, getPreferredHeight() - 2 * margin);
        setExtent(getPreferredWidth(), getPreferredHeight());
    }

    public int getPreferredWidth() {
        return backgroundBitmap.getWidth();
    }

    public int getPreferredHeight() {
        return backgroundBitmap.getHeight();
    }

    public void paintBackground(Graphics graphics) {
        graphics.drawBitmap(0, 0, getPreferredWidth(), getPreferredHeight(), backgroundBitmap, 0, 0);
    }

    public void setText(final String text) {
        new UITask() {
            public void execute() {
                if (text == null || text.length() < 1) {
                    textField.setText("N/A");
                    return;
                }
                textField.setText(text);
            }
        }.invokeLater();
    }

    public String getText() {
        synchronized (Application.getEventLock()) {
            return textField.getText();
        }
    }

    protected void setFont(String fontFamilyName, int fontStyle, int fontHeight) {
        try {
            textField.setFont(FontFamily.forName(fontFamilyName).getFont(fontStyle, fontHeight));
        } catch (ClassNotFoundException e) {
            XLogger.error(getClass(), "can't set custom font");
        }
    }

    private static class TextField extends RichTextField {
        private boolean selectable = true;

        private TextField(final boolean selectable) {
            this.selectable = selectable;
        }

        public boolean isSelectable() {
            return selectable;
        }
    }

}
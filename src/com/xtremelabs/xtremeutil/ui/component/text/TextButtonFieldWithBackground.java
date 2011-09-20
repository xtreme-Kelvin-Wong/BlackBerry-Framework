package com.xtremelabs.xtremeutil.ui.component.text;


import com.xtremelabs.xtremeutil.util.logger.XLogger;
import com.xtremelabs.xtremeutil.ui.FieldFactory;
import com.xtremelabs.xtremeutil.util.concurrency.UITask;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.VerticalFieldManager;


public abstract class TextButtonFieldWithBackground extends Manager {
    private static final int HORIZONTAL_MARGIN = 10;
    private static final int TOP_MARGIN = 7;
    private static final int BOTTOM_MARGIN = 10;

    protected Bitmap backgroundBitmap;
    VerticalFieldManager verticalFieldManager = new VerticalFieldManager(NO_HORIZONTAL_SCROLL
            | NO_VERTICAL_SCROLLBAR | VERTICAL_SCROLL);
    private Bitmap focusBitmap;
    private RichTextField labelField;
    private RichTextField textField;
    private ContentManager contentlManager = new ContentManager() {

    };


    protected TextButtonFieldWithBackground(String label, int labelColor, Bitmap backgroundBitmap, Bitmap focusBitmap) {
        super(NO_HORIZONTAL_SCROLL | NO_HORIZONTAL_SCROLLBAR | NO_VERTICAL_SCROLLBAR);

        this.backgroundBitmap = backgroundBitmap;
        this.focusBitmap = focusBitmap;
        labelField = FieldFactory.makeRichTextField(label, false, NON_FOCUSABLE, labelColor);
        textField = FieldFactory.makeRichTextField("Loading...", false, FOCUSABLE | FIELD_TOP, Color.BLACK);

        setFont("BBClarity", Font.PLAIN, 9);
        verticalFieldManager.add(textField);
        contentlManager.add(labelField);
        contentlManager.add(verticalFieldManager);
        add(contentlManager);
    }

    public abstract boolean handleAction();


    protected void sublayout(int i, int i1) {
        setPositionChild(contentlManager, HORIZONTAL_MARGIN, TOP_MARGIN);
        layoutChild(contentlManager,
                getPreferredWidth() - 2 * HORIZONTAL_MARGIN, getPreferredHeight() - TOP_MARGIN - BOTTOM_MARGIN);
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


    protected void paint(Graphics graphics) {
        super.paint(graphics);
//        graphics.pushContext(0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), 0, 0);
        if (isFocus()) {
//            graphics.setColor(Color.RED);
//            graphics.drawRect(1,1,focusBitmap.getWidth()-2,focusBitmap.getHeight()-2);
//            graphics.drawRect(2,2,focusBitmap.getWidth()-4,focusBitmap.getHeight()-4);
            graphics.drawBitmap(0, 0, focusBitmap.getWidth(), focusBitmap.getHeight(), focusBitmap, 0, 0);
        }
//        graphics.popContext();

    }

    public void onFocus(int direction) {
        super.onFocus(direction);
        invalidate();
    }


    public void onUnfocus() {
        super.onUnfocus();
        invalidate();
    }

    public void setFont(String fontFamilyName, int fontStyle, int fontHeight) {
        try {
            Font font = FontFamily.forName(fontFamilyName).getFont(fontStyle, fontHeight);
            labelField.setFont(font);
            textField.setFont(font);
        } catch (ClassNotFoundException e) {
            XLogger.error(getClass(), "can't set custom font");
        }
    }

    public void setText(final String text) {
        new UITask() {
            public void execute() {
                textField.setText(text);
            }
        }.invokeLater();
    }

    protected boolean keyChar(char c, int status, int time) {
        if (c == Characters.ENTER) {
            return handleAction();
        }
        return super.keyChar(c, status, time);
    }

    protected boolean trackwheelUnclick(int status, int time) {
        return handleAction();
    }

    private class ContentManager extends Manager {
        protected ContentManager() {
            super(NO_HORIZONTAL_SCROLL);
        }

        public void sublayout(int w, int h) {
            setPositionChild(labelField, 0, 0);
            layoutChild(labelField, this.getPreferredWidth() / 3, this.getPreferredHeight());

            setPositionChild(verticalFieldManager, this.getPreferredWidth() / 3, 0);
            layoutChild(verticalFieldManager, this.getPreferredWidth() * 2 / 3, this.getPreferredHeight());

            setExtent(this.getPreferredWidth(), this.getPreferredHeight());
        }

        public int getPreferredWidth() {
            return backgroundBitmap.getWidth() - 2 * HORIZONTAL_MARGIN;
        }

        public int getPreferredHeight() {
            return backgroundBitmap.getHeight() - TOP_MARGIN - BOTTOM_MARGIN;
        }
    }
}

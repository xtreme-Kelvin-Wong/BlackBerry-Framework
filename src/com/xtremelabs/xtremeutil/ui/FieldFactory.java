package com.xtremelabs.xtremeutil.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.*;


public class FieldFactory {

    public FieldFactory() {
    }

    static public LabelField makeLabelField(String text, long style, int color) {
        return makeLabelField(text, style, color, Theme.DEFAULT_FONT);
    }

    static public LabelField makeLabelField(String text, final int width, final int height, Font font) {
        LabelField field = new LabelField(text) {
            protected void paint(Graphics graphics) {
                graphics.setColor(Color.WHITE);
                super.paint(graphics);
            }

            protected void sublayout() {
                setExtent(width, height);
            }
        };
        field.setFont(font);
        return field;
    }

    static public LabelField makeLabelField(String text, long style, int fontStyle, int fontHeight) {
        Font font = Theme.DEFAULT_FONT.getFontFamily().getFont(fontStyle, fontHeight);
        return makeLabelField(text, style, Theme.DEFAULT_COLOR, font);
    }

    static public LabelField makeLabelField(String text, long style, int color, Font font) {
        return makeLabelFieldWithBackground(text, style, Color.WHITE, color, font);
    }

    static public LabelField makeLabelFieldWithBackground(String text, long style, int textColor, int backgroundColor) {
        return makeLabelFieldWithBackground(text, style, textColor, backgroundColor, null);
    }

    static public LabelField makeLabelFieldWithBackground(String text, long style, final int textColor,
                                                          final int backgroundColor, Font font) {
        LabelField field = new LabelField(text, style) {
            protected void paint(Graphics graphics) {
                graphics.pushContext(graphics.getClippingRect(), 0, 0);
                graphics.setColor(textColor);
                super.paint(graphics);
                graphics.popContext();
            }

            protected void paintBackground(Graphics graphics) {
                if (backgroundColor == Color.WHITE) {
                    super.paintBackground(graphics);
                    return;
                }
                graphics.pushContext(graphics.getClippingRect(), 0, 0);
                graphics.setColor(backgroundColor);
                graphics.fillRect(0, 0, Display.getWidth(), Display.getHeight());
                graphics.popContext();
            }

            public int getPreferredWidth() {
                return getFont().getAdvance(getText());
            }

            public int getPreferredHeight() {
                return getFont().getHeight();
            }
        };
        if (font != null) {
            field.setFont(font);
        }
        return field;
    }

    public static Field makeLinkField(final String text, final Runnable task, final int color) {
        return makeLinkField(text, task, color, Theme.DEFAULT_FONT);
    }

    public static Field makeLinkField(final String text, final Runnable task, final int color, final Font font) {

        return new LabelField(text, Field.FOCUSABLE | Field.USE_ALL_WIDTH) {

            protected void paint(Graphics graphics) {

                graphics.pushContext(graphics.getClippingRect(), 0, 0);
                if (font != null) {
                    graphics.setFont(font);
                }
                graphics.setColor(color);
                super.paint(graphics);
                graphics.popContext();
            }

            protected boolean trackwheelUnclick(int i, int i1) {
                task.run();
                return true;
            }
        };
    }

    public static Field makeLinkField(final String text, final Runnable task, final Font font) {

        return new LabelField(text, Field.FOCUSABLE | Field.USE_ALL_WIDTH) {

            protected void paint(Graphics graphics) {

                graphics.pushContext(graphics.getClippingRect(), 0, 0);
                if (font != null) {
                    graphics.setFont(font);
                }

                super.paint(graphics);
                graphics.popContext();
            }

            protected boolean trackwheelUnclick(int i, int i1) {
                task.run();
                return true;
            }
        };
    }


    static public EditField makeEditField(long style, final int color, final boolean selectable) {
        final String label = "";
        final String initial = "";

        return new EditField(label, initial, 100, style) {
            protected void paint(Graphics graphics) {
                graphics.pushContext(graphics.getClippingRect(), 0, 0);
                graphics.setColor(color);
                super.paint(graphics);
                graphics.popContext();
            }

            public boolean isSelectable() {
                return selectable;
            }
        };
    }

    static public EditField makeEditField(final String text, long style, final int color, final Font font) {
        EditField editField = new EditField(style) {

            protected void paint(Graphics graphics) {
                graphics.pushContext(graphics.getClippingRect(), 0, 0);
                graphics.setColor(color);
                super.paint(graphics);
                graphics.popContext();
            }
        };

        editField.setText(text);
        editField.setFont(font);
        return editField;
    }

    static public RichTextField makeRichTextField(final String text, final boolean selectable, final long style,
                                                  final int color) {
        return new RichTextField(text, style) {
            protected void paint(Graphics graphics) {
                graphics.pushContext(graphics.getClippingRect(), 0, 0);
                graphics.setColor(color);
                super.paint(graphics);
                graphics.popContext();
            }

            public boolean isSelectable() {
                return selectable;
            }
        };
    }

    static public RichTextField makeRichTextField(final String text, final boolean selectable, final long style) {
        return new RichTextField(text, style) {
            public boolean isSelectable() {
                return selectable;
            }
        };
    }

    static public RichTextField makeActiveRichTextField(final String text, final boolean selectable, final long style,
                                                        final int color, final Font font) {
        ActiveRichTextField textField = new ActiveRichTextField(text, style) {
            protected void paint(Graphics graphics) {
                graphics.pushContext(graphics.getClippingRect(), 0, 0);
                graphics.setColor(color);
                super.paint(graphics);
                graphics.popContext();
            }

            public boolean isSelectable() {
                return selectable;
            }

            public void onFocus(int direction) {
                super.onFocus(direction);
            }

            public void onUnfocus() {
                super.onUnfocus();
            }

        };
        if (font != null) {
            textField.setFont(font);
        }
        return textField;
    }


    /* Spacer helpers */
    static public Field makeVerticalSpacerField(final int height) {
        return new Field(Field.NON_FOCUSABLE) {
            public int getPreferredWidth() {
                return 1;
            }

            public int getPreferredHeight() {
                return height;
            }

            protected void layout(int w, int h) {
                setExtent(1, height);
            }

            protected void paint(Graphics graphics) {
            }
        };
    }

    static public Field makeHorizontalSpacerField(final int width) {
        return new Field(Field.NON_FOCUSABLE) {
            public int getPreferredWidth() {
                return width;
            }

            public int getPreferredHeight() {
                return 1;
            }

            protected void layout(int w, int h) {
                setExtent(width, 1);
            }

            protected void paint(Graphics graphics) {
            }
        };
    }

    static public Field makeSquareSpacerField(final int sideDimension, boolean focusable) {
        final Bitmap bitmap = GuiUtils.getEmptyTransparentBitmap(sideDimension, sideDimension);
        return new BitmapField(bitmap, (focusable ? Field.FOCUSABLE : Field.NON_FOCUSABLE)) {
            public int getPreferredWidth() {
                return sideDimension;
            }

            public int getPreferredHeight() {
                return sideDimension;
            }

            protected void onUnfocus() {
                super.onUnfocus();
                setBitmap(bitmap);
            }
        };
    }

    static public Field makeFocusMarker() {
        return makeSquareSpacerField(2, true);
    }

    static public LabelField makeVerticalSpacerField() {
        return new LabelField("", 0);
    }

    static public LabelField makeHorizontalSpacerField() {
        return new LabelField("", 0);
    }


    public static PasswordEditField makePasswordEditField(long style, final int color) {
        final String label = "";
        final String initial = "";

        return new PasswordEditField(label, initial, 100, style) {
            protected void paint(Graphics graphics) {
                graphics.pushContext(graphics.getClippingRect(), 0, 0);
                graphics.setColor(color);
                super.paint(graphics);
                graphics.popContext();
            }

            public boolean isSelectable() {
                return false;
            }
        };
    }

}
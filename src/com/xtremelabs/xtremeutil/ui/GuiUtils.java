package com.xtremelabs.xtremeutil.ui;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.util.StringUtilities;

import java.util.Vector;

public class GuiUtils {
    private static final int LIGHT_BLUE = 0x2260CC;
    private static final int LIGHT_GREY = 0xAAAAAA;
    private static final int LIGHTER_GREY = 0xCCCCCC;
    private static final int LIGHT_BROWN = 0xAA7744;
    private static final int DARK_BLUE = 0x222275;
    private static final int DARK_BROWN = 0x220F00;

    // CSS colors: http://www.w3schools.com/css/css_colornames.asp
    private static final int BISQUE = 0xFFE4C4;
    private static final int CORNSILK = 0xFFF8DC;
    private static final int TAN = 0xD2B48C;
    private static final int MISTY_ROSE = 0xFFE4E1;
    private static final int SEA_SHELL_ISH = 0xFFF8F6;

    private static final int RED = 0xF02F39;
    private static final int GREEN = 0x82D458;
    private static final int BLUE = 0x3366CC;
    private static final int YELLOW = 0xFAF843;

    private static final int WHITE = 0xFFFFFF;
    private static final int WHITE_SMOKE = Color.WHITESMOKE;
    private static final int BLACK = 0x000000;
    private static final int GREY = 0x888888;


    public static Font getFont(String fontFamilyName, int fontStyle, int fontSize) {
        try {
            FontFamily fontFamily = FontFamily.forName(fontFamilyName);

            return fontFamily.getFont(fontStyle, fontSize);
        } catch (ClassNotFoundException e) {
//      Logger.getInstance().log("error in getFont", e);
            return Font.getDefault();
        }
    }

    public static EncodedImage resizeImage(EncodedImage image, int desiredWidth, int desiredHeight) {
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        double imgAspectRatio = (imgWidth + 0.001) / imgHeight;
        double aspectRatio = (desiredWidth + 0.001) / desiredHeight;
        int scale = 0;

        if (imgAspectRatio >= aspectRatio) {
            int x = Fixed32.toFP(1);
            x = Fixed32.div(x, desiredWidth);
            x = Fixed32.mul(x, imgWidth);
            scale = x;
        } else if (imgAspectRatio < aspectRatio) {
            int x = Fixed32.toFP(1);
            x = Fixed32.div(x, desiredHeight);
            x = Fixed32.mul(x, imgHeight);
            scale = x;
        }
        image = image.scaleImage32(scale, scale);
        return image;
    }

// TODO: fix below: if width or height is less than 2 the transparent color may not work

    public static Bitmap getEmptyTransparentBitmap(int width, int height) {
        Bitmap bitmap = new Bitmap(width, height);
        bitmap.createAlpha(Bitmap.ALPHA_BITDEPTH_8BPP);
        int[] argb = new int[width * height];
        int length = width * height;
        for (int i = 0; i < length; i++) {
            argb[i] = 0x00FFFFFF;
        }
        bitmap.setARGB(argb, 0, width, 0, 0, width, height);

        return bitmap;
    }

    public static Bitmap getBitmapFromRuntimeStore(String resourceName) {
        long runtimeStoreId = StringUtilities.stringHashToLong(new StringBuffer(GuiUtils.class.getName()).append(resourceName).toString());
        RuntimeStore store = RuntimeStore.getRuntimeStore();
        Object runtimeObj = store.get(runtimeStoreId);
        if (runtimeObj != null) {
            try {
                return (Bitmap) runtimeObj;
            } catch (ClassCastException e) {
                XLogger.error(GuiUtils.class, "Object for id already occupied\n" + e);
            }
        }
        Bitmap bitamp = Bitmap.getBitmapResource(resourceName);
        store.replace(runtimeStoreId, bitamp);
        return bitamp;
    }

    public static Bitmap getOnePixelColoredBitmap(int color) {
        Bitmap bitmap = new Bitmap(1, 1);
        bitmap.createAlpha(Bitmap.ALPHA_BITDEPTH_8BPP);
        int[] argb = new int[1];
        argb[1] = 0xFF000000 + color;
        bitmap.setARGB(argb, 0, 1, 0, 0, 1, 1);

        return bitmap;
    }


    public static Font getFont(int fontStyle, int fontSize) {
        return getFont(Font.getDefault().getFontFamily().toString(), fontStyle, fontSize);
    }


    public static int getApplicationColor(String colorLabel) {
        if (colorLabel == null) {
            return BLACK;
        }
        if (colorLabel.equalsIgnoreCase("Red")) {
            return RED;
        }
        if (colorLabel.equalsIgnoreCase("Green")) {
            return GREEN;
        }
        if (colorLabel.equalsIgnoreCase("Blue")) {
            return BLUE;
        }
        if (colorLabel.equalsIgnoreCase("Yellow")) {
            return YELLOW;
        }
        if (colorLabel.equalsIgnoreCase("LightBlue")) {
            return LIGHT_BLUE;
        }
        if (colorLabel.equalsIgnoreCase("LightGrey")) {
            return LIGHT_GREY;
        }
        if (colorLabel.equalsIgnoreCase("LighterGrey")) {
            return LIGHTER_GREY;
        }
        if (colorLabel.equalsIgnoreCase("DarkBlue")) {
            return DARK_BLUE;
        }
        if (colorLabel.equalsIgnoreCase("DarkBrown")) {
            return DARK_BROWN;
        }
        if (colorLabel.equalsIgnoreCase("LightBrown")) {
            return LIGHT_BROWN;
        }
        if (colorLabel.equalsIgnoreCase("Bisque")) {
            return BISQUE;
        }
        if (colorLabel.equalsIgnoreCase("Cornsilk")) {
            return CORNSILK;
        }
        if (colorLabel.equalsIgnoreCase("Tan")) {
            return TAN;
        }
        if (colorLabel.equalsIgnoreCase("MistyRose")) {
            return MISTY_ROSE;
        }
        if (colorLabel.equalsIgnoreCase("SeaShell")) {
            return SEA_SHELL_ISH;
        }
        if (colorLabel.equalsIgnoreCase("White")) {
            return WHITE;
        }
        if (colorLabel.equalsIgnoreCase("WhiteSmoke")) {
            return WHITE_SMOKE;
        }
        if (colorLabel.equalsIgnoreCase("Grey")) {
            return GREY;
        }
        return BLACK;
    }


    public static Vector labelFieldsOfEveryFont() {
        Vector fields = new Vector();
        FontFamily[] families = FontFamily.getFontFamilies();
        int[] styles = new int[]{Font.PLAIN, Font.BOLD, Font.ITALIC};
        String[] styleNames = new String[]{"Plain", "Bold", "Italic"};
        for (int i = 0; i < families.length; i++) {
            FontFamily family = families[i];

            for (int height = 12; height < 20; height += 2) {
                for (int style = 0; style < styles.length; style++) {
                    LabelField field = new LabelField(new
                            StringBuffer().append(family.getName()).append(" ").append(height).append(" ").append(styleNames[style]).toString(),
                            Field.FOCUSABLE);
                    field.setFont(family.getFont(styles[style], height));
                    fields.addElement(field);
                }
                LabelField field = new LabelField(" ", Field.NON_FOCUSABLE);
                fields.addElement(field);
            }
        }

        return fields;
    }

    public static Vector labelFieldsOfEveryColor() {
        Vector fields = new Vector();
        int i = 0;
        String sample_color_lable = "Sample Colour";
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.ALICEBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.ANTIQUEWHITE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.AQUA));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.AQUAMARINE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.AZURE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.BEIGE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.BISQUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.BLACK));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.BLANCHEDALMOND));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.BLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.BLUEVIOLET));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.BROWN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.BURLYWOOD));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.CADETBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.CHARTREUSE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.CHOCOLATE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.CORAL));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.CORNFLOWERBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.CORNSILK));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.CRIMSON));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.CYAN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKCYAN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKGOLDENROD));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKGRAY));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKGREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKKHAKI));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKMAGENTA));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKOLIVEGREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKORANGE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKORCHID));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKRED));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKSALMON));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKSEAGREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKSLATEBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKSLATEGRAY));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKTURQUOISE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DARKVIOLET));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DEEPPINK));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DEEPSKYBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DIMGRAY));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.DODGERBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.FIREBRICK));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.FLORALWHITE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.FORESTGREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.FUCHSIA));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.GAINSBORO));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.GHOSTWHITE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.GOLD));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.GOLDENROD));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.GRAY));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.GREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.GREENYELLOW));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.HONEYDEW));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.HOTPINK));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.INDIANRED));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.INDIGO));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.IVORY));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.KHAKI));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LAVENDER));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LAVENDERBLUSH));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LAWNGREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LEMONCHIFFON));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIGHTBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIGHTCORAL));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIGHTCYAN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIGHTGOLDENRODYELLOW));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIGHTGREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIGHTGREY));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIGHTPINK));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIGHTSALMON));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIGHTSEAGREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIGHTSKYBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIGHTSLATEGRAY));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIGHTSTEELBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIGHTYELLOW));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIME));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LIMEGREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.LINEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MAGENTA));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MAROON));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MEDIUMAQUAMARINE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MEDIUMBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MEDIUMORCHID));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MEDIUMPURPLE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MEDIUMSEAGREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MEDIUMSLATEBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MEDIUMSPRINGGREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MEDIUMTURQUOISE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MEDIUMVIOLETRED));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MIDNIGHTBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MINTCREAM));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MISTYROSE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.MOCCASIN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.NAVAJOWHITE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.NAVY));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.OLDLACE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.OLIVE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.OLIVEDRAB));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.ORANGE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.ORANGERED));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.ORCHID));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.PALEGOLDENROD));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.PALEGREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.PALETURQUOISE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.PALEVIOLETRED));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.PAPAYAWHIP));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.PEACHPUFF));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.PERU));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.PINK));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.PLUM));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.POWDERBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.PURPLE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.RED));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.ROSYBROWN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.ROYALBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.SADDLEBROWN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.SALMON));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.SANDYBROWN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.SEAGREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.SEASHELL));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.SIENNA));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.SILVER));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.SKYBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.SLATEBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.SLATEGRAY));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.SNOW));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.SPRINGGREEN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.STEELBLUE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.TAN));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.TEAL));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.THISTLE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.TOMATO));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.TURQUOISE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.VIOLET));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.WHEAT));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.WHITE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.WHITESMOKE));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.YELLOW));
        fields.addElement(FieldFactory.makeLabelFieldWithBackground(sample_color_lable + (++i), LabelField.FOCUSABLE, Color.BLACK, Color.YELLOWGREEN));

        return fields;
    }
}

package com.xtremelabs.xtremeutil.ui;

import com.xtremelabs.xtremeutil.device.api.platform.OsVersion;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;

public class Theme {

    public static String DEFAULT_FONTNAME = "BBClarity";
    //360x480                    //480x320 & 320x240
    public static final int DEFAULT_SIZE = Display.getHeight() >= 480 ? Display.getWidth() / 17 : Display.getWidth() / 21;
    public static final int SMALL_SIZE = Display.getHeight() >= 480 ? Display.getWidth() / 22 : Display.getWidth() / 24;
    public static final int LARGE_SIZE = Display.getHeight() >= 480 ? Display.getWidth() / 15 : Display.getWidth() / 18;
    public static final int EXTRA_LARGE_SIZE = Display.getHeight() >= 480 ? Display.getWidth() / 14 : Display.getWidth() / 17;

    public static final Font SETTINGS_LABEL_FONT = Font.getDefault().derive(Font.PLAIN, Font.getDefault().getHeight() - 3);
    public static final Font SETTINGS_CHOICE_FONT = Font.getDefault();

    public static final Font DEFAULT_FONT = Font.getDefault().derive(Font.PLAIN, DEFAULT_SIZE);

    public static final Font DEFAULT_BOLD_FONT = Font.getDefault().derive(Font.BOLD, DEFAULT_SIZE);
    public static final Font DEFAULT_ITALIC_FONT = Font.getDefault().derive(Font.ITALIC, DEFAULT_SIZE);

    public static final Font SMALL_FONT = Font.getDefault().derive(Font.PLAIN, SMALL_SIZE);

    public static final Font LARGE_FONT = Font.getDefault().derive(Font.PLAIN, LARGE_SIZE);
    public static final Font LARGE_BOLD_FONT = Font.getDefault().derive(Font.BOLD, LARGE_SIZE);
    public static final Font LARGE_FONT_UNDERLINED = Font.getDefault().derive(Font.UNDERLINED, LARGE_SIZE);

    public static final Font EXTRA_LARGE_FONT = Font.getDefault().derive(Font.PLAIN, EXTRA_LARGE_SIZE);
    public static final Font TITLE_FONT = DEFAULT_FONT;

    public static final Font MAIN_FONT = DEFAULT_FONT;

    public static final int DEFAULT_COLOR = OsVersion.getOsVersion().isAtLeast(4, 6, 0) ? Color.WHITE : Color.BLACK;
    public static final int BACKGROUND_COLOR = OsVersion.getOsVersion().isAtLeast(4, 6, 0) ? Color.BLACK : Color.GRAY;
}

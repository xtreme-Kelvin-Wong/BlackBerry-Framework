package com.xtremelabs.xtremeutil.ui.component.misc;

import com.xtremelabs.xtremeutil.util.concurrency.UITask;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;


/**
 * Created by IntelliJ IDEA. User: xtremelabs Date: Sep 28, 2009 Time: 7:46:48
 * PM To change this template use File | Settings | File Templates.
 */
public class Loading {
    private static Font LOADING_FONT;
    private final static String LOADING_TEXT = "Loading...";

    private static PopupScreen popup;

    public static final void startLoading() {
        startLoading(LOADING_TEXT);
    }

    public static final void startLoading(final String loadingStr) {
        if (popup != null) {
            // if the popup is not null,
            // Check if loading screen is the active screen, do nothing
            if (UiApplication.getUiApplication().getActiveScreen() == popup) {
                return;
            } else {
                dismissPopup();
            }
        }
        VerticalFieldManager loadingDelegate = new VerticalFieldManager();

        TextField loadingText = new TextField(Field.READONLY | Field.NON_FOCUSABLE) {
            public int getPreferredWidth() {
                return getFont().getAdvance(getText());
            }

            protected void layout(int width, int height) {
                super.layout(getPreferredWidth(), height);
            }
        };
        loadingText.setText(loadingStr);
        if(LOADING_FONT!=null) {
        	loadingText.setFont(LOADING_FONT);
        }
        loadingDelegate.add(loadingText);
        popup = new PopupScreen(loadingDelegate);

        showPopup();
    }

    public static final void finishLoading() {
        if (popup == null) {
            // if popup is null, then the loading screen is not displayed
            return;
        }
        dismissPopup();
    }

    public static final void setFont(Font font) {
    	LOADING_FONT = font;
    }
    
    private static void showPopup() {
        if (popup != null && !popup.isDisplayed()) {
            if (UiApplication.getUiApplication().isEventThread()) {
                UiApplication.getUiApplication().pushScreen(popup);
            } else {
                new UITask() {
                    public void execute() {
                        UiApplication.getUiApplication().pushScreen(popup);
                    }
                }.invokeAndWait();
            }
        }
    }

    private static void dismissPopup() {
        if (popup != null) {
            if (popup.isDisplayed()) {
                if (UiApplication.getUiApplication().isEventThread()) {
                    UiApplication.getUiApplication().popScreen(popup);
                    popup = null;
                } else {
                    new UITask() {
                        public void execute() {
                            UiApplication.getUiApplication().popScreen(popup);
                            popup = null;
                        }
                    }.invokeAndWait();
                }
            } else {
                popup = null;
            }
        }
    }
}
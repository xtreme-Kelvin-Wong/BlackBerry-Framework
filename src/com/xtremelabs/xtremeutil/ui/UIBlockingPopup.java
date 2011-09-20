package com.xtremelabs.xtremeutil.ui;

import com.xtremelabs.xtremeutil.application.XtremeApplication;
import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.DialogFieldManager;
import net.rim.device.api.ui.container.PopupScreen;

/**
* Created by Xtreme Labs Inc.
* User: Sina Sojoodi
* Date: 12/06/11
* Time: 9:20 PM
*/
public class UIBlockingPopup extends PopupScreen {
    private static UiApplication application = UiApplication.getUiApplication();
    private static UIBlockingPopup instance;

    public UIBlockingPopup(final Manager manager) {
        super(manager, 0);
    }

    public static void show(final String message) {
        if (instance != null && instance.isDisplayed()) {
            application.popScreen(instance);
        }
        DialogFieldManager dfm = new DialogFieldManager();
        Bitmap icon = ApplicationDescriptor.currentApplicationDescriptor().getIcon();
        dfm.setIcon(new BitmapField(icon));
        final RichTextField richTextField = new RichTextField(message, NON_FOCUSABLE);
        richTextField.setText(message);
        dfm.setMessage(richTextField);
        instance = new UIBlockingPopup(dfm);
        application.pushScreen(instance);
    }

    /**
     * @return was displayed previously
     */
    public static boolean hide() {
        if (instance != null && instance.isDisplayed()) {
            try {
                application.popScreen(instance);
                return true;
            } catch (Exception e) {
                XLogger.error(UIBlockingPopup.class, e);
            }
        }
        return false;
    }

    public boolean keyDown(int keyCode, int status) {
        if (Keypad.key(keyCode) == Keypad.KEY_ESCAPE) {
            ((XtremeApplication) application).exit(0);
            return true;
        }
        return super.keyDown(keyCode, status);
    }
}

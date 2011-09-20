package com.xtremelabs.xtremeutil.device.api.screenshot.controllers;

import com.xtremelabs.xtremeutil.device.api.screenshot.utilities.ScreenshotEmailUtil;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.PNGEncodedImage;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.UiEngine;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.DialogClosedListener;

public class ScreenshotEmailPrompt {
	public static void emailPrompt(final PNGEncodedImage png){

		final Dialog emailDialog = new Dialog(Dialog.D_YES_NO, "Email this screenshot?", Dialog.NO,  Bitmap.getPredefinedBitmap(Bitmap.QUESTION), Screen.NO_SYSTEM_MENU_ITEMS);

		synchronized (Application.getEventLock()) {
			UiEngine ui = Ui.getUiEngine();
			ui.pushGlobalScreen(emailDialog, 1, UiEngine.GLOBAL_QUEUE);

			emailDialog.setDialogClosedListener(new DialogClosedListener() {

				public void dialogClosed(Dialog dialog, int choice) {
					if (choice == Dialog.YES) {
						ScreenshotEmailUtil.sendEmail(png);
					}

					//System.exit(0);
					UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
				}
			});
		}

	}
}

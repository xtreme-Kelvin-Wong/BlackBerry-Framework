/*******************************************************************************
 * Copyright (c) 2011 Xtreme Labs Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Xtreme Labs Inc. - initial API and implementation
 ******************************************************************************/
package com.xtremelabs.xtremeutil.device.api.screenshot.controllers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import com.xtremelabs.xtremeutil.device.api.screenshot.ScreenshotBackgroundScreen;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.PNGEncodedImage;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.UiApplication;

public class ScreenshotCapture  {

	private ScreenshotBackgroundScreen screen = new ScreenshotBackgroundScreen();
	
	public ScreenshotCapture(){
		UiApplication.getUiApplication().pushScreen(screen);
		
		ScreenshotEmailPrompt.emailPrompt(makePNG());
	}
	
	public PNGEncodedImage makePNG(){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String strDate = sdf.format(now);
		final String mFileName = System.getProperty("fileconn.dir.photos") + "Screenshot " + strDate + ".png";
		final Bitmap bmp = new Bitmap(Display.getWidth(), Display.getHeight());

		Display.screenshot(bmp);
		screen.setBackground(bmp);
		
		final PNGEncodedImage png = PNGEncodedImage.encode(bmp);
		
		FileConnection fc = null;
		DataOutputStream out = null;
		
		try {
			fc = (FileConnection)Connector.open(mFileName);
			
			if (fc.exists())
			{
				fc.delete();
			}

			fc.create();

			out = fc.openDataOutputStream();
			out.write(png.getData());

		} catch (IOException e) {
			System.out.println(e.toString());
		}
		
		try {
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			if (fc != null) {
				fc.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return png;
	}
}

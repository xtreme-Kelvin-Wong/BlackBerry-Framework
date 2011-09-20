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
package com.xtremelabs.xtremeutil.device.api.screenshot;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class ScreenshotBackgroundScreen extends MainScreen {

	public ScreenshotBackgroundScreen()
	{         
		setTitle("Screenshots");
		
	}
	
	public void setBackground(Bitmap bmp){
		add(new BitmapField(bmp));
	}
	

}

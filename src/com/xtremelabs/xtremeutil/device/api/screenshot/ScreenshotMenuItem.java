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

import com.xtremelabs.xtremeutil.device.api.screenshot.controllers.ScreenshotCapture;

import net.rim.blackberry.api.menuitem.ApplicationMenuItem;
import net.rim.blackberry.api.menuitem.ApplicationMenuItemRepository;
import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.ui.UiApplication;



public class ScreenshotMenuItem extends ApplicationMenuItem {

	//Place commented lines into main to add Screenshot app to system menu
	//static RuntimeStore store = RuntimeStore.getRuntimeStore();
	//final static long ID = 0x88ca79067d23b28cL;

	public ScreenshotMenuItem(){
		//		Object obj = store.get(ID);
		//		if (obj == null){
		//			ApplicationMenuItemRepository repository = ApplicationMenuItemRepository.getInstance();
		//			ScreenshotMenuItem mi = new ScreenshotMenuItem();
		//			repository.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM, mi);
		//
		//			try{
		//				store.put(ID, "xtremestring");
		//			} catch(IllegalArgumentException e){
		//
		//			}
		//			
		//		}
		super(1);
	}

	public Object run(Object context) {
		UiApplication.getApplication().invokeLater(new Runnable() {
			public void run() {
				new ScreenshotCapture();
			}
		}, 100, false);
		return null;
	}

	public String toString() {
		return "Take Screenshot";
	}

}

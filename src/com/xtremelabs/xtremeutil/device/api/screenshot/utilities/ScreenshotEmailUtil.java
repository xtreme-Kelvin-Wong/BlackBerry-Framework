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
package com.xtremelabs.xtremeutil.device.api.screenshot.utilities;

import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.blackberry.api.mail.Message;
import net.rim.blackberry.api.mail.Multipart;
import net.rim.blackberry.api.mail.SupportedAttachmentPart;
import net.rim.blackberry.api.mail.TextBodyPart;
import net.rim.device.api.system.PNGEncodedImage;

public class ScreenshotEmailUtil {

	public static void sendEmail(PNGEncodedImage png) {
		Message msg = new Message();

		try {

			//set a subject for the message
			msg.setSubject("Screenshot - Application error");

			//sets priority
			msg.setPriority(Message.Priority.HIGH);

			Multipart multipart = new Multipart();

			SupportedAttachmentPart attach = new SupportedAttachmentPart(multipart,
					"image/png", "Screenshot.png", png.getData());
			multipart.addBodyPart(attach);

			multipart.addBodyPart(new TextBodyPart(multipart, " "));

			msg.setContent(multipart);

			MessageArguments msgArgs = new MessageArguments(msg);
			Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, msgArgs);
		}
		catch (Exception me) {
			System.err.println(me);
		}
	}	

}

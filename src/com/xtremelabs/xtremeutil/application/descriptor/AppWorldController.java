/**
 * Created by IntelliJ IDEA.
 * User: qikpath
 * Date: 2-Feb-2010
 * Time: 6:59:08 PM
 * To change this template use File | Settings | File Templates.
 */
package com.xtremelabs.xtremeutil.application.descriptor;

import com.xtremelabs.xtremeutil.application.XtremeApplication;
import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.ApplicationDescriptor;

import javax.microedition.content.ContentHandler;
import javax.microedition.content.Invocation;
import javax.microedition.content.Registry;
import java.io.IOException;

public class AppWorldController {
    private static AppWorldController ourInstance = new AppWorldController();

    public static AppWorldController getInstance() {
        return ourInstance;
    }

    private AppWorldController() {
    }

    public void openAppWorld() {
        AppWorldData awd = new AppWorldData(ApplicationDescriptor.currentApplicationDescriptor().getModuleName());
        try {
            openAppWorld(awd.getAppWorldId());
        } catch (Exception e) {
            XLogger.warn(AppVersionChecker.class, new StringBuffer("Error opening appWorld with ID ")
                    .append(awd.getAppWorldId()).append(": ").append(e).toString());
            Browser.getDefaultSession().displayPage("http://appworld.blackberry.com/webstore/");
        }
    }


    /*FROM: DB-00736: How To - Interface with BlackBerry® AppWorld?
    * (http://www.blackberry.com/knowledgecenterpublic/livelink.exe/fetch/2000/348583/1573156/How_To_-_Interface_with_the_BlackBerry_App_World.html?nodeid=1947155&vernum=0)
    *
    */

    /**
     * openAppWorld
     * <p/>
     * Opens the App World pointing to a specific application. <p>
     * Note: This method takes advantage of
     * javax.microedition.content, which was introduced in 4.3.
     * There is no way to open the BlackBerry App World on an older OS.
     * <p/>
     *
     * @param myContentId App World ID of application to open.
     * @throws IllegalArgumentException if myContentId is invalid
     * @throws javax.microedition.content.ContentHandlerException
     *                                  if App World is not installed
     * @throws SecurityException        if access is not permitted
     * @throws java.io.IOException      if access to the registry fails
     */

    public static void openAppWorld(String myContentId) throws IllegalArgumentException,
            SecurityException, IOException {
        final XtremeApplication application = (XtremeApplication) Application.getApplication();
        Registry registry = Registry.getRegistry(application.getClass().getName());
        Invocation invocation = new Invocation(null, null, "net.rim.bb.appworld.Content", true, ContentHandler.ACTION_OPEN);
        invocation.setArgs(new String[]{myContentId});
        boolean mustExit = registry.invoke(invocation);
        if (mustExit) // For strict compliance - this should never happen on a BlackBerry
        {
            application.exit(0);
        }

// TODO: put below in a thread and cancel after a certain time has passed or remove all together     
        // Please note that this response won't be generated
        // until the BlackBerry AppWorld application exits
        // This method will block until that time
//        Invocation response = registry.getResponse(true);
//        if (response.getStatus() != Invocation.OK) {
//            XLogger.warn(AppVersionChecker.class, "Problem invoking BlackBerry® AppWorld?. Error code " + response.getStatus());
//        } else {
//            XLogger.info(AppVersionChecker.class, "BlackBerry® AppWorld? successfully opened.");
//        }
    }
}

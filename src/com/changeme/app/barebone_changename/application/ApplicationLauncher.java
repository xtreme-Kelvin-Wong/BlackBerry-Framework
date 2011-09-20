package com.changeme.app.barebone_changename.application;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import com.xtremelabs.xtremeutil.util.lang.ArrayUtils;
import net.rim.blackberry.api.homescreen.HomeScreen;
import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.DeviceInfo;

public class ApplicationLauncher {

    // TODO: set the application's secret key
    private static final String SECRET_KEY = "???";

    public static String STARTUP_ARG = "startup";

    public static void main(String[] args) {

        StringBuffer argsBuffer = new StringBuffer(
                "---Starting app with args---").append("\n");
        argsBuffer.append(ArrayUtils.stringValue(args));
        argsBuffer.append("---End of args---");

        XLogger.info(ApplicationLauncher.class, argsBuffer.toString());

        try {
           waitIfInStartup();

            boolean invokedOnStartup = (args != null && args.length > 0 && args[0].indexOf(STARTUP_ARG) != -1);
            // Enter the respective event dispatcher for the Application
            // depending on whether it was launched on startup or from the
            // HomeScreen
            if (invokedOnStartup) {
                BareBoneDaemonApplication daemonApplication = BareBoneDaemonApplication.getAppInstance();
                daemonApplication.runOnStartup();
                daemonApplication.enterEventDispatcher();

            } else {
                BareBoneApplication application = new BareBoneApplication(args);
                application.enterEventDispatcher();
            }
        } catch (SecurityException e) {
            String message = "Can't operate due to strict security settings. Please contact your IT administrator.";
            XLogger.error(e.getClass(), message);
            System.exit(1);
        }
    }

    private static void setRolloverIcon() {
        final Bitmap rolloverIcon = Bitmap.getBitmapResource("rollover.png");
        if (null == rolloverIcon)
            return;
        try {
            HomeScreen.setRolloverIcon(rolloverIcon, 0);
        } catch (IllegalArgumentException e) {
            try {
                HomeScreen.setRolloverIcon(rolloverIcon, 1);
            } catch (Throwable t) {
                // ignore
            }
        }
    }

     private static void waitIfInStartup() {
        final ApplicationManager appMgr = ApplicationManager.getApplicationManager();

        boolean inStartup = false;

        while (appMgr.inStartup()) {
            inStartup = true;
            startupSleep(250);
        }

        if (inStartup) {
            if (DeviceInfo.isSimulator()) {
                // We sleep in case of hot swapping
                startupSleep(1500);
            } else {
                startupSleep(750);
            }
        }


    }

    private static void startupSleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // Do nothing
        }
    }

}
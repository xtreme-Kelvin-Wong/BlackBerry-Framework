package com.xtremelabs.xtremeutil.application;

import com.xtremelabs.xtremeutil.device.api.platform.OsVersion;
import com.xtremelabs.xtremeutil.util.logger.XLogger;
import com.xtremelabs.xtremeutil.net.NetworkConnectionController;
import com.xtremelabs.xtremeutil.ui.UIBlockingPopup;
import com.xtremelabs.xtremeutil.util.concurrency.TaskEventListener;
import com.xtremelabs.xtremeutil.util.concurrency.UITask;
import net.rim.blackberry.api.phone.Phone;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import java.util.Timer;

public abstract class AbstractXtremeApplicationImpl extends UiApplication implements XtremeApplication,
        TaskEventListener {
    private Timer timer = new Timer();
    private volatile boolean runTestsOnStartup = false;
    private boolean devmode;

    public void initialize(String[] args) {
        UITask.setGlobalEventListener(this);
        exitIfInferiorOsVersion();
        exitIfScreenResolutionNotSupported();

        // all the spaced delimited args are in the first element
        if (args.length == 0) {
            return;
        }
        String appArgs = args[0];
        if (appArgs.toLowerCase().indexOf(DEVMODE_ARGUMENT) != -1) {
            devmode = true;
            XLogger.info(getClass(), "devmode ON");
        }
        if (appArgs.toLowerCase().indexOf(RUNTESTS_ARGUMENT) != -1) {
            this.runTestsOnStartup = true;
            XLogger.info(getClass(), "tests on startup ON");
        }
        if (appArgs.toLowerCase().indexOf(SIMTESTS_ARGUMENT) != -1 && DeviceInfo.isSimulator()) {
            this.runTestsOnStartup = true;
            XLogger.info(getClass(), "tests on startup (simulator only) ON");
        }
        if (runTestsOnStartup) {
            UITask runTests = new UITask() {
                public void execute() {

                    int choice = Dialog.ask(Dialog.D_YES_NO, "Run tests?");
                    if (choice == Dialog.YES) {
                        getTestRunner().runUnitTests();
                    }
                }

            };
            invokeLater(runTests, 1000L, false);
        }
    }

    public boolean isDevmode() {
        return devmode;
    }

    public void pushGlobalAlertAndExit(String message) {
        pushGlobalAlert(message);
        if (!DeviceInfo.isSimulator()) {
            exit(1);
        } else {
            if (isDevmode()) {
                XLogger.warn(getClass(), "&&&&&&& DEVICE WOULD EXIT APP AT THIS POINT, BUT SIMULATOR DOES NOT &&&&&&&");
            } else {
                exit(1);
            }
        }
    }

    public void pushGlobalAlert(final String message) {
        Application.getApplication().invokeAndWait(new Runnable() {
            public void run() {
                Bitmap PREDEFINED_INFO_BITMAP = Bitmap.getPredefinedBitmap(Bitmap.INFORMATION);
                Dialog dialog = new Dialog(Dialog.D_OK, message, Dialog.OK, PREDEFINED_INFO_BITMAP, Manager.NO_HORIZONTAL_SCROLL);
                Ui.getUiEngine().pushGlobalScreen(dialog, 5, GLOBAL_SHOW_LOWER | GLOBAL_QUEUE | GLOBAL_MODAL);
            }
        });
    }

    public void exit(int systemExitStatus) {
        NetworkConnectionController.getInstance().unregister();
        timer.cancel();
        timer = null;
        UITask.removeGlobalEventListener();
        System.exit(systemExitStatus);
    }

    public Timer getTimer() {
        return timer;
    }

    public static String getBlackberryPin() {
        return Integer.toHexString(DeviceInfo.getDeviceId());
    }

    public String getBlackberryPhoneNumber() {
        String phoneNumber = null;
        try {
            phoneNumber = Phone.getDevicePhoneNumber(false);
        } catch (Exception e) {
            XLogger.error(getClass(), "phone info retrieval was denied " + e);
        }
        return phoneNumber == null ? "" : phoneNumber;
    }

    public String getBlackberryModel() {
        return DeviceInfo.getDeviceName().substring(0, 4);
    }

    public String getOsVersion() {
        return OsVersion.getOsVersion().toString();
    }

    public void pauseUi(String message) {
        synchronized (getEventLock()) {
            UIBlockingPopup.show(message);
        }
    }

    /**
     * @return whether pause-popup was displayed and successfully poped from the stack
     */
    public boolean resumeUi() {
        synchronized (getEventLock()) {
            return UIBlockingPopup.hide();
        }
    }


}

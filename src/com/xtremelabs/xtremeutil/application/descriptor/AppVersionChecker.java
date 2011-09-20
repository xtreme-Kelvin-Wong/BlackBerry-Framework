package com.xtremelabs.xtremeutil.application.descriptor;

import com.xtremelabs.xtremeutil.application.XtremeApplication;
import com.xtremelabs.xtremeutil.net.AppVersionRequest;
import com.xtremelabs.xtremeutil.net.api.Request;
import com.xtremelabs.xtremeutil.net.api.RequestQueue;
import com.xtremelabs.xtremeutil.net.api.ResponseHandler;
import com.xtremelabs.xtremeutil.net.api.ResponseObject;
import com.xtremelabs.xtremeutil.util.concurrency.UITask;
import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.system.Application;
import net.rim.device.api.ui.component.Dialog;

public class AppVersionChecker implements ResponseHandler {
    private static final String ERROR_MESSAGE = "Could not check application version. Please try again later.";
    private static final String INFO_MESSAGE = "Checking application version. Please wait...";
    private boolean manualCheck = false;
    private static JadParser jadParser;
    private String moduleName;
    private AppVersionCheckListener appVersionCheckListener;

    public void requestReturned(Request request, ResponseObject response) {


        final Version serverVersion = ((AppVersionRequest.AppVersionResponse) response).getVersion();

        final Version localVersion;
        if (moduleName != null) {
            localVersion = Version.getAppVersion(moduleName);
        } else {
            localVersion = Version.getAppVersion();
        }

        if (localVersion != null) {
            if (localVersion.compareTo(serverVersion) < 0) {
                new UITask() {
                    public void execute() {
                        if (getJadParser().isFromAppWorld()) {
                            appworldUpdateMayExist(serverVersion, localVersion);
                        } else {
                            serverUpdateNeeded(serverVersion, localVersion);
                        }
                    }
                }.invokeLater();

            } else if (manualCheck && localVersion.compareTo(serverVersion) == 0) {
                final String message = new StringBuffer()
                        .append("Application is up to date. Server version is ")
                        .append(serverVersion)
                        .append(", your version is ")
                        .append(localVersion)
                        .toString();
                new UITask() {
                    public void execute() {
                        Dialog.inform(message);
                    }
                }.invokeLater();

            } else if (manualCheck && localVersion.compareTo(serverVersion) > 0) {
                final String message = new StringBuffer()
                        .append("Your application is newer. Server version is ")
                        .append(serverVersion)
                        .append(", your version is ")
                        .append(localVersion)
                        .toString();
                new UITask() {
                    public void execute() {
                        Dialog.inform(message);
                    }
                }.invokeLater();
            }

        }
        if (appVersionCheckListener != null) {
            appVersionCheckListener.appVersionCheckComplete(true);
        }
    }

    public void requestFailed(Request request, Exception exception, ResponseObject response) {
        if (appVersionCheckListener != null) {
            appVersionCheckListener.appVersionCheckComplete(false);
        }
    }

    public static interface AppVersionCheckListener {
        public void appVersionCheckComplete(boolean success);
    }

    public AppVersionChecker(boolean manual) {
        manualCheck = manual;
    }

    public AppVersionChecker(String codfileName, boolean manual) {
        this(codfileName, manual, null);
    }

    public AppVersionChecker(String codfileName, boolean manual, AppVersionCheckListener listener) {
        manualCheck = manual;
        moduleName = codfileName;
        appVersionCheckListener = listener;
    }

    public void checkVersion(RequestQueue q) {
        AppVersionRequest req = new AppVersionRequest(this);
        req.sendJadRequest(q);
    }

    public static void updateIfNewVersion(AppVersionCheckListener listener, RequestQueue q) {
        if (getJadParser().isAppWorldUpdateAvailable()) {
            appWorldUpdateNeeded();
        } else {
            new AppVersionChecker(null, false, listener).checkVersion(q);
        }
    }

    public static JadParser getJadParser() {
        if (jadParser == null) {
            jadParser = new JadParser();
        }
        return jadParser;
    }

    private void serverUpdateNeeded(Version serverVersion, Version localVersion) {
        String message = new StringBuffer()
                .append("Application needs to be updated. Server version is ")
                .append(serverVersion)
                .append(", your version is ")
                .append(localVersion)
                .append(". Download new version now?")
                .toString();
        int response = Dialog.ask(Dialog.D_YES_NO, message, Dialog.YES);

        if (response == Dialog.YES) {
            Browser.getDefaultSession().displayPage(((XtremeApplication) Application.getApplication()).getJadUrl());
        }
    }

    private static void appWorldUpdateNeeded() {
        String message = "An update is available through BlackBerry® AppWorld?. Download now?";
        int response = Dialog.ask(Dialog.D_YES_NO, message, Dialog.YES);
        if (response == Dialog.YES) {
            AppWorldController.getInstance().openAppWorld();
        }
    }

    private void appworldUpdateMayExist(Version serverVersion, Version localVersion) {
        String message = new StringBuffer()
                .append("A new version of the application will soon be available on BlackBerry® AppWorld?. New update version is")
                .append(serverVersion)
                .append(", your version is ")
                .append(localVersion)
                .append(". Check the App World now?")
                .toString();
        int response = Dialog.ask(Dialog.D_YES_NO, message, Dialog.YES);
        if (response == Dialog.YES) {
            AppWorldController.getInstance().openAppWorld();
        }
    }

    public boolean handleConnectionFailure() {
        return false;
    }


}
package com.xtremelabs.xtremeutil.device.api.platform;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;


public class OsVersion {
    public static final int MAJOR = 0;
    public static final int MINOR = 1;
    public static final int BABY = 2;
    public static final int NO_VERSION = -1;

    private static final int MAX_VERSION_INDICES = 4;

    private int[] versionArray;
    private static OsVersion osVersion;

    private OsVersion() {
        //      DeviceInfo.getPlatformVersion() returned 4.0.0.183 on a blod!!!
        //    String versionString = DeviceInfo.getPlatformVersion();

        String versionString = getDeviceOsVersion();

        initializeVersionArray();

        int startPoint = 0;
        int endPoint;
        int arrayIndex = 0;

        do {
            endPoint = versionString.indexOf('.', startPoint);
            try {
                versionArray[arrayIndex++] =
                        Integer.parseInt(versionString.substring(startPoint, endPoint == -1 ? versionString.length() : endPoint));
            } catch (NumberFormatException e) {
                break;
            } catch (StringIndexOutOfBoundsException e) {
                break;
            }

            startPoint = endPoint + 1;
        } while (arrayIndex < MAX_VERSION_INDICES && endPoint != -1);

    }

    public static OsVersion getOsVersion() {
        if (osVersion == null) {
            osVersion = new OsVersion();
        }
        return osVersion;
    }

    private void initializeVersionArray() {
        versionArray = new int[MAX_VERSION_INDICES];

        for (int i = 0; i < MAX_VERSION_INDICES; i++) {
            versionArray[i] = NO_VERSION;
        }
    }

    public int get(int versionIndex) {
        try {
            return versionArray[versionIndex];
        } catch (ArrayIndexOutOfBoundsException e) {
            return NO_VERSION;
        }
    }

    public boolean isAtLeast(int major, int minor, int baby) {
        return get(MAJOR) > major ||
                get(MAJOR) == major && get(MINOR) > minor ||
                get(MAJOR) == major && get(MINOR) == minor && get(BABY) >= baby;
    }

    public String getStringOsVersion() {
        return new StringBuffer().append(get(MAJOR)).append(".").append(Integer.toString(get(MINOR))).append(".").append(Integer.toString(get(BABY))).toString();
    }

    public static String getDeviceOsVersion() {
        //TODO (find a better way to do this, or wait until we're on 4.3 and above)
        // apprently this is the only way to get the OS version prior to 4.3. In 4.3, you can do DeviceInfo.getSoftwareVersion(),
        // but that doesn't work in 4.2.X.

        // Tried DeviceInfo.getPlatformVersion but it didn't work

        ApplicationDescriptor[] visibleApps = ApplicationManager.getApplicationManager().getVisibleApplications();
        for (int i = visibleApps.length - 1; i >= 0; --i) {
            String appThatShouldAlwaysBeThereAndHaveTheSameVersionAsTheOS = "net_rim_bb_browser_daemon";
            if ((visibleApps[i].getModuleName()).equals(appThatShouldAlwaysBeThereAndHaveTheSameVersionAsTheOS)) {
                final String version = visibleApps[i].getVersion();
                //        Logger.getInstance().log("Device OS version: ", version);
                return version;
            }
        }
        return "";
    }

    public String toString() {
        return new StringBuffer().append(get(MAJOR)).append(".").append(get(MINOR)).append(".").append(get(BABY)).toString();
    }

}




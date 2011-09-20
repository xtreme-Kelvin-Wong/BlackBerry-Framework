package com.xtremelabs.xtremeutil.application.descriptor;

import com.xtremelabs.xtremeutil.util.string.XStringUtilities;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.CodeModuleManager;


public class Version {

    private int major;
    private int minor;
    private int build;

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getBuild() {
        return build;
    }

    public void setBuild(int build) {
        this.build = build;
    }

    public Version(String version) {
        String trim = version.trim();

        int firstPeriod = trim.indexOf(".");
        if (firstPeriod < 0) {
            trim += ".0";
            firstPeriod = trim.indexOf(".");
        }

        int secondPeriod = trim.indexOf(".", firstPeriod + 1);
        if (secondPeriod < 0) {
            trim += ".0";
            secondPeriod = trim.indexOf(".", firstPeriod + 1);
        }

        String major = trim.substring(0, firstPeriod);
        String minor = trim.substring(firstPeriod + 1, secondPeriod);
        String build = trim.substring(secondPeriod + 1, trim.length());


        try {
            this.major = Integer.parseInt(major);
        } catch (NumberFormatException e) {
            this.major = 0;
        }
        try {
            this.minor = Integer.parseInt(minor);
        } catch (NumberFormatException e) {
            this.minor = 0;
        }
        try {
            this.build = Integer.parseInt(build);
        } catch (NumberFormatException e) {
            this.build = 0;
        }
    }

    private Version(int maj, int min, int build) {
        major = maj;
        minor = min;
        this.build = build;
    }

    // modulename is the name of the cod file; ex: speedtest, unify, echelon, ...
    public static Version getAppVersion(String moduleName) {
        int moduleHandle = CodeModuleManager.getModuleHandle(moduleName);
        String stringVersion = null;
        if (moduleHandle != 0) {
            stringVersion = CodeModuleManager.getModuleVersion(moduleHandle);
        }
        if (stringVersion == null || XStringUtilities.count('.', stringVersion) != 2) {
            return null;
        }
        return new Version(stringVersion);
    }


    public static Version getAppVersion() {
        String stringVersion = ApplicationDescriptor.currentApplicationDescriptor().getVersion();
        if (stringVersion == null || XStringUtilities.count('.', stringVersion) != 2) {
            return null;
        }
        return new Version(stringVersion);
    }


    public boolean equals(Object o) {
        if (o != null && o instanceof Version) {
            Version ver = (Version) o;
            return this.major == ver.major &&
                    this.minor == ver.minor &&
                    this.build == ver.build;
        }
        return false;

    }


    public int compareTo(Version ver) {

        if (getMajor() < ver.getMajor()) return -1;
        if (getMajor() > ver.getMajor()) return 1;

        if (getMinor() < ver.getMinor()) return -1;
        if (getMinor() > ver.getMinor()) return 1;

        if (getBuild() < ver.getBuild()) return -1;
        if (getBuild() > ver.getBuild()) return 1;

        //Everything is equal return 0
        return 0;
    }


    public String toString() {
        return major + "." + minor + "." + build;
    }

}

package com.xtremelabs.xtremeutil.net;

import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.WLANInfo;

public class ConnectionStatus {
    public static boolean isWifiConnected() {
        return WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED;
    }

    public static boolean hasDirectCoverage() {
        return CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_DIRECT);
    }

    public static boolean hasBISCoverage() {
        return CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B);
    }

    public static boolean hasMDSCoverage() {
        return CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS);
    }

    public static boolean hasValidConnection() {
        return isWifiConnected() || hasDirectCoverage() || hasBISCoverage() || hasMDSCoverage();
    }
}

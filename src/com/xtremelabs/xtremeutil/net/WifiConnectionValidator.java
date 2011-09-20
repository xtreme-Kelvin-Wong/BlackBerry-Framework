package com.xtremelabs.xtremeutil.net;

import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.WLANConnectionListener;
import net.rim.device.api.system.WLANInfo;


class WifiConnectionValidator implements WLANConnectionListener {
    private NetworkConnectionController connectionController;

    WifiConnectionValidator(NetworkConnectionController controller) {
        this.connectionController = controller;
    }

    public void networkConnected() {
//        connectionController.getConnectionType("wifi network connected");
    }

    public void networkDisconnected(int i) {
//        connectionController.getConnectionType("wifi network disconnected");
    }

    protected boolean isWiFiActive() {
        return ((RadioInfo.getActiveWAFs() & RadioInfo.WAF_WLAN) != 0)
                && (CoverageInfo.getCoverageStatus(RadioInfo.WAF_WLAN, false) != 0)
                && (WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED);

// Uncomment the following line to disable Wifi
//    return false;
    }
}
//#preprocess
package com.xtremelabs.xtremeutil.net;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import com.xtremelabs.xtremeutil.util.concurrency.UITask;
import net.rim.device.api.io.transport.TransportInfo;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.util.IntVector;

/* This class uses RIM API to determine connection type */
public class NetworkConnectionController implements RadioStatusListener, CoverageStatusListener {
    private static NetworkConnectionController connectionControllerInstance;

    public static final int NO_CONNECTION = 0;
    public static final int DIRECT_CONNECTION = 1, CARRIER_CONNECTION = 1;
    public static final int BIS_B_CONNECTION = 2;
    public static final int BES_MDS_CONNECTION = 4;
    public static final int WIFI_DIRECT_CONNECTION = 8;

    private volatile boolean lowSignalLevel;
    private volatile int coverage = CoverageInfo.COVERAGE_NONE;
    private WifiConnectionValidator wifiConnectionValidator;

    private NetworkConnectionController() {
        wifiConnectionValidator = new WifiConnectionValidator(this);
    }

    // singleton
    public static NetworkConnectionController getInstance() {
        if (connectionControllerInstance == null) {
            connectionControllerInstance = new NetworkConnectionController();
        }
        return connectionControllerInstance;
    }

    public boolean isNetworkConnectionValid() {
        // valid if wifi connection available
        if (getConnectionType() == WIFI_DIRECT_CONNECTION) {
            return true;
        }

        // invalid if low network signal level
        if (isNetworkCoverageNotSufficient()) {
            XLogger.warn(getClass(), "low signal level");
            return false;
        } else if (getConnectionType() == NO_CONNECTION) {
            XLogger.warn(getClass(), " NO_CONNECTION");
            return false;
        }

        //if no wifi, data service has to be available
        int networkService = RadioInfo.getNetworkService();
        if ((networkService & RadioInfo.NETWORK_SERVICE_DATA) == 0) {
            XLogger.warn(getClass(), "No data service available. Network services available: " + networkService);
            return false;
        }

        /*
        * Note: checking CoverageInfo and RadioInfo is sufficient to cover all following
        *                       cases. No additional check need to be performed, based on RIM tech support.
        */
        // GPRS or 3G
        if ((RadioInfo.getActiveWAFs() & RadioInfo.WAF_3GPP) != 0) {
            return true;
        }

        //CDMA
        if ((RadioInfo.getActiveWAFs() & RadioInfo.WAF_CDMA) != 0) {
            return true;
        }

        //IDEN
        if ((RadioInfo.getActiveWAFs() & RadioInfo.WAF_IDEN) != 0) {
            return true;
        }

        XLogger.warn(getClass(), "Tried all possibilities, no connection");
        return false;
    }

    public int getConnectionType() {
        return decideConnectionType();
    }

    public static boolean hasMDSOrBISBCoverage() {
        // BisB third
        return CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS)
                || CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B);
    }

    public boolean isBESConnection() {
        return getConnectionType() == BES_MDS_CONNECTION;
    }

    public static int[] availableConnectionTypes() {
    	IntVector ans = new IntVector();
        if (isWiFiConnected()) {
            ans.addElement(TransportInfo.TRANSPORT_TCP_WIFI);
        }

        if (isBisAvailable()) {
        	ans.addElement(TransportInfo.TRANSPORT_BIS_B);
        }

        if (isBesAvailable()) {
        	ans.addElement(TransportInfo.TRANSPORT_MDS);
        }

        if (isDirectTCPAvailable()) {
        	ans.addElement(TransportInfo.TRANSPORT_TCP_CELLULAR);
        }
        return ans.toArray();
    }
    
    public static boolean isWiFiConnected() {
        return ((RadioInfo.getActiveWAFs() & RadioInfo.WAF_WLAN) != 0)
                && (CoverageInfo.getCoverageStatus(RadioInfo.WAF_WLAN, false) != 0)
                && (WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED);
// Uncomment the following line to disable Wifi
//    return false;
    }

    public static boolean isBesAvailable() {
        return CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS);
    }

    public static boolean isBisAvailable() {
        return CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B);
    }

    public static boolean isApnSet() {
        //#ifdef VER_5.0.0
        // TODO: For now, just assume the APN is set
        // A possible solution would be to open connections to a few high-reliability servers (Google etc)
        // using a Direct TCP connection and see if the connection succeeds. If so, then APN must have been set.
        	return true;
        //#else
        if (RadioInfo.getNetworkType() == RadioInfo.NETWORK_GPRS) {
            return net.rim.device.api.io.transport.options.TcpCellularOptions.isDefaultAPNSet();
//        	return true;
        } else {
            return true;
        }
        //#endif
    }

    public static boolean isDirectTCPAvailable() {
        return isApnSet() && CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_DIRECT);
    }
    
    private int decideConnectionType() {
        try {
            //WiFi first
            if (isWiFiActive()) {
                return WIFI_DIRECT_CONNECTION;
            }

            int WAF = RadioInfo.getActiveWAFs() & (~RadioInfo.WAF_WLAN);// clear WLAN bit

            //MDS second
            if (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS, WAF, false) ||
                    (coverage & CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS) {
                return BES_MDS_CONNECTION;
            }

            //BisB third
            if (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B, WAF, false) ||
                    (coverage & CoverageInfo.COVERAGE_BIS_B) == CoverageInfo.COVERAGE_BIS_B) {
                return BIS_B_CONNECTION;
            }

            //direct/carrier fourth
            // TODO: see if both CoverageInfo.COVERAGE_CARRIER and CoverageInfo.DIRECT_CONNECTION are 1 if so use value since 4.7 and 4.2 each don't have one available
            if (CoverageInfo.isCoverageSufficient(CARRIER_CONNECTION, WAF, false) ||
                    (coverage & CARRIER_CONNECTION) == CARRIER_CONNECTION) {
                return CARRIER_CONNECTION;
            }

            if (!CoverageInfo.isOutOfCoverage(WAF, false)) {
                // Can't figure it out, but just ask if there's coverage (Telus Storm)
                // Try single argument isCoverageSufficient
                // MDS second (Wifi already was first earlier)
                if (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS)) {
                    return BES_MDS_CONNECTION;
                }

                // BisB third
                if (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B)) {
                    return BIS_B_CONNECTION;
                }

                // direct/carrier fourth
                if (CoverageInfo.isCoverageSufficient(CARRIER_CONNECTION)) {
                    return CARRIER_CONNECTION;
                }
                //              if all fail assume BIS_B_CONNECTION is available
                return BIS_B_CONNECTION;
            }

        }
        catch (Exception e) {
            new UITask() {
                public void execute() {
                    XLogger.error(getClass(), "Network settings detection failed, please make sure you have an active \"Mobile Network\" conncetion");
                    Dialog.alert("Network settings detection failed, please make sure you have an active \"Mobile Network\" conncetion");
                }
            }.invokeLater();
        }

        return NO_CONNECTION;
    }


    private boolean isNetworkCoverageNotSufficient() {
        try {
            if (lowSignalLevel || (RadioInfo.getNetworkType() == RadioInfo.NETWORK_GPRS
                    && GPRSInfo.getGPRSState() == GPRSInfo.GPRS_STATE_IDLE)) {
                sleepOneSecond();
            }
            return (lowSignalLevel || (RadioInfo.getNetworkType() == RadioInfo.NETWORK_GPRS
                    && GPRSInfo.getGPRSState() == GPRSInfo.GPRS_STATE_IDLE));
        } catch (Exception e) {
            XLogger.warn(getClass(), "GPRS Info could not be read potentially CDMA device " + e);
            if (lowSignalLevel) {
                sleepOneSecond();
            }
            return lowSignalLevel;
        }

    }

    private void sleepOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException iE) {
            //ignore
        }
    }

    public boolean isWiFiActive() {
        return wifiConnectionValidator.isWiFiActive();
    }

    static void logCoverageState() {
        String msg = new StringBuffer().append("Coverage (Overall, 3GPP, CDMA, iDEN, WLAN): \n(").
                append(CoverageInfo.getCoverageStatus()).
                append(", ").append(CoverageInfo.getCoverageStatus(RadioInfo.WAF_3GPP, false)).
                append(", ").append(CoverageInfo.getCoverageStatus(RadioInfo.WAF_CDMA, false)).
                append(", ").append(CoverageInfo.getCoverageStatus(RadioInfo.WAF_IDEN, false)).
                append(", ").append(CoverageInfo.getCoverageStatus(RadioInfo.WAF_WLAN, false)).
                append(")").toString();
        //      Logger.getInstance().log(msg);
        //      Logger.getInstance().log(new StringBuffer().append("Active WAFs: ").append(RadioInfo.getActiveWAFs()).toString());
    }

    public void radioTurnedOff() {
//    	getConnectionType("Radio state off.");
    }


    public void signalLevel(int level) {
        if (level <= -250) {
            lowSignalLevel = true;
            //      Logger.getInstance().log(new StringBuffer("Signal level is too low: ").append(level).append("dBm"));
        } else {
            lowSignalLevel = false;
        }
    }

    public void pdpStateChange(int apn, int state, int cause) {
        //    Logger.getInstance().log(new StringBuffer().append("PDP state changed! apn is ").append(apn).append(" the state is ").append(state).append(" the cause is ").append(cause).toString());
    }

    public void networkStateChange(int state) {
    	
        //getConnectionType(new StringBuffer("Network state changed. State is ").append(state).toString());
        //    Logger.getInstance().log(new StringBuffer().append("Network state changed ").append(state).toString());
        //    logGPRSState();
    }


    public void networkStarted(int networkId, int service) {
        //    Logger.getInstance().log(new StringBuffer().append("Network started! The networkId is ").append(networkId).append(" the service is ").append(service).toString());
    }

    public void networkServiceChange(int networkId, int service) {
        //    Logger.getInstance().log(new StringBuffer().append("Network service changed! The networkId is ").append(networkId).append(" the service is ").append(service).toString());
//        getConnectionType(new StringBuffer("Network service changed, networkId: ").append(networkId).append(" service: ").append(service).toString());
    }

    public void networkScanComplete(boolean b) {
    }

    public void baseStationChange() {
    }

    public void mobilityManagementEvent(int i, int i1) {
    }

    public void coverageStatusChanged(int newCoverage) {
        coverage = newCoverage;
//        getConnectionType(new StringBuffer("Coverage status changed. coverage is ").append(coverage).toString());
    }

    public void register() {
        Application.getApplication().addRadioListener(this);
        CoverageInfo.addListener(this);
        WLANInfo.addListener(wifiConnectionValidator);
    }

    public void unregister() {
        Application.getApplication().removeRadioListener(this);
        CoverageInfo.removeListener(this);
        WLANInfo.removeListener(wifiConnectionValidator);
    }
}
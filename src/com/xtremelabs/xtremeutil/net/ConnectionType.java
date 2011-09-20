package com.xtremelabs.xtremeutil.net;

import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;

public abstract class ConnectionType {
    private static String BIS_PARAMS = ";deviceside=false;ConnectionType=mds-public";
    private static String MDS_PARAMS = ";deviceside=false";
    private static String DIRECT_PARAMS = ";deviceside=true";
    private static String WIFI_PARAMS = ";interface=wifi";

    private String _connectionParameter = null;
    private String _connectionUid;

    public ConnectionType(String connectionParameter) {
        _connectionParameter = connectionParameter;
    }

    public void setConnectionUID(String uid) {
        _connectionUid = uid;
    }

    public String formatUrl(String url) {
        StringBuffer buf = new StringBuffer(url);
        buf.append(_connectionParameter);

        if (_connectionUid != null && _connectionUid.length() != 0) {
            buf.append(";ConnectionUID=");
            buf.append(_connectionUid);
        }

        return buf.toString();
    }

    public String getConnectionUID() {
        return _connectionUid;
    }

    public boolean hasCoverage() {
        return false;
    }

    public boolean hasServiceBook() {
        return getConnectionUID() != null;
    }

    public String getConnectionParameter() {
        return _connectionParameter;
    }

    public boolean canConnect() {
        return hasCoverage();
    }

    public static final ConnectionType MDS = new ConnectionType(MDS_PARAMS) {
        public boolean hasCoverage() {
            return ConnectionStatus.hasMDSCoverage();
        }
    };

    public static final ConnectionType BIS = new ConnectionType(BIS_PARAMS) {
        public boolean hasCoverage() {
            return ConnectionStatus.hasBISCoverage();
        }
    };

    public static final ConnectionType DIRECT = new ConnectionType(DIRECT_PARAMS) {
        public boolean hasCoverage() {
            return ConnectionStatus.hasDirectCoverage();
        }
    };

    public static final ConnectionType WAP2 = new ConnectionType(DIRECT_PARAMS) {
        public boolean hasCoverage() {
            return ConnectionStatus.hasDirectCoverage();
        }

        public boolean canConnect() {
            return hasCoverage() && hasServiceBook();
        }
    };

    public static final ConnectionType WIFI = new ConnectionType(WIFI_PARAMS) {
        public boolean hasCoverage() {
            return ConnectionStatus.isWifiConnected();
        }
    };

    static {
        // Parse the service books to ensure we can use certain transport
        ServiceBook sb = ServiceBook.getSB();
        ServiceRecord[] records = sb.getRecords();

        for (int i = 0; i < records.length; i++) {
            ServiceRecord myRecord = records[i];
            String cid, uid;

            if (myRecord.isValid() && !myRecord.isDisabled()) {
                cid = myRecord.getCid().toLowerCase();
                uid = myRecord.getUid().toLowerCase();

                // Wap2.0
                if (cid.indexOf("wptcp") != -1 && uid.indexOf("wifi") == -1 && uid.indexOf("mms") == -1) {
                    WAP2.setConnectionUID(myRecord.getUid());
                }
            }
        }
    }
}

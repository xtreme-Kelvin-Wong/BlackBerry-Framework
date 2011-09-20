package com.changeme.app.barebone_changename.net;

import com.xtremelabs.xtremeutil.net.api.RequestQueue;

public class BareBoneRequestQueue extends RequestQueue {

    private static BareBoneRequestQueue _instance;

    public static BareBoneRequestQueue getInstance() {
        if (_instance != null) {
            return _instance;
        }
        return new BareBoneRequestQueue();
    }
}

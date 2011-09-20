package com.xtremelabs.xtremeutil.application;

import com.xtremelabs.xtremeutil.util.concurrency.ThreadAdapter;
import net.rim.device.api.ui.UiApplication;

/*
 * Proxy.java 
 */

/**
 * Placeholder for a real proxy/daemon app to be used by the app.
 */
public abstract class Proxy extends UiApplication {

    public abstract void submitRunnable(Runnable runnable);

    public abstract void startThread(ThreadAdapter thread);

    public abstract void invokeRunnable(Runnable runnable);

    /**
     * @deprecated
     * <p/><b/><br/>
     * DO NOT USE
     * @return NOTHING!
     * @throws RuntimeException ALWAYS!
     */
    public static Proxy getInstance() {
        throw new RuntimeException("This method is not accessible to non-RIM-internal apps");
    }

}


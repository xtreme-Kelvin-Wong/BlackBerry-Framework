package com.xtremelabs.xtremeutil.util.concurrency;

import com.xtremelabs.xtremeutil.application.Proxy;
import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.device.api.system.Application;

/**
 * ThreadUtils.java
 * 
 * Utility class for creating and starting threads
 */

public class ThreadUtils {

    /**
     * @param thread
     * @param startNow if true, this method will wait until the thread starts
     */
    public static void startDaemonThread(final ThreadAdapter thread, Proxy daemon, boolean startNow) {
        if (daemon == Application.getApplication()) {
            thread.start();
        } else {
            if (startNow) {
                daemon.invokeRunnable(new Runnable() {
                    public void run() {
                        XLogger.info(getClass(), "starting thread in daemon process. Thread:\n" + thread);
                        thread.start();
                    }
                });
            } else {
                daemon.startThread(thread);
            }
        }
    }

    public static void startApplicationThread(final Application app, final ThreadAdapter thread, boolean startNow) {
        if (app == null || app.isAlive()) return;

        if (app == Application.getApplication()) {
            thread.start();
        } else {
            Runnable runnable = new Runnable() {
                public void run() {
                    thread.start();
                }
            };

            if (startNow) {
                app.invokeAndWait(runnable);
            } else {
                app.invokeLater(runnable);
            }
        }
    }
}

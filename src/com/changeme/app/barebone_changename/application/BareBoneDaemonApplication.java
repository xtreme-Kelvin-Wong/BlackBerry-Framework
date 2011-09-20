package com.changeme.app.barebone_changename.application;

import com.xtremelabs.xtremeutil.application.AbstractXtremeDaemon;
import com.xtremelabs.xtremeutil.device.api.runtimestore.RuntimeConstructorDelegate;
import com.xtremelabs.xtremeutil.device.api.runtimestore.RuntimeSingletonFactory;
import com.xtremelabs.xtremeutil.util.logger.XLogger;

import java.util.Timer;

public class BareBoneDaemonApplication extends AbstractXtremeDaemon {
    private static BareBoneDaemonApplication instance;
    private Timer timer;


    BareBoneDaemonApplication() {
    }

    public static BareBoneDaemonApplication getAppInstance() {
        if (instance == null) {
            instance = (BareBoneDaemonApplication) RuntimeSingletonFactory.getSingletonInstance(new RuntimeConstructorDelegate() {
                public Object create() {
                    return new BareBoneDaemonApplication();
                }
            });
        }
        return instance;
    }

    public void runOnStartup() {
        // Log Daemon Process ID
        new Thread(new Runnable() {
            public void run() {
                XLogger.info(BareBoneDaemonApplication.this.getClass(), "Proxy (daemon app) procID: " + getProcessId());
            }
        }).start();
    }
}

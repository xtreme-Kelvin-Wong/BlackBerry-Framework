package com.xtremelabs.xtremeutil.application;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import com.xtremelabs.xtremeutil.util.concurrency.ThreadAdapter;
import net.rim.device.api.system.Alert;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Timer;

public abstract class AbstractXtremeDaemon extends Proxy {
    static final String GUI_APP_TYPE_NOT_FOUND = "GuiApp type not found!";
    static final String GUI_APP_NOT_FOUND = "GuiApp not found!";
    static final String GUI_APP_TYPE_NOT_COMPATIBLE_WITH_UI_APPLICATION_CLASS = "GuiApp type not compatible with AbstractXtremeApplicationImpl.class!";
    static final String GUI_APP_IS_NO_LONGER_ALIVE = "GuiApp is no longer alive!";
    static final String APP_INSTANCE_IS_BEING_REPLACED_UNEXPECTEDLY = "App instance is being replaced unexpectedly!";
    static final String COULD_NOT_SHUTDOWN_OLD_INSTANCE_MEMOROY_LEAK_MAY_OCCUR = "could not shutdown old instance. Memoroy Leak may occur";

    private volatile Hashtable _applicationInstances;

    boolean devmode;
    private Timer timer;

    public AbstractXtremeDaemon() {
        _applicationInstances = new Hashtable();
        timer = new Timer();
    }

    abstract protected void runOnStartup();

    protected boolean acceptsForeground() {
        return false;
    }

    public Timer getTimer() {
        return timer;
    }

    public void submitRunnable(final Runnable runnable) {
        invokeLater(runnable);
    }

    public void startThread(final ThreadAdapter thread) {
        invokeLater(new Runnable() {
            public void run() {
                thread.start();
            }
        });
    }

    public void invokeRunnable(final Runnable runnable) {
        invokeAndWait(runnable);
    }

    public AbstractXtremeApplicationImpl getGuiApplication(String type) {
        WeakReference reference = (WeakReference) _applicationInstances.get(type);
        if (reference == null) {
            XLogger.warn(getClass(), GUI_APP_TYPE_NOT_FOUND);
            return null;
        }
        Object referee = reference.get();
        if (referee == null) {
            XLogger.warn(getClass(), GUI_APP_NOT_FOUND);
            return null;
        }
        if (!(referee instanceof AbstractXtremeApplicationImpl)) {
            XLogger.error(getClass(), GUI_APP_TYPE_NOT_COMPATIBLE_WITH_UI_APPLICATION_CLASS + referee.getClass());
            return null;
        }
        AbstractXtremeApplicationImpl app = (AbstractXtremeApplicationImpl) referee;
        if (!app.isAlive()) {
            XLogger.warn(getClass(), GUI_APP_IS_NO_LONGER_ALIVE);
            return null;
        }
        return app;
    }

    public void setGuiApplication(String type, AbstractXtremeApplicationImpl guiApp) {
        WeakReference weakReference = new WeakReference(guiApp);
        WeakReference reference = (WeakReference) _applicationInstances.get(type);
        if (reference != null && reference.get() != guiApp) {
            XLogger.warn(getClass(), APP_INSTANCE_IS_BEING_REPLACED_UNEXPECTEDLY);
            if (devmode) {
                Alert.startVibrate(100);
            }
            Object referee = reference.get();

            if (referee instanceof AbstractXtremeApplicationImpl) {
                AbstractXtremeApplicationImpl app = (AbstractXtremeApplicationImpl) referee;
                if (app.isAlive()) {
                    try {
                        app.exit(1);
                    } catch (Throwable e) {
                        XLogger.error(getClass(), COULD_NOT_SHUTDOWN_OLD_INSTANCE_MEMOROY_LEAK_MAY_OCCUR);
                    }
                }
            }
        }

        _applicationInstances.put(type, weakReference);
    }

    public void clearGuiApplicationReference(String type) {
        _applicationInstances.remove(type);
    }
}
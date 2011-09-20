package com.xtremelabs.xtremeutil.util.concurrency;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.ui.UiApplication;

import java.lang.ref.WeakReference;


/**
 * example:
 * UITask runnable = new UITask() {
 * public void execute() {
 * // update UI
 * manager.add(new ButtonField());
 * }
 * };
 * <p/>
 * runnable.invokeLater();
 */
public abstract class UITask implements Runnable {
    static String EXCEPTION_OCCURRED_IN_APPLICATION = "Exception Occurred in Application: ";
    static String UI_APPLICATION_IS_NULL_ABORTING_UPDATE = "UiApplication is null!, aborting update";
    static String APPLICATION_IS_TERMINATED_ABORTING_UPDATE = "Application is terminated!, aborting update";
    static String APPLICATION_DOESN_T_HAVE_EVENT_THREAD_ABORTING_UPDATE = "Application doesn't have eventThread!, aborting update";
    static String EXCEPTION_IN_INVOKE_LATER = "Exception in invokeLater";
    static String EXCEPTION_IN_CANCEL_INVOKE_LATER = "Exception in cancelInvokeLater";
    static String EXCEPTION_IN_INVOKE_AND_WAIT = "Exception in invokeAndWait";
    static String EXCEPTION_IN_INVOKE_NOW = "Exception in invokeNow";
    static String AN_ERROR_OCCURRED_IN = "An error occurred in ";
    static String EXCEPTION_DETAIL = "\nException detail: ";

    private static WeakReference globalListenerReference; // contains TaskEventListener
    private WeakReference taskEventListenerReference; // contains TaskEventListener

    public UITask() {
    }

    public UITask(TaskEventListener listener) {
        taskEventListenerReference = new WeakReference(listener);
    }

    public abstract void execute();


    public static void setGlobalEventListener(TaskEventListener listener) {
        globalListenerReference = new WeakReference(listener);
    }

    public static void removeGlobalEventListener() {
        globalListenerReference.clear();
        globalListenerReference = null;
    }

    private UiApplication getUIApplication() {
        return UiApplication.getUiApplication();
    }

    private void checkApplication(UiApplication app) throws UIRunnableException {
        if (app == null) {
            throw new UIRunnableException(UI_APPLICATION_IS_NULL_ABORTING_UPDATE);
        } else if (!app.isAlive()) {
            throw new UIRunnableException(APPLICATION_IS_TERMINATED_ABORTING_UPDATE);
        } else if (!app.hasEventThread()) {
            throw new UIRunnableException(APPLICATION_DOESN_T_HAVE_EVENT_THREAD_ABORTING_UPDATE);
        } else {
            // todo: log something?
        }
    }

    public void invokeLater() {
        invokeLater(getUIApplication());
    }

    public void invokeLater(UiApplication app) {
        try {
            checkApplication(app);
            app.invokeLater(this);
        } catch (Exception e) {
            handleError(app, EXCEPTION_IN_INVOKE_LATER, e, true);
        }
    }

    public int invokeLater(long time, boolean repeat) {
        return invokeLater(getUIApplication(), time, repeat);
    }

    public int invokeLater(UiApplication app, long time, boolean repeat) {
        int id = -1;
        try {
            checkApplication(app);
            id = app.invokeLater(this, time, repeat);
        } catch (Exception e) {
            handleError(app, EXCEPTION_IN_INVOKE_LATER, e, false);
        }
        return id;
    }

    public int replaceAndInvokeLater(final int id) {
        return replaceAndInvokeLater(getUIApplication(), id);
    }

    public int replaceAndInvokeLater(UiApplication app, final int id) {
        cancelInvokeLater(id);
        return invokeLater(app, 1, false); // 0 would throw illegalArgumentException
    }

    public void cancelInvokeLater(int id) {
        cancelInvokeLater(getUIApplication(), id);
    }

    public void cancelInvokeLater(UiApplication app, int id) {
        try {
            checkApplication(app);
            app.cancelInvokeLater(id);
        } catch (Exception e) {
            handleError(app, EXCEPTION_IN_CANCEL_INVOKE_LATER, e, false);
        }
    }

    public void invokeAndWait() {
        invokeAndWait(getUIApplication());
    }

    public void invokeAndWait(UiApplication app) {
        try {
            checkApplication(app);
            app.invokeAndWait(this);
        } catch (Exception e) {
            handleError(app, EXCEPTION_IN_INVOKE_AND_WAIT, e, true);
        }
    }

    public void invokeNow() {
        invokeNow(getUIApplication());
    }

    public void invokeNow(UiApplication app) {
        try {
            checkApplication(app);
            synchronized (app.getAppEventLock()) {
                run();
            }
        } catch (Exception e) {
            handleError(app, EXCEPTION_IN_INVOKE_NOW, e, true);
            return;
        }
        synchronized (app.getAppEventLock()) {
            run();
        }

    }

    private void handleError(Application app, String msg, Exception e, boolean logAsError) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(EXCEPTION_OCCURRED_IN_APPLICATION).append(app).append('\n');
        buffer.append(e);
        buffer.append("\n");
        buffer.append(msg);

        if (logAsError) {
            XLogger.error(getClass(), msg);
        } else {
            XLogger.debug(getClass(), msg);
        }
    }

    public final void run() {
        try {
            execute();
        } catch (final Throwable t) {
            t.printStackTrace();
            TaskEventListener listener = null;

            //check specific listener first
            if (taskEventListenerReference != null) {
                Object specificListener = taskEventListenerReference.get();
                if (specificListener != null) {
                    listener = (TaskEventListener) specificListener;
                }
            }

            //check global listener next
            if (listener == null && globalListenerReference != null) {
                Object globalListener = globalListenerReference.get();
                if (globalListener != null) {
                    listener = (TaskEventListener) globalListener;
                }
            }

            if (listener != null) {
                listener.onUIException(t);
            } else {
                String message = new StringBuffer(AN_ERROR_OCCURRED_IN)
                        .append(ApplicationDescriptor.currentApplicationDescriptor().getName())
                        .append(EXCEPTION_DETAIL)
                        .append(t)
                        .toString();
                XLogger.error(getClass(), message);
            }
        }
    }


    private static class UIRunnableException extends Exception {
        public UIRunnableException(String message) {
            super(message);
        }
    }
}

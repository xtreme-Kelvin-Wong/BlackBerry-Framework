//#preprocess

package com.xtremelabs.xtremeutil.util.logger;

import com.xtremelabs.xtremeutil.device.api.runtimestore.RuntimeConstructorDelegate;
import com.xtremelabs.xtremeutil.device.api.runtimestore.RuntimeSingletonFactory;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.util.StringUtilities;

import java.util.Vector;

public class XLogger {
    public static final long GUID = StringUtilities.stringHashToLong(ApplicationDescriptor.currentApplicationDescriptor().getModuleName() + XLogger.class);
    private String xLoggerToken = ApplicationDescriptor.currentApplicationDescriptor().getName();

    public static final int DATALOSS = 1; //F%$%)#!!!!
    public static final int FATAL_ERROR = 2; //SUPER BAD
    public static final int ERROR = 4; //Really bad
    public static final int WARNING = 8; //Bad
    public static final int INFO = 16; //Okay...
    public static final int DEBUG = 32; //Okay...

    public static int MAX_SIZE = 400;

    protected static XLogger instance;
    private static final String EXCEPTION_PREFIX = "Exception: ";

    private Vector log;

    private boolean systemLoggingOn = true;

    public static XLogger getInstance() {
        if (instance == null) {
            instance = (XLogger) RuntimeSingletonFactory.getSingletonInstance(new RuntimeConstructorDelegate() {
                public Object create() {
                    return new XLogger();
                }
            });
        }
        return instance;
    }

    protected XLogger() {
        log = new Vector(MAX_SIZE);
        String name = EventLogger.getRegisteredAppName(GUID);
        if (name == null || !name.equalsIgnoreCase(xLoggerToken)) {
            EventLogger.register(GUID, xLoggerToken, EventLogger.VIEWER_STRING);
        }
    }


    public void setSystemLoggingOn(boolean systemLoggingOn) {
        this.systemLoggingOn = systemLoggingOn;
    }

    public void setXLoggerToken(String token) {
        xLoggerToken = token;
    }

    public static void resetLog() {
        XLogger logger = XLogger.getInstance();
        logger.log.removeAllElements();
        logger.log.ensureCapacity(MAX_SIZE);
    }

    public static void fatal(Class cls, Exception e) {
        XLogger.fatal(cls, EXCEPTION_PREFIX + e.toString());
    }

    public static void fatal(Class cls, String msg) {
        //#ifdef ANALYTICS
        //    com.flurry.blackberry.FlurryAgent.onError("FATAL", msg, cls.getName());
        //#endif
        XLogger.getInstance().log(cls, FATAL_ERROR, msg);
    }

    public static void error(Class cls, Exception e) {
        XLogger.error(cls, EXCEPTION_PREFIX + e.toString());
    }

    public static void error(Class cls, String msg) {
        //#ifdef ANALYTICS
        //    com.flurry.blackberry.FlurryAgent.onError("ERROR", msg, cls.getName());
        //#endif
        XLogger.getInstance().log(cls, ERROR, msg);
    }

    public static void warn(Class cls, Exception e) {
        XLogger.warn(cls, EXCEPTION_PREFIX + e.toString());
    }

    public static void warn(Class cls, String msg) {
        XLogger.getInstance().log(cls, WARNING, msg);
    }

    public static void info(Class cls, Exception e) {
        XLogger.info(cls, EXCEPTION_PREFIX + e.toString());
    }

    public static void info(Class cls, String msg) {
        XLogger.getInstance().log(cls, INFO, msg);
    }

    public static void debug(Class cls, Exception e) {
        XLogger.debug(cls, EXCEPTION_PREFIX + e.toString());
    }

    public static void debug(Class cls, String msg) {
        XLogger.getInstance().log(cls, DEBUG, msg);
    }

    public Vector dumpLog() {
        return log;
    }

    public Vector dumpFilteredLog(int flags) {

        Vector ret = new Vector();

        for (int i = 0; i < log.size(); i++) {
            LogEntry curr = (LogEntry) log.elementAt(i);

            if ((flags & curr.level) != 0) {
                ret.addElement(curr);
            }
        }

        return ret;
    }

    public void log(Class cls, int level, String msg) {

        if (log.size() >= MAX_SIZE) {
            log.removeElementAt(0);
        }

        String fullClassPath = cls.getName();
        String className = fullClassPath.substring(fullClassPath.lastIndexOf('.') + 1, fullClassPath.length());
        LogEntry entry = new LogEntry(System.currentTimeMillis(), className, level, msg);
        log.addElement(entry);
        systemLog(level, entry);
    }

    private void systemLog(int level, LogEntry entry) {
        if (!systemLoggingOn) {
            return;
        }

        //#ifdef DEVELOPMENT
        if (level == ERROR || level == FATAL_ERROR) {
            System.err.println(entry);
        } else {
            System.out.println(entry);
        }
        //#endif
        switch (level) {
            case DEBUG:
                //#ifdef DEVELOPMENT
                EventLogger.logEvent(GUID, entry.toString().getBytes(), EventLogger.DEBUG_INFO);
                //#endif
                break;
            case INFO:
                EventLogger.logEvent(GUID, entry.toString().getBytes(), EventLogger.INFORMATION);
                break;
            case WARNING:
                EventLogger.logEvent(GUID, entry.toString().getBytes(), EventLogger.WARNING);
                break;
            case ERROR:
                EventLogger.logEvent(GUID, entry.toString().getBytes(), EventLogger.ERROR);
                break;
            case FATAL_ERROR:
                EventLogger.logEvent(GUID, entry.toString().getBytes(), EventLogger.SEVERE_ERROR);
                break;
            default:
                EventLogger.logEvent(GUID, entry.toString().getBytes(), EventLogger.ALWAYS_LOG);
                break;
        }
    }

    private String levelToString(int level) {

        switch (level) {
            case FATAL_ERROR:
                return "FATAL";
            case ERROR:
                return "ERROR";
            case WARNING:
                return "WARN";
            case INFO:
                return "INFO";
            case DEBUG:
                return "DEBUG";
        }

        return "UNKNOWN";
    }

    private class LogEntry {
        public String msg;
        public int level;
        public String cls;
        public long date;

        public LogEntry(long date, String cls, int level, String msg) {
            this.date = date;
            this.cls = cls;
            this.level = level;
            this.msg = msg;
        }

        public String toString() {
            StringBuffer logMsg = new StringBuffer();
            logMsg = logMsg.append(levelToString(level)).append(" ").append(date).append(" ").append(msg).append("\t").append(cls);
            return logMsg.toString();
        }
    }

    public String getLogBlob(int flags) {

        Vector log = dumpFilteredLog(flags);
        StringBuffer ret = new StringBuffer();

        for (int i = 0; i < log.size(); i++) {
            ret = ret.append(log.elementAt(i).toString()).append("\n");
        }

        return ret.toString();
    }
}

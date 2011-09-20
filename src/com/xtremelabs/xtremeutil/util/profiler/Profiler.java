package com.xtremelabs.xtremeutil.util.profiler;


import com.xtremelabs.xtremeutil.util.logger.XLogger;

import java.util.Hashtable;

public class Profiler {

    private Hashtable timeHash;
    private static Profiler instance = new Profiler();


    protected Profiler() {
        timeHash = new Hashtable();
    }

    public synchronized static Profiler getInstance() {
        return instance;
    }

    public synchronized void start(String key) throws Exception {
        if (timeHash.containsKey(key)) {
            ProfilerContent startingContent = (ProfilerContent) timeHash.get(key);
            if (startingContent.isStarted()) {
                throw new Exception("Method with " + key + " already started");
            } else {
                startingContent.start();
            }
        } else {
            ProfilerContent startingContent = new ProfilerContent();
            startingContent.start();
            timeHash.put(key, startingContent);
        }
    }

    public synchronized void stop(String key) throws Exception {
        if (timeHash.containsKey(key)) {
            ProfilerContent startingContent = (ProfilerContent) timeHash.get(key);
            if (!startingContent.isStarted()) {
                throw new Exception("Method with " + key + " is not started");
            } else {
                startingContent.stop();
                if (this == instance) {
                    XLogger.info(getClass(), new StringBuffer("task: ").append(key).append(", took ").append(getTimeElapsedInSeconds(key)).toString());
                }
            }
        } else {
            throw new Exception("Method with " + key + " is not started");
        }
    }

    public synchronized long getTimeElapsed(String key) {
        if (timeHash.containsKey(key)) {
            return ((ProfilerContent) timeHash.get(key)).getTimeElapsed();
        } else {
            return 0;
        }

    }

    public String getTimeElapsedInSeconds(String key) {
        return getTimeElapsedInSeconds(getTimeElapsed(key));
    }

    protected static String getTimeElapsedInSeconds(long time) {
        int seconds = (int) time / 1000;               //2001 ->   2  and 1
        int milis = (int) time % 1000 + 1000;          // then 1 -> 1001
        String milisString = "" + milis;              // then 1001 -> "1001"
        milisString = milisString.substring(1);       // then "1001" -> "001"
        return (seconds + "." + milisString + " second(s)");  //final result 2001 -> "2.001 second(s)"

    }

    public synchronized void clean() {
        timeHash.clear();
    }

    private class ProfilerContent {
        private long timeStart;
        private long timeElapsed;
        private boolean isStarted;


        private ProfilerContent() {
            timeStart = 0;
            timeElapsed = 0;
            isStarted = false;
        }

        private void start() {
            timeStart = System.currentTimeMillis();
            isStarted = true;
        }

        private void stop() {
            long temp = System.currentTimeMillis();
            timeElapsed += temp - timeStart;
            isStarted = false;
        }

        private long getTimeElapsed() {
            return timeElapsed;
        }

        private boolean isStarted() {
            return isStarted;
        }
    }
}

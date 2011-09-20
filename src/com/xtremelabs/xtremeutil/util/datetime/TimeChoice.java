package com.xtremelabs.xtremeutil.util.datetime;

public class TimeChoice {
    public static final int ONESECOND = 1000;
    public static final int ONEMINUTE = 60000;
    public static final int ONEHOUR = 3600000;
    public static final int ONEDAY = 86400000;
    public static final int ONEWEEK = 604800000;
    public static final long ONEMONTH = 2419200000L;
    public static final long ONEYEAR = 31536000000L;

    protected long timeInMiliSeconds;
    private static TimeChoice mutaChoice = new TimeChoice(0);

    public TimeChoice(long time) {
        this.timeInMiliSeconds = time;
    }

    public static String formatTime(long time) {
        mutaChoice.timeInMiliSeconds = time;
        return mutaChoice.toString().trim();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(" ");
        if (timeInMiliSeconds < ONESECOND) {
            buffer.append(timeInMiliSeconds);
            buffer.append(" Millisecond").append((timeInMiliSeconds) != 1 ? "s " : " ");
        } else if (timeInMiliSeconds < ONEMINUTE) {
            buffer.append(timeInMiliSeconds / ONESECOND);
            buffer.append(" Second").append((timeInMiliSeconds / ONESECOND) != 1 ? "s " : " ");
        } else if (timeInMiliSeconds < ONEHOUR) {
            buffer.append(timeInMiliSeconds / ONEMINUTE);
            buffer.append(" Minute").append((timeInMiliSeconds / ONEMINUTE) != 1 ? "s " : " ");
        } else if (timeInMiliSeconds < ONEDAY) {
            buffer.append(timeInMiliSeconds / ONEHOUR);
            buffer.append(" Hour").append((timeInMiliSeconds / ONEHOUR) != 1 ? "s " : " ");
        } else {
            buffer.append(timeInMiliSeconds / ONEDAY);
            buffer.append(" Day").append((timeInMiliSeconds / ONEDAY) != 1 ? "s " : " ");
        }
        return buffer.toString();
    }

    public long longValue() {
        return timeInMiliSeconds;
    }

    public boolean equals(Object object) {
        return (object instanceof TimeChoice) && ((TimeChoice) object).longValue() == longValue();
    }
}

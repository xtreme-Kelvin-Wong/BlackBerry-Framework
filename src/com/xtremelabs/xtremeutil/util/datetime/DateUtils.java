package com.xtremelabs.xtremeutil.util.datetime;


import net.rim.device.api.i18n.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    public static Date createDate(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date createDate(int year, int month, int day) {
        return createDate(year, month, day, 0, 0);
    }

    private static String format(Date time, String formatString) {
        StringBuffer buffer = new StringBuffer();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(time);
        new SimpleDateFormat(formatString).format(calendar, buffer, null);
        return buffer.toString();
    }

    public static String formatTimeStamp(long timestamp, boolean includeMilis) {
        return includeMilis ?
                formatTimeStamp(timestamp, "d MMM yyyy HH:mm:ss + SSS") :
                formatTimeStamp(timestamp, "d MMM yyyy HH:mm:ss");
    }

    public static String formatTimeStamp(long timestamp) {
        return formatTimeStamp(timestamp, "d MMM yyyy HH:mm:ss");
    }

    public static String formatTimeStamp(long timestamp, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(timestamp));
    }

    public static boolean isSameDay(long time1, long time2) {
        String day1 = formatTimeStamp(time1, "d MMM yyyy");
        String day2 = formatTimeStamp(time2, "d MMM yyyy");
        return day1.equals(day2);
    }

    public static String formatRangeForDisplay(Date startTime, int durationMinutes) {
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);

        long durationMillis = durationMinutes * 60 * 1000;
        Date endTime = new Date(startTime.getTime() + durationMillis);
        Calendar endTimeCalendar = Calendar.getInstance();
        endTimeCalendar.setTime(endTime);

        StringBuffer buffer = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat("h:mma");
        format.format(startTimeCalendar, buffer, null);
        buffer.append(" - ");
        format.format(endTimeCalendar, buffer, null);

        return buffer.toString().toLowerCase();
    }


    public static String formatTime(Date time) {
        return format(time, "h:mma").toLowerCase();
    }

    public static String formatTimeWithoutAmPm(Date time) {
        return format(time, "h:mm");
    }

    public static String formatNoPunctuationUtc(Date time) {
        StringBuffer buffer = new StringBuffer();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(time);
        new SimpleDateFormat("yyyyMMddHHmm").format(calendar, buffer, null);
        return buffer.toString();
    }

    public static Calendar midnight(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Date midnight(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal = midnight(cal);
        return cal.getTime();
    }

    public static long midnight(long ms) {
        return midnight(new Date(ms)).getTime();
    }
}

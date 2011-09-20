package com.xtremelabs.xtremeutil.util.datetime;

import rimunit.TestCase;

import java.util.Calendar;
import java.util.Date;


public class DateUtilsTest {

    public static class TestCreateDate extends TestCase {
        public void run() throws Exception {
            Date date = DateUtils.createDate(2005, 7, 27, 1, 39);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            assertEquals(2005, calendar.get(Calendar.YEAR));
            assertEquals(Calendar.JULY, calendar.get(Calendar.MONTH));
            assertEquals(27, calendar.get(Calendar.DAY_OF_MONTH));
            assertEquals(1, calendar.get(Calendar.HOUR_OF_DAY));
            assertEquals(39, calendar.get(Calendar.MINUTE));
            assertEquals(0, calendar.get(Calendar.SECOND));
            assertEquals(0, calendar.get(Calendar.MILLISECOND));
        }
    }


    public static class TestFormatRange extends TestCase {
        public void run() throws Exception {
            assertEquals("11:30am - 1:30pm",
                    DateUtils.formatRangeForDisplay(DateUtils.createDate(2004, 2, 29, 11, 30), 120));
            assertEquals("10:05pm - 12:35am",
                    DateUtils.formatRangeForDisplay(DateUtils.createDate(2003, 2, 28, 22, 5), 150));
        }
    }

    public static class TestFormatNoPunctuationUtc extends TestCase {
        public void run() throws Exception {
            assertEquals("200811011347",
                    DateUtils.formatNoPunctuationUtc(new Date(1225547220000L)));
        }
    }

    public static class TestFormatTime extends TestCase {
        public void run() throws Exception {
            assertEquals("11:59pm",
                    DateUtils.formatTime(DateUtils.createDate(2004, 12, 31, 23, 59)));
            assertEquals("11:59am",
                    DateUtils.formatTime(DateUtils.createDate(2004, 12, 31, 11, 59)));
            assertEquals("11:59",
                    DateUtils.formatTimeWithoutAmPm(DateUtils.createDate(2004, 12, 31, 23, 59)));
        }
    }

    public static class TestDifferentDay extends TestCase {
        public void run() throws Exception {
// TODO: fix these tests logically       
//      assertTrue("Now vs Now - 1 Day", DateUtils.isSameDay(DateUtils.NOW, DateUtils.NOW - 1));
//      assertTrue("Now vs Now - 1 Year", DateUtils.isSameDay(DateUtils.NOW, DateUtils.NOW - (long) DateTimeUtilities.ONEDAY * 365));
//      assertFalse("1970 vs 1970 + 1 hour", DateUtils.isSameDay(1, DateTimeUtilities.ONEHOUR));
        }
    }

}
package com.xtremelabs.xtremeutil.device.api.pim;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import com.xtremelabs.xtremeutil.util.lang.AggregateEnumeration;
import net.rim.blackberry.api.pdap.BlackBerryPIM;

import javax.microedition.pim.Event;
import javax.microedition.pim.EventList;
import javax.microedition.pim.PIMException;
import java.util.Enumeration;
import java.util.Vector;

public class EventEnumeration extends AggregateEnumeration {

    private String[] listNames = BlackBerryPIM.getInstance().listPIMLists(BlackBerryPIM.EVENT_LIST);

    // The currently enumerating list
    private EventList list = null;

    // Index of current list, starting right before the first item
    private int listIndex = -1;

    private int searchType;
    private long startDate;
    private long endDate;
    private String summary = null;
    private int access;

    public EventEnumeration(int searchType,
                            long startDate,
                            long endDate,
                            int access) {
        this.searchType = searchType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.access = access;
    }

    /**
     * Enumerate all events with the given summary
     *
     * @param summary value of summary field
     * @param access  read/write or both
     */
    public EventEnumeration(String summary, int access) {
        this.summary = summary;
        this.access = access;
    }

    public static Vector getEvents(int searchType,
                                   long startDate,
                                   long endDate,
                                   int access) {
        return new EventEnumeration(searchType, startDate, endDate, access).toVector();
    }

    public static Vector getEvents(String summary, int access) {
        return new EventEnumeration(summary, access).toVector();
    }


    public void close() {
        if (list != null) {
            try {
                list.close();
                list = null;
            } catch (PIMException e) {
                XLogger.warn(getClass(), "Can't close event list: " + PIMUtil.formatException(e));
            }
        }
    }


    public boolean hasMoreElements() {
        boolean hasMoreElements = false;
        try {
            hasMoreElements = super.hasMoreElements();
        } finally {
            if (!hasMoreElements) {
                close();
            }
        }
        return hasMoreElements;
    }

    protected Enumeration nextEnumeration() {
        // Loop until we find a list that we can read
        for (; ; ) {
            // Close the current list, if there is one
            close();

            if (++listIndex >= listNames.length) {
                return null;
            }

            // Try to open the next list and query it. If either fails, continue the loop.
            // If the query fails, the open list will be closed at the top of the next iter.
            try {
                list = (EventList) BlackBerryPIM.getInstance().openPIMList(BlackBerryPIM.EVENT_LIST,
                        access,
                        listNames[listIndex]);
                if (summary != null) {
                    Event query = list.createEvent();
                    query.addString(Event.SUMMARY, Event.ATTR_NONE, summary);
                    return list.items(query);
                } else {
                    return list.items(searchType, startDate, endDate, false);
                }
            } catch (PIMException e) {
                XLogger.warn(getClass(), "Can't read event list '" + listNames[listIndex] +
                        "': " + PIMUtil.formatException(e));
            } catch (SecurityException e) {
                XLogger.warn(getClass(), "Security exception reading event list '" + listNames[listIndex] +
                        "': " + e.toString());
            }
        }
    }

    private Vector toVector() {
        Vector container = new Vector();
        try {
            while (hasMoreElements()) {
                container.addElement(nextElement());
            }
        } catch (Exception e) {
            close();
            throw new EventEnumerationException(e.getMessage());
        }
        return container;
    }


    public static class EventEnumerationException extends RuntimeException {
        public EventEnumerationException(String message) {
            super(message);
        }
    }

}

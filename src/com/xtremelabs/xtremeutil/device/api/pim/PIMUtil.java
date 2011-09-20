package com.xtremelabs.xtremeutil.device.api.pim;

import net.rim.blackberry.api.pdap.BlackBerryEvent;
import net.rim.blackberry.api.pdap.BlackBerryPIM;
import net.rim.device.api.util.Arrays;

import javax.microedition.pim.*;

public class PIMUtil {

    private PIMUtil() {
    }

    public static String exceptionReasonString(PIMException ex) {
        switch (ex.getReason()) {
            case PIMException.FEATURE_NOT_SUPPORTED:
                return "feature not supported";
            case PIMException.GENERAL_ERROR:
                return "general error";
            case PIMException.LIST_CLOSED:
                return "list closed";
            case PIMException.LIST_NOT_ACCESSIBLE:
                return "list not accessible";
            case PIMException.MAX_CATEGORIES_EXCEEDED:
                return "max categories exceeded";
            case PIMException.UNSUPPORTED_VERSION:
                return "unsupported version";
            case PIMException.UPDATE_ERROR:
                return "update error";
            default:
                return "unknown exception type";
        }
    }

    public static String listTypeString(int type) {
        switch (type) {
            case BlackBerryPIM.CONTACT_LIST:
                return "contact";
            case BlackBerryPIM.EVENT_LIST:
                return "event";
            case BlackBerryPIM.MEMO_LIST:
                return "memo";
            case BlackBerryPIM.TODO_LIST:
                return "todo";
            default:
                return "unknown";
        }
    }

    public static String formatException(PIMException ex) {
        return "PIMException: " + ex.getMessage() + " (" + exceptionReasonString(ex) + ')';
    }

    public static PIMList getNamedListOrDefault(int type, int access, String name) throws PIMException {
        PIM pim = PIM.getInstance();
        if (name != null && Arrays.contains(pim.listPIMLists(type), name)) {
            return pim.openPIMList(type, access, name);
        }
        return pim.openPIMList(type, access);
    }

    /**
     * @param duration: length of the  event
     * @param label:    text for the event subject
     * @param note:     text for the event note
     * @throws javax.microedition.pim.PIMException
     *
     */
    public static void addCurrentEventToDefaultCalendar(long duration, String label, String note) throws PIMException {
        EventList eventList = (EventList) PIM.getInstance().openPIMList(PIM.EVENT_LIST, PIM.WRITE_ONLY);
        BlackBerryEvent evt = (BlackBerryEvent) eventList.createEvent();
        final long now = System.currentTimeMillis();
        final long endTime = now + duration;
        evt.addDate(BlackBerryEvent.START, Event.ATTR_NONE, now);
        evt.addDate(BlackBerryEvent.END, Event.ATTR_NONE, endTime);
        evt.addString(BlackBerryEvent.SUMMARY, Event.ATTR_NONE, label);
        evt.addString(BlackBerryEvent.NOTE, Event.ATTR_NONE, note);
        evt.addInt(BlackBerryEvent.FREE_BUSY, Event.ATTR_NONE, BlackBerryEvent.FB_BUSY);
        evt.commit();
        eventList.close();
    }
}

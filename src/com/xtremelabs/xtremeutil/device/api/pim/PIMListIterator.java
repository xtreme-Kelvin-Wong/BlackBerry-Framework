package com.xtremelabs.xtremeutil.device.api.pim;

import net.rim.blackberry.api.pdap.BlackBerryPIM;
import net.rim.blackberry.api.pdap.BlackBerryPIMList;

import javax.microedition.pim.PIMException;

public abstract class PIMListIterator {
    public static void each(int type, int access, PIMListIterator iter) {
        BlackBerryPIM pim = (BlackBerryPIM) BlackBerryPIM.getInstance();
        String[] listNames = pim.listPIMLists(type);
        for (int i = 0; i < listNames.length; i++) {
            try {
                BlackBerryPIMList list = (BlackBerryPIMList) pim.openPIMList(type, access, listNames[i]);
                try {
                    iter.run(list);
                } finally {
                    list.close();
                }
            } catch (PIMException e) {
                iter.onError(listNames[i], e);
            }
        }
    }

    public abstract void run(BlackBerryPIMList list);

    public abstract void onError(String listName, PIMException ex);
}

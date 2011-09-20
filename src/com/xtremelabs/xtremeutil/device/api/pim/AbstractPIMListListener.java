package com.xtremelabs.xtremeutil.device.api.pim;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.blackberry.api.pdap.BlackBerryPIMList;
import net.rim.blackberry.api.pdap.PIMListListener;

import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;

public abstract class AbstractPIMListListener implements PIMListListener {
    protected String listName;

    public AbstractPIMListListener(BlackBerryPIMList list) {
        this.listName = list.getName();
        list.addListener(this);
    }

    public void removeAsListener() {
        try {
            BlackBerryPIMList list = (BlackBerryPIMList) PIM.getInstance().openPIMList(PIM.EVENT_LIST, PIM.READ_WRITE, listName);
            try {
                list.removeListener(this);
            } finally {
                list.close();
            }
        } catch (PIMException e) {
            XLogger.error(getClass(), e);
        }
    }
}

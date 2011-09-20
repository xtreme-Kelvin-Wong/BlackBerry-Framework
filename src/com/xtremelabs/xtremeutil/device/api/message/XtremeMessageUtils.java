package com.xtremelabs.xtremeutil.device.api.message;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.blackberry.api.mail.ServiceConfiguration;
import net.rim.blackberry.api.mail.Session;
import net.rim.blackberry.api.mail.Store;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.ApplicationManager;

import java.util.Vector;

public class XtremeMessageUtils {
    public static Store[] getMessageStores() {
        Vector storeBuffer = new Vector();

        // From:  Article Number DB-00153:  What Is - Application is not notified when new messages arrive
        // http://www.blackberry.com/knowledgecenterpublic/livelink.exe/fetch/2000/348583/800332/800698/What_Is_-_Application_is_not_notified_when_new_messages_arrive.html?nodeid=800434&vernum=0
        ServiceRecord[] records = ServiceBook.getSB().getRecords();
        for (int i = 0; i < records.length; i++) {
            ServiceRecord record = records[i];
            if (record.getCid().equals("CMIME")) {
                ServiceConfiguration configuration = new ServiceConfiguration(record);
                Store store = Session.getInstance(configuration).getStore();
                storeBuffer.addElement(store);
            }
        }
        Store[] stores = new Store[storeBuffer.size()];
        storeBuffer.copyInto(stores);
        return stores;
    }

    public static Store waitForDefaultStore() {
        final ApplicationManager applicationManager = ApplicationManager.getApplicationManager();
        while (applicationManager.inStartup()) {
            try {
                Thread.sleep(300L);
            } catch (InterruptedException e) {
                XLogger.error(XtremeMessageUtils.class, e);
                return null;
            }
        }
        Session session = Session.getDefaultInstance();
        return session == null ? null : session.getStore();
    }

}

package com.changeme.app.barebone_changename.device.api.persistence;

import com.xtremelabs.xtremeutil.device.api.persistence.AbstractXPersistentStoreManagerImpl;
import com.xtremelabs.xtremeutil.device.api.runtimestore.RuntimeConstructorDelegate;
import com.xtremelabs.xtremeutil.device.api.runtimestore.RuntimeSingletonFactory;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.ContentProtectedHashtable;
import net.rim.device.api.util.Persistable;

import java.util.Hashtable;

/**
 * Created by Xtreme Labs Inc.
 * <p/><b/><br/>
 * DO NOT MOVE THIS CLASS to another package. Otherwise, all the previously stored data from
 * previous application will be deleted.  See Persistable interface documentation for more details
 */
public class PersistentStoreManager extends AbstractXPersistentStoreManagerImpl {
    private static PersistentStoreManager instance;
//    private static final CodeSigningKey CODE_SIGNING_KEY = CodeSigningKey.get("<key name>");

    private PersistentStoreManager() {
        super(ApplicationDescriptor.currentApplicationDescriptor().getModuleName());
    }

    public static PersistentStoreManager getInstance() {
        if (instance == null) {
            instance = (PersistentStoreManager) RuntimeSingletonFactory.getSingletonInstance(new RuntimeConstructorDelegate() {
                public Object create() {
                    return new PersistentStoreManager();
                }
            });
        }
        return instance;
    }

    public ContentProtectedHashtable makeContentProtectedHashTable(Hashtable hashContent) {
        return new PersistableHashtable(hashContent);
    }

    protected void storeContent(String partialKey, Object content) {
        synchronized (net.rim.device.api.system.PersistentStore.getSynchObject()) {
            PersistentObject persistentObject = PersistentStore.getPersistentObject(getKey(partialKey));
            persistentObject.setContents(content);
            /*replace above with below if code signing key exists*/
            //  persistentObject.setContents(new ControlledAccess(content, CODE_SIGNING_KEY));
            persistentObject.commit();
        }
    }

    /**
     * <p/><b/><br/>
     * DO NOT MOVE OR CHANGE THIS CLASS by adding methods or add member variables. Otherwise, all the previously stored data from
     * previous application will be deleted.  See Persistable interface documentation for more details
     */
    private static final class PersistableHashtable extends ContentProtectedHashtable implements Persistable {
        private PersistableHashtable(Hashtable content) {
            super(content, false);
        }
    }
}

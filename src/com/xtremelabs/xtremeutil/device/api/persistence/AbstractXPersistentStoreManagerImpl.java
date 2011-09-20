package com.xtremelabs.xtremeutil.device.api.persistence;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import com.xtremelabs.xtremeutil.util.string.XStringUtilities;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.ContentProtectedHashtable;
import net.rim.device.api.util.StringUtilities;

import java.util.Hashtable;

/**
 * Persistence store requirements
 * 1- When app is removed from device, all persistent content should be removed
 * 2- Changing the content and content structure of a model persisted should not blow away previously persisted content
 */
public abstract class AbstractXPersistentStoreManagerImpl implements XPersistentStoreManager {
    private String _appUniqueKey;

    /**
     * For testing only, app unique key should be provided so that any process can access persistent content if there is
     * a need.
     */
    AbstractXPersistentStoreManagerImpl() {
        this(null);
    }

    /**
     * Main constructor which uses appUniqueKey as a part of key to store persistent data
     *
     * @param appUniqueKey partial key passed associated with all persisted content for the app.  If null, only app process
     *                     can access persistent content
     */
    public AbstractXPersistentStoreManagerImpl(String appUniqueKey) {
        if (appUniqueKey == null) {
            _appUniqueKey = ApplicationDescriptor.currentApplicationDescriptor().getModuleName();
        } else {
            _appUniqueKey = appUniqueKey;
        }
    }

    /**
     * BlackBerry OS removes all persisted objects without an object handle. An object that explicitly implements Persistable
     * interface will guarantee stagnant objects are not left behind when the app is removed from device.
     *
     * @param hashContent base hastable that gets converted into a Persistable ContentProtectedHashtable
     * @return persistable ContentProtectedHashtable with hashContent that will be removed when the app is removewd from device
     */
    protected abstract ContentProtectedHashtable makeContentProtectedHashTable(Hashtable hashContent);


    /**
     * Store content has to be an instance of a subclass of ContenetProtectedHashtable that implements persistable otherwise
     * when app is removed stagnant persisted content remain on device
     *
     * @param partialKey used to create the unique key with which the persisted content is associated with
     * @param content    what gets stored inside the PersistentObject
     * @throws UndeletableObjectException thrown if the stored content would not be deleted from persistence if app is uninstalled
     */
    public void store(String partialKey, IHashBridge content) throws UndeletableObjectException {
        ContentProtectedHashtable storeContent = makeContentProtectedHashTable(content.convertToHash());
        if (storeContent.getClass().equals(ContentProtectedHashtable.class)) {
            throw new UndeletableObjectException();
        }
        storeContent(partialKey, storeContent);
    }

    /**
     * Loads the content of a model based on the Hastable retrieved from persistence
     *
     * @param partialKey key associated with the persisted content
     * @param loadInto   object the persistent content is load into
     * @return true if loading was successful
     */
    public boolean load(String partialKey, IHashBridge loadInto) {
        String thisClassName = XStringUtilities.getClassNameWithoutPackage(this);
        String loadIntoClassName = XStringUtilities.getClassNameWithoutPackage(loadInto);
        if (loadInto == null || partialKey == null) {
            XLogger.warn(getClass(), new StringBuffer()
                    .append("Illegal null argument passed in load method  loading from ").append(thisClassName)
                    .append("\n  loadInto: ").append(loadIntoClassName)
                    .append("\n  partialKey: ").append(partialKey).toString());
            return false;
        }
        try {
            Object rawObj = loadContent(partialKey);
            if (rawObj == null) {
                XLogger.debug(getClass(), new StringBuffer()
                        .append("error in loading into ").append(loadIntoClassName)
                        .append(" from ").append(thisClassName)
                        .append(":\ncontent was null").toString());
                return false;
            }
            if (rawObj instanceof ContentProtectedHashtable) {
                loadInto.recreateFromHash((ContentProtectedHashtable) rawObj);
                return true;
            } else {
                XLogger.warn(getClass(), new StringBuffer()
                        .append("error in loading into ").append(loadIntoClassName)
                        .append(" from ").append(thisClassName)
                        .append(":\ncontent persisted is not compatible").toString());
                return false;
            }
        } catch (Exception e) {
            XLogger.warn(getClass(), new StringBuffer()
                    .append("error in loading into ").append(loadIntoClassName)
                    .append(" from ").append(thisClassName)
                    .append(":\n").append(e).toString());
            return false;
        }
    }

    public void delete(String partialKey) {
        deleteContent(partialKey);
    }

    protected void storeContent(String partialKey, Object content) {
        synchronized (net.rim.device.api.system.PersistentStore.getSynchObject()) {
            PersistentObject persistentObject = PersistentStore.getPersistentObject(getKey(partialKey));
            persistentObject.setContents(content);
            persistentObject.commit();
        }
    }


    protected Object loadContent(String partialKey) {
        synchronized (PersistentStore.getSynchObject()) {
            PersistentObject persistentObject = PersistentStore.getPersistentObject(getKey(partialKey));
            return persistentObject.getContents();
        }
    }

    protected void deleteContent(String partialKey) {
        synchronized (PersistentStore.getSynchObject()) {
            PersistentStore.destroyPersistentObject(getKey(partialKey));
        }
    }

    protected long getKey(String partialKey) {
        return StringUtilities.stringHashToLong(new StringBuffer().append(_appUniqueKey).append(partialKey).toString());
    }


}

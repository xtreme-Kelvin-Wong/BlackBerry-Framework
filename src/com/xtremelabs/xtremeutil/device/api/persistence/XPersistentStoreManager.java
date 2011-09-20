package com.xtremelabs.xtremeutil.device.api.persistence;


public interface XPersistentStoreManager {
    /**
     * @see AbstractXPersistentStoreManagerImpl#store
     */
    public void store(String filename, IHashBridge content) throws UndeletableObjectException;

    /**
     * @see AbstractXPersistentStoreManagerImpl#load
     */
    boolean load(String filename, IHashBridge loadInto);

    /**
     * @see AbstractXPersistentStoreManagerImpl#delete
     */
    void delete(String filename);

}

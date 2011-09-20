package com.xtremelabs.xtremeutil.device.api.persistence;

import java.util.Hashtable;

/**
 * Created by Xtreme Labs Inc.
 */
public interface IHashBridge {

    public Hashtable convertToHash();

    public void recreateFromHash(Hashtable hashtable);


}

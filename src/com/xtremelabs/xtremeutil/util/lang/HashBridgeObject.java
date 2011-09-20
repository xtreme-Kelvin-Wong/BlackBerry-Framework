package com.xtremelabs.xtremeutil.util.lang;

import java.util.Hashtable;


public interface HashBridgeObject {
    public Hashtable convertToHash();

    public void recreateFromHash(Hashtable hashtable);
}

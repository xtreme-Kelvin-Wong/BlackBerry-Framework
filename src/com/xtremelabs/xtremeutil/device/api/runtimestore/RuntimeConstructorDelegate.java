package com.xtremelabs.xtremeutil.device.api.runtimestore;

import net.rim.device.api.system.CodeModuleManager;
import net.rim.device.api.util.StringUtilities;

public abstract class RuntimeConstructorDelegate {
    public abstract Object create();

    protected long getGuid() {
        return StringUtilities.stringHashToLong(new StringBuffer(getClass().getName())
                .append(CodeModuleManager.getModuleHandleForClass(getClass()))
                .toString());
    }
}



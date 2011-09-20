package com.xtremelabs.xtremeutil.device.api.persistence;

public class UndeletableObjectException extends Exception {
    public UndeletableObjectException() {
        super("Class provided will not be deleted from persistent store if application is removed!");
    }
}

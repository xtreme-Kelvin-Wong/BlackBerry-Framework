package com.xtremelabs.xtremeutil.util.lang;


public interface SerializableObject {
    public String serializeToString();

    public void deserializeFromString(String string) throws Exception;
}

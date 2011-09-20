package com.xtremelabs.xtremeutil.util.lang;

import net.rim.device.api.util.EmptyEnumeration;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * Implement this class to compose a sequence of <code>Enumeration</code>s into a single <code>Enumeration</code>.
 */
public abstract class AggregateEnumeration implements Enumeration {

    private Enumeration curEnum = new EmptyEnumeration();

    /**
     * Get the next enumeration in the aggregate sequence.
     *
     * @return the next <code>Enumeration</code> in the sequence or <code>null</code> if there are no more
     */
    protected abstract Enumeration nextEnumeration();

    public boolean hasMoreElements() {
        for (; ;) {
            if (curEnum == null) {
                return false;
            }
            if (curEnum.hasMoreElements()) {
                return true;
            }
            curEnum = nextEnumeration();
        }
    }

    public Object nextElement() {
        if (!hasMoreElements()) {
            throw new NoSuchElementException();
        }
        return curEnum.nextElement();
    }
}

package com.xtremelabs.xtremeutil.util.lang;

import net.rim.device.api.util.Arrays;
import net.rim.device.api.util.Comparator;

import java.util.Vector;

public class Queue extends Vector {
    private int maxSize;

    public Queue(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    public Queue() {
        this(-1);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized Object enqueue(Object element) {
        addElement(element);
        if (maxSize != -1 && size() > maxSize) {
            dequeue();
        }
        return element;
    }

    public synchronized Object dequeue() {
        int len = size();
        if (len == 0) {
            throw new EmptyQueueException();
        }
        Object obj = elementAt(0);
        removeElementAt(0);
        return obj;
    }

    public void sort(Comparator comparator) {
        Object[] objArray = new Object[size()];
        copyInto(objArray);
        Arrays.sort(objArray, comparator);
        for (int i = 0; i < objArray.length; i++) {
            setElementAt(objArray[i], i);
        }
    }
}

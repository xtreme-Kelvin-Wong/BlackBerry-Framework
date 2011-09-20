package com.xtremelabs.xtremeutil.util.lang;

/*
 * Stack.java 
 */
import java.util.Vector;

/**
 *  Basic stack implementation.
 */
public class Stack {
    
    private Vector _stack = new Vector();
    
    public Object pop() {
        int size = _stack.size();
        Object top = _stack.elementAt(size - 1);
        _stack.removeElementAt(size - 1);
        return top;
    }
    
    public void push(Object o) {
        _stack.addElement(o);
    }
        
    public Object peekBottom() {
    	int size = _stack.size();
        if (size == 0) {
            return null;
        }
        return _stack.elementAt(0);
    }
    
    public Object peek() {
        int size = _stack.size();
        if (size == 0) {
            return null;
        }
        return _stack.elementAt(size - 1);
    }
    
    
    public int size() {
        return _stack.size();
    }

}


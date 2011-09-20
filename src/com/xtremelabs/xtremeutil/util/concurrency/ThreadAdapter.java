package com.xtremelabs.xtremeutil.util.concurrency;

/**
 * ThreadAdapter allows for the actual thread creation to be delayed until the {@link #start()} method is called.
 * This allows threads to be passed in other processes without having them terminated when the original process dies.
 * @see ThreadUtils
 *
 * @author Sina Sojoodi
 */
public class ThreadAdapter implements Runnable {
    private Runnable _target;
    private String _name;
    private int _priority = Thread.NORM_PRIORITY;

    protected Thread _thread;

    public ThreadAdapter() {
        this(null, null);
    }

    public ThreadAdapter(Runnable target) {
        this(target, null);
    }

    public ThreadAdapter(String name) {
        this(null, name);
    }

    public ThreadAdapter(Runnable target, String name) {
        _target = target;
        _name = name;
    }

    public synchronized void start() {
        if (_target != null) {
            if (_name != null) {
                _thread = new Thread(_target, _name);
            } else {
                _thread = new Thread(_target);
            }
        } else {
            if (_name != null) {
                _thread = new Thread(this, _name);
            } else {
                _thread = new Thread(this);
            }
        }
        _thread.start();
    }

    public void run() {
        // subclasses should override
    }

    public void interrupt() {
        if (_thread != null && _thread.isAlive()) {
            _thread.interrupt();
        }
    }

    public final boolean isAlive() {
        return (_thread != null && _thread.isAlive());

    }

    public final void setPriority(int priority) {
        _priority = priority;
        if (_thread != null)
            _thread.setPriority(priority);
    }

    public final int getPriority() {
        return _priority;

    }

    public final java.lang.String getName() {
        return _name;
    }


    public final void join() throws java.lang.InterruptedException {
        if (_thread != null && _thread.isAlive()) {
            _thread.join();
        }
    }

}

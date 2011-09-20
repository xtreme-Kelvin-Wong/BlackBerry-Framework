package com.xtremelabs.xtremeutil.util.concurrency;

/*
 * ThreadRunner.java
 */

import com.xtremelabs.xtremeutil.util.logger.XLogger;

import java.util.Vector;

/**
 * This is a quick and dirty implementation of a thread pooling.
 * this class neither supports cancel, suspend, resume operation of assigned tasks,
 * nor generates any call-back for completion, and hence should only
 * be used for very quick and simple tasks such as event handling, assigning command
 * to other sophisticated operations.
 *
 */

public class ThreadQueue {
    private static final int DEFAULT_NUMBER_OF_WORKERS = 4;

    private static final int DEFAULT_WORKER_PRIORITY = Thread.MIN_PRIORITY;

    private int numberOfWorkers;

    private int workerPriority;

    private boolean createThreadOnProxyProcess;

    private final Vector queue = new Vector();

    private final Worker[] workers;

    private volatile boolean done = false;

    public ThreadQueue() {
        this(DEFAULT_NUMBER_OF_WORKERS, DEFAULT_WORKER_PRIORITY, false);
    }

    public ThreadQueue(int numberOfWorkers, int workerPriority, boolean createThreadOnProxyProcess) {
        this.numberOfWorkers = numberOfWorkers;
        this.workerPriority = workerPriority;
        this.createThreadOnProxyProcess = createThreadOnProxyProcess;
        workers = new Worker[numberOfWorkers];
        ensureThreadRunning();
    }

    public void ensureThreadRunning() {
        synchronized (workers) {
            for (int i = 0; i < numberOfWorkers; i++) {
                if (workers[i] == null) {
                    workers[i] = new Worker("Worker " + i);
                    workers[i].setPriority(workerPriority);
                }

                if (!workers[i].isAlive()) {
                    // todo: remove references to Proxy
//                    if (createThreadOnProxyProcess)
//                    {
//                        Proxy.getInstance().startThread(workers[i]);
//                    }
//                    else
//                    {
                    workers[i].start();
//                    }
                }
            }
        }
    }

    public void enqueue(Runnable runnable) {
        enqueue(runnable, false);
    }

    public void enqueue(Runnable runnable, boolean priority) {
        if (!done) ensureThreadRunning();

        synchronized (queue) {
            if (!done) {
                if (priority) {
                    queue.insertElementAt(runnable, 0);
                } else {
                    queue.addElement(runnable);
                }
                queue.notifyAll();
            }
        }
    }

    public int size() {
        return queue.size();
    }

    public int getNumberOfWorkers() {
        return numberOfWorkers;
    }

    public boolean createThreadOnProxyProcess() {
        return createThreadOnProxyProcess;
    }

    public synchronized void shutdown() {
        done = true;
        for (int i = 0; i < numberOfWorkers; i++) {
            try {
                workers[i].interrupt();
                workers[i].join();
            }
            catch (Exception e) {
                // ignore
            }
        }
    }


    private class Worker extends Thread {
        public Worker(String name) {
            super(name);
        }

        public void run() {
            XLogger.info(getClass(), this.getName() + " started");

            while (!done) {
                Runnable runnable = null;

                synchronized (queue) {
                    while (!done && queue.isEmpty()) {
                        try {
                            queue.wait();
                        }
                        catch (InterruptedException e) {
                            // ignored
                        }
                    }

                    if (done) break;

                    runnable = (Runnable) queue.elementAt(0);
                    queue.removeElementAt(0);
                }

                try {
                    runnable.run();
                }
                catch (Exception e) {
                    handleError(e);
                }
                catch (Throwable t) {
                    handleError(t);
                }
            }

           	XLogger.info(getClass(), this.getName() + " stoppedd");
        }

        private void handleError(Throwable t) {
            // we basically ignore
        	XLogger.error(getClass(), "Exception in Thread: [ " + getName() + "]: "
                    + t.getClass().getName() + ": "
                    + t.getMessage());
        }
    }
}

package com.xtremelabs.xtremeutil.net.api;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import com.xtremelabs.xtremeutil.net.IConnectionModifier;
import net.rim.device.api.util.Comparator;
import net.rim.device.api.util.LongHashtable;
import net.rim.device.api.util.SimpleSortingVector;

import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

public class RequestQueue implements Comparator, IConnectionModifier {
    /**
     * The default number of concurrent active server requests (and thereby the default number of <code>threads</code>
     */
    public final static int DEFAULT_BURST_SIZE = 4;

    private final Object lock = new Object();
    private final Random random = new Random();

    private volatile LongHashtable _queueTime;
    private volatile SimpleSortingVector _waitQueue;
    private volatile SimpleSortingVector _pausedQueue;

    private RequestExecutor[] _executors;

    public RequestQueue() {
        this(DEFAULT_BURST_SIZE);
    }

    protected RequestQueue(int activeRequestCount) {
        _queueTime = new LongHashtable(10);
        _waitQueue = new SimpleSortingVector();
        _pausedQueue = new SimpleSortingVector();
        _waitQueue.setSortComparator(this);
        _pausedQueue.setSortComparator(this);

        _executors = new RequestExecutor[activeRequestCount];

        for (int i = 0, ii = _executors.length; i < ii; ++i) {
            _executors[i] = new RequestExecutor();
            _executors[i].start();
        }
    }

    public void shutdown() {
        synchronized (lock) {
            for (int i = 0, ii = _executors.length; i < ii; ++i) {
                _executors[i].finish();
            }
            lock.notifyAll();
        }
    }

    public void modify(StreamConnection connection) throws IOException {

    }

    public Vector getWaitQueue() {
        return _waitQueue;
    }

    public LongHashtable getQueueTime() {
        return _queueTime;
    }

    //TODO: write batch enqueue that is faster
    public void enqueue(Request request) {
        // puts the request onto a queue, but does not send the request
        request.addConnectionModifier(this);

        synchronized (lock) {
            _waitQueue.addElement(request);
            _waitQueue.reSort();
            _queueTime.put(System.currentTimeMillis(), request);
        }
    }


    public void pauseAllQueued() {
        synchronized (lock) {
            for (int i = _waitQueue.size() - 1; i >= 0; --i) {
                _pausedQueue.addElement(_waitQueue.elementAt(i));
                _waitQueue.removeElementAt(i);
            }
            _pausedQueue.reSort();
        }

    }

    public void sendAllQueued() {
        synchronized (lock) {
            for (int i = _pausedQueue.size() - 1; i >= 0; --i) {
                _waitQueue.addElement(_pausedQueue.elementAt(i));
                _pausedQueue.removeElementAt(i);
            }
            _waitQueue.reSort();
            lock.notifyAll();
        }
    }

    public void send(Request request) {
        request.addConnectionModifier(this);

        synchronized (lock) {
            _waitQueue.addElement(request);
            _waitQueue.reSort();
            _queueTime.put(System.currentTimeMillis(), request);
            lock.notify();
        }
    }

    public void stopRequest(Request request) {
        synchronized (lock) {
            
        }
    }

    public Vector removeRequestsWithPriority(int priority) {
        synchronized (lock) {
            int size = _waitQueue.size();
            Vector removedRequests = new Vector(size);
            for (int i = 0; i < size; ++i) {
                Request request = (Request) _waitQueue.elementAt(i);
                if (request.getPriority() == priority) {
                    removedRequests.addElement(_waitQueue.elementAt(i));
                }
            }
            size = removedRequests.size();
            for (int i = 0, ii = removedRequests.size(); i < ii; ++i) {
                Request request = (Request) removedRequests.elementAt(i);
                _waitQueue.removeElement(request);
            }

            return removedRequests;
        }
    }

    public int compare(Object o1, Object o2) {
        if (!(o1 instanceof Request) || !(o2 instanceof Request)) {
            return -1;
        }

        Request first = (Request) o1;
        Request second = (Request) o2;

        if (first.getPriority() == second.getPriority()) {
            long key1 = _queueTime.getKey(first);
            long key2 = _queueTime.getKey(second);

            if (key1 == key2) {
                return 0;
            }

            return key1 < key2 ? -1 : 1;
        }

        return first.getPriority() < second.getPriority() ? -1 : 1;
    }

    class RequestExecutor extends Thread {
        boolean _finished;

        public RequestExecutor() {
            setPriority(Thread.NORM_PRIORITY - 2);
            _finished = false;
        }

        public void finish() {
            _finished = true;
        }

        public void run() {
            while (!_finished) {

                Request request = null;

                synchronized (lock) {
                    while (_waitQueue.isEmpty() && !_finished) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                        }
                    }

                    // Queue is non-empty, but we may be finished
                    if (_finished) {
                        break;
                    }

                    request = (Request) _waitQueue.elementAt(0);
                    _waitQueue.removeElement(request);

                    // Skip if request is canceled or finished
                    if (request.isCancelled() || request.isFinished()) {
                        continue;
                    }
                }

                try {
                    // Random delay between 0-70 ms. We do this so that
                    // multiple requests don't start exactly at the same time
                 /*   long delay = random.nextInt(35) + random.nextInt(35);  //disable delay by default
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                    }
*/
                    request.executeRequest();

                    long longKey = _queueTime.getKey(request);
                    if (_queueTime.remove(longKey) == null) {
                        // XLogger.warn(getClass(), "didn't remove from the queue here.");
                    }
                } catch (Throwable e) {
                    XLogger.error(getClass(), e.toString());
                }
            }
        }
    }
}

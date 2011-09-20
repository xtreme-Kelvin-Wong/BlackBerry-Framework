package com.xtremelabs.xtremeutil.device.api.runtimestore;

import com.xtremelabs.xtremeutil.util.logger.XLogger;
import net.rim.device.api.system.ControlledAccessException;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.util.IntHashtable;
import net.rim.device.api.util.StringUtilities;


/**
 * <b/><p/><br/>
 * IMPORTANT: DO NOT LOG using XLogger in this class as the circular reference causes complex bugs
 */
public class RuntimeSingletonFactory {
//    private static final CodeSigningKey CODE_SIGNING_KEY = CodeSigningKey.get("<name of code signing public key>");

    private static IntHashtable locksContainer;

    protected static interface StoreOperation {
        public Object run(RuntimeStore store);
    }

    /**
     * A fatal error thrown from any of the static methods methods
     */
    public static class Disaster extends RuntimeException {
        public Disaster(String message) {
            super(message);
        }

    }

    /**
     * Create or return the existing global singleton for the given class delegated by the RuntimeConstructorDelegate object
     *
     * @param delegate constructs the base instance of the global singleton and is used to generate the runtime store id
     * @return the singular instance of the given class
     * @throws IllegalArgumentException if the given instance is <code>null</code>
     */
    public static Object getSingletonInstance(final RuntimeConstructorDelegate delegate) {
        final long id = delegate.getGuid();
        return withStore(new StoreOperation() {
                    public Object run(RuntimeStore store) {
                        Object o = store.get(id);
                        if (o == null) {
                            o = delegate.create();
                            if (o == null) {
                                throw new IllegalArgumentException("global singleton instance can't be null");
                            }
//                    o = new ControlledAccess(o, CODE_SIGNING_KEY);
                            try {
                                store.put(id, o);
                            } catch (IllegalArgumentException e) {
                                Object oldObj = store.replace(id, o);
                                try {

                                    final String errorString = "runtime store put threw ";
                                    EventLogger.logEvent(XLogger.GUID, errorString.getBytes(), EventLogger.DEBUG_INFO);

                                    final String message = new StringBuffer("replacing existing instance (possibly wrong class):")
                                            .append(oldObj)
                                            .append(" with: ")
                                            .append(o)
                                            .toString();
                                    EventLogger.logEvent(XLogger.GUID, errorString.getBytes(), EventLogger.WARNING);
                                } catch (Exception loggerException) {
                                    System.err.println("replacing existing instance failed and logger failed" + loggerException);
                                }
                            }
                        }
                        // runtime store automatically uwrapps ControlAccess Object
                        return store.get(id);
                    }
                }, delegate.getClass());
    }


    /**
     * Synchronization on the store object itself may blocks all other processes synchronizing on the store object and
     * causes system instability.  Realistically, we only need synchronization on the singlton objects themselves not the
     * entire store.
     * <p>Note: the operation of getting the lock itself is synchronized on the store object itself.  getLock(Class klass)
     * takes 0.04ms (very small!)</p>
     *
     * @param klass Class object used to extract the lock
     * @return lockObject for a class
     */
    protected static Object getLock(Class klass) {
        final RuntimeStore store = RuntimeStore.getRuntimeStore();
        synchronized (store) {
            if (locksContainer == null) {
                long id = StringUtilities.stringHashToLong(RuntimeSingletonFactory.class.getName() + "storeLock");
                Object content = store.get(id);
                if (content == null || !(content instanceof IntHashtable)) {
                    locksContainer = new IntHashtable();
                    store.replace(id, locksContainer);
                } else {
                    locksContainer = (IntHashtable) content;
                }
            }
            final int classHash = klass.getName().hashCode();
            Object lock = locksContainer.get(classHash);
            if (lock == null) {
                lock = new Object();
                locksContainer.put(classHash, lock);
            }
            return lock;
        }
    }

    /**
     * Helper method to perform a synchronized operation on the runtime store. The global
     * store object is locked and then passed to the <code>run</code> method of the given
     * <code>StoreOperation</code>.
     *
     * @param block passed the global RuntimeStore, which will be locked for the duration
     * @param klass contextual class, used only for error messages
     * @return the value returned from the block
     */
    protected static Object withStore(StoreOperation block, Class klass) {
        try {
            final RuntimeStore store = RuntimeStore.getRuntimeStore();
            synchronized (getLock(klass)) {
                return block.run(store);
            }
        } catch (ControlledAccessException ex) {
            error(klass, ex, "not authorized to access runtime store");
        }
        return null; // unreachable
    }

    /**
     * Helper method to handle fatal errors (by throwing an informative Disaster exception)
     *
     * @param klass   a class related to the original exception
     * @param ex      the original exception
     * @param message a specific message related to the error condition
     * @throws Disaster always
     */
    protected static void error(Class klass, Exception ex, String message) throws Disaster {
        String text = new StringBuffer("Failed to construct/return the singleton object.")
                .append("\nReference class: ").append(klass.getName())
                .append("\nReference message: ").append(message)
                .append("\nReference exception: ").append(ex).toString();
        try {
            EventLogger.logEvent(XLogger.GUID, text.getBytes(), EventLogger.SEVERE_ERROR);
        } catch (Exception e) {
            System.err.println("logger failed!\n" + e + "\noriginal text:\n" + text);
        }
        throw new Disaster(text);
    }

    /**
     * Explicitly set the global singleton instance for a class.
     *
     * @param delegate constructs the base instance of the global singleton and is used to generate the runtime store id
     * @throws IllegalArgumentException if the given instance is <code>null</code>
     * @throws IllegalStateException    if there is already a global singleton instance for the given class
     */
    public static void setSingletonInstance(final RuntimeConstructorDelegate delegate) {
        final Object instance = delegate.create();
        if (instance == null) {
            throw new IllegalArgumentException("global singleton instance can't be null");
        }
        final long id = delegate.getGuid();

        withStore(new StoreOperation() {
                    public Object run(RuntimeStore store) {
                        Object existing = store.get(id);
                        if (existing == null) {
                            store.put(id, instance);
                        } else if (existing != instance) {
                            throw new IllegalStateException("a different global singleton already exists for the given class");
                        }
                        return null;
                    }
                }, instance.getClass());
    }


    /**
     * Removes the global singleton for the given class and returns it
     *
     * @param delegate for which to remove the global singleton
     * @return the former global instance or <code>null</code> if none existed for the given class
     */
    public static Object removeSingletonInstance(RuntimeConstructorDelegate delegate) {
        if (delegate == null) {
            XLogger.warn(RuntimeSingletonFactory.class, "could not remove singlton instance, delegate was null");
            return null;
        }
        final long id = delegate.getGuid();
        return withStore(new StoreOperation() {
                    public Object run(RuntimeStore store) {
                        return store.remove(id);
                    }
                }, delegate.getClass());
    }

    /**
     * Return the existing global singleton for the given class, or <code>null</code> if
     * there is no global singleton for the class.
     *
     * @param delegate for which to remove the global singleton
     * @return the global singleton instance for the given class
     */
    public static Object getExistingSingletonInstance(final RuntimeConstructorDelegate delegate) {
        return withStore(new StoreOperation() {
                    public Object run(RuntimeStore store) {
                        return store.get(delegate.getGuid());
                    }
                }, delegate.getClass());
    }

}

package com.xtremelabs.xtremeutil.device.api.runtimestore;

import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.util.StringUtilities;
import rimunit.TestSuite;

import java.util.Vector;

public class RuntimeSingletonFactoryTestSuite extends TestSuite {

    protected Class[] suite() {
        return new Class[]{
                TestCreateAndReturnInstance.class,
                TestSetInstance.class,
                TestRetrieveExistingInstance.class,
                TestRemoveInstance.class,
        };
    }

    protected static class Thingy {
    }


    protected static abstract class TestCase extends rimunit.TestCase {
        Vector guids = new Vector();

        protected class ThingyDelegate extends RuntimeConstructorDelegate {
            Thingy thingy;

            protected final Object getThingy() {
                if (thingy == null) {
                    guids.addElement(new Long(getGuid()));
                    thingy = new Thingy();
                }
                return thingy;
            }

            public Object create() {
                return getThingy();
            }

            protected long getGuid() {
                return StringUtilities.stringHashToLong(getClass().getName() + hashCode());
            }
        }

        public void tearDown() throws Exception {
            final RuntimeStore store = RuntimeStore.getRuntimeStore();
            synchronized (store) {
                for (int i = 0; i < guids.size(); i++) {
                    long guid = ((Long) guids.elementAt(i)).longValue();
                    store.remove(guid);
                }
            }
        }
    }

    public static class TestCreateAndReturnInstance extends TestCase {
        public void run() throws Exception {
            final ThingyDelegate thingyDelegate = new ThingyDelegate();
            assertSame("new global singleton should have the same class as the one constructed via deligate",
                    thingyDelegate.getThingy(),
                    RuntimeSingletonFactory.getSingletonInstance(thingyDelegate));

            assertNotNull("global singleton of a legitimate class should not return null",
                    RuntimeSingletonFactory.getSingletonInstance(thingyDelegate));

            final ThingyDelegate delegate1 = new ThingyDelegate();
            assertSame("should always return the same global singleton object for a particular class",
                    RuntimeSingletonFactory.getSingletonInstance(delegate1),
                    RuntimeSingletonFactory.getSingletonInstance(delegate1));

        }
    }

    public static class TestSetInstance extends TestCase {
        public void run() throws Exception {

            final RuntimeConstructorDelegate nullDelegate = new ThingyDelegate() {
                public Object create() {
                    return null;
                }
            };


            assertThrows("should not allow global singleton to be set to null",
                    IllegalArgumentException.class,
                    new TestBlock() {
                        public void run() throws Exception {
                            RuntimeSingletonFactory.setSingletonInstance(nullDelegate);
                        }
                    });


            final ThingyDelegate thingyDelegate = new ThingyDelegate();

            RuntimeSingletonFactory.setSingletonInstance(thingyDelegate);
            assertSame("should allow global singleton to be set explicitly",
                    thingyDelegate.getThingy(),
                    RuntimeStore.getRuntimeStore().get(thingyDelegate.getGuid()));

            assertDoesntThrow("should allow global singleton to be repeatedly set to the same object",
                    IllegalStateException.class,
                    new TestBlock() {
                        public void run() throws Exception {
                            RuntimeSingletonFactory.setSingletonInstance(thingyDelegate);
                        }
                    });

            final RuntimeConstructorDelegate newThingyDelegate = new ThingyDelegate() {
                protected long getGuid() {
                    return thingyDelegate.getGuid();
                }
            };
            assertThrows("should not allow existing global singleton to be changed",
                    IllegalStateException.class,
                    new TestBlock() {
                        public void run() throws Exception {
                            RuntimeSingletonFactory.setSingletonInstance(newThingyDelegate);
                        }
                    });
        }
    }

    public static class TestRetrieveExistingInstance extends TestCase {
        public void run() throws Exception {
            final TestCase.ThingyDelegate delegate0 = new ThingyDelegate();
            assertNull("should return null if asked for an existing instance that does not exist",
                    RuntimeSingletonFactory.getExistingSingletonInstance(delegate0));


            final ThingyDelegate delegate1 = new ThingyDelegate();
            assertSame("should return the same global singleton after getSingletonInstance creation",
                    RuntimeSingletonFactory.getSingletonInstance(delegate1),
                    RuntimeSingletonFactory.getExistingSingletonInstance(delegate1));

            final ThingyDelegate delegate2 = new ThingyDelegate();
            RuntimeSingletonFactory.setSingletonInstance(delegate2);
            assertSame("should return the same global singleton after setSingletonInstance creation",
                    delegate2.getThingy(),
                    RuntimeSingletonFactory.getExistingSingletonInstance(delegate2));
        }
    }

    public static class TestRemoveInstance extends TestCase {
        public void run() throws Exception {
            final ThingyDelegate thingyDelegate = new ThingyDelegate();
            assertNull("should return null when asked to remove singleton instance that does not exist",
                    RuntimeSingletonFactory.removeSingletonInstance(thingyDelegate));

            final ThingyDelegate thingyDelegate1 = new ThingyDelegate();
            RuntimeSingletonFactory.setSingletonInstance(thingyDelegate1);
            assertSame("should return global singleton when it is removed",
                    thingyDelegate1.getThingy(),
                    RuntimeSingletonFactory.removeSingletonInstance(thingyDelegate1));

            assertNull("should remove singleton instance",
                    RuntimeSingletonFactory.getExistingSingletonInstance(thingyDelegate1));
        }
    }
}


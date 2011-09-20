package com.xtremelabs.xtremeutil.device.api.persistence;

import net.rim.device.api.util.ContentProtectedHashtable;
import net.rim.device.api.util.Persistable;
import rimunit.TestCase;

import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: xtremelabs
 * Date: 29/06/11
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersistenceTest {

    public static class TestPersistentStoreManager extends AbstractXPersistentStoreManagerImpl {

        private static TestPersistentStoreManager instance = new TestPersistentStoreManager();

        public static TestPersistentStoreManager getInstance() {
            return instance;
        }

        protected ContentProtectedHashtable makeContentProtectedHashTable(Hashtable hashContent) {
            return new TestContentProtectedHashtable(hashContent);
        }

        private static final class TestContentProtectedHashtable extends
                ContentProtectedHashtable implements Persistable {

            private TestContentProtectedHashtable(Hashtable content) {
                super(content, false);
            }
        }
    }

    public static class LoadNonExistantKeyTest extends TestCase {

        public void run() throws Exception {

            boolean result = TestPersistentStoreManager.getInstance().load("non-existant key", new DummyHashBridge());

            assertFalse("Load failed with no exceptions:", result);
        }
    }

    public static class DummyHashBridge implements IHashBridge {

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Hashtable convertToHash() {
            Hashtable hash = new Hashtable();

            hash.put("data", data);

            return hash;
        }

        public void recreateFromHash(Hashtable hashtable) {

            if(hashtable.containsKey("data")) {
                data = (String)hashtable.get("data");
            }
        }
    }

    public static class LoadKeyTest extends TestCase {
        public void run() throws Exception {

            DummyHashBridge dummy = new DummyHashBridge();

            dummy.setData("testdata---------------------------");

            TestPersistentStoreManager.getInstance().store("testkey", dummy);

            DummyHashBridge loadedDummy = new DummyHashBridge();

            TestPersistentStoreManager.getInstance().load("testkey", loadedDummy);

            assertEquals("Stored data is equal to loaded data", dummy.getData(), loadedDummy.getData());
        }
    }

    public static class DeleteKeyTest extends TestCase {
        public void run() throws Exception {
                        DummyHashBridge dummy = new DummyHashBridge();

            dummy.setData("test data for delete ----------------------");

            TestPersistentStoreManager.getInstance().store("deletekey", dummy);

            TestPersistentStoreManager.getInstance().delete("deletekey");

            DummyHashBridge loadedDummy = new DummyHashBridge();

            boolean loadResult = TestPersistentStoreManager.getInstance().load("deletekey", loadedDummy);

            assertFalse("Original data failed to load after deletion", loadResult);
            assertTrue("Attempted load did not result in any data being written to loadedDummy", loadedDummy.getData() == null);
        }
    }
}

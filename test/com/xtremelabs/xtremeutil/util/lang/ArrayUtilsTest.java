package com.xtremelabs.xtremeutil.util.lang;

import rimunit.TestCase;

public class ArrayUtilsTest {
    public static class TestContainsFunction extends TestCase {
        public void run() throws Exception {
            int[] a = new int[]{0, 1, 345, 3};
            assertTrue("Array contains 0", ArrayUtils.contains(a, 0));
            assertTrue("Array contains 1", ArrayUtils.contains(a, 1));
            assertTrue("Array contains 3", ArrayUtils.contains(a, 3));
            assertTrue("Array contains 345", ArrayUtils.contains(a, 345));
            assertFalse("Array does not contain 666", ArrayUtils.contains(a, 666));
        }
    }
}

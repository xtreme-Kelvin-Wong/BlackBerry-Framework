/**
 * Created by IntelliJ IDEA.
 * User: xtremelabs
 * Date: Aug 6, 2009
 * Time: 5:03:30 PM
 * To change this template use File | Settings | File Templates.
 */
package com.xtremelabs.xtremeutil.util.lang;

import net.rim.device.api.util.Comparator;
import rimunit.TestCase;

public class StackTest {
    private abstract static class StackTestCase extends TestCase {
        protected Stack sq;
        protected Object[] objs;
        protected int numObjs;

        public void setUp() throws Exception {
            numObjs = 5;
            sq = new Stack();
            objs = new Object[numObjs];
            for (int i = 0; i < objs.length; i++) {
                objs[i] = new Object();
            }
        }
    }

    public static class TestPush extends StackTestCase {
        public void run() throws Exception {
            for (int i = 0; i < numObjs; i++) {
                sq.push(objs[i]);
            }
            assertEquals("size should be", objs.length, sq.size());
            assertEquals("bottom element should be", objs[0], sq.peekBottom());
            assertEquals("top element should be", objs[numObjs - 1], sq.peek());
        }
    }   

    public static class TestPop extends StackTestCase {
        public void run() throws Exception {
            for (int i = 0; i < numObjs; i++) {
                sq.push(objs[i]);
            }
            sq.pop();
            sq.pop();
            assertEquals("size should be", numObjs - 2, sq.size());
            assertEquals("bottom element should be", objs[0], sq.peekBottom());
            assertEquals("top element should be", objs[numObjs - 3], sq.peek());
        }
    }
}
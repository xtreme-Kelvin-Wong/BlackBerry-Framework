package com.xtremelabs.xtremeutil.util.lang;

import net.rim.device.api.util.EmptyEnumeration;
import net.rim.device.api.util.ObjectEnumerator;
import rimunit.TestSuite;

import java.util.Enumeration;
import java.util.NoSuchElementException;


public class AggregateEnumerationTest extends TestSuite {

    protected Class[] suite() {
        return new Class[]{
                TestNonEmpty.class,
                TestEmpty.class,
                TestBlindIteration.class,
                TestOneMember.class,
                TestEmptyMembers.class,
                TestNesting.class
        };
    }

    public abstract static class TestCase extends rimunit.TestCase {

        protected static class ZZ extends AggregateEnumeration {
            private Enumeration[] enums;
            private int i = 0;

            public ZZ(Enumeration[] a) {
                enums = a;
            }

            public ZZ(Enumeration a) {
                enums = new Enumeration[]{a};
            }

            public ZZ(Enumeration a, Enumeration b, Enumeration c) {
                enums = new Enumeration[]{a, b, c};
            }

            protected Enumeration nextEnumeration() {
                return (i < enums.length) ? enums[i++] : null;
            }
        }

        protected static final Object A = "A",
                B = "B",
                C = "C",
                D = "D",
                E = "E",
                F = "F",
                G = "G",
                H = "H",
                I = "I";

        protected Enumeration ABC() {
            return new ObjectEnumerator(new Object[]{A, B, C});
        }

        protected Enumeration DEF() {
            return new ObjectEnumerator(new Object[]{D, E, F});
        }

        protected Enumeration GHI() {
            return new ObjectEnumerator(new Object[]{G, H, I});
        }

        protected Enumeration ABCDEFGHI() {
            return new ObjectEnumerator(new Object[]{A, B, C, D, E, F, G, H, I});
        }

        protected Enumeration EMPTY() {
            return new EmptyEnumeration();
        }
    }

    public static class TestNonEmpty extends TestCase {
        public void run() throws Exception {
            final Enumeration e = new ZZ(ABC(), DEF(), GHI());
            Enumeration abcdefghi = ABCDEFGHI();

            for (int i = 0; i < 9; i++) {
                assertTrue("nonempty should report more elements at " + i + "/8",
                        e.hasMoreElements());
                assertSame("nonempty should return next element at " + i + "/8",
                        abcdefghi.nextElement(),
                        e.nextElement());
            }

            assertFalse("should report no more elements at end",
                    e.hasMoreElements());
            assertFalse("should be able to call hasMoreElements() multiple times at end",
                    e.hasMoreElements());
            assertThrows("nextElement() should throw NSEE at end",
                    NoSuchElementException.class,
                    new TestBlock() {
                        public void run() {
                            e.nextElement();
                        }
                    });
        }
    }

    public static class TestEmpty extends TestCase {
        public void run() throws Exception {
            final Enumeration e = new AggregateEnumeration() {
                protected Enumeration nextEnumeration() {
                    return null;
                }
            };
            assertFalse("should report no more elements if empty",
                    e.hasMoreElements());
            assertThrows("nextElement() should throw NSEE if empty",
                    NoSuchElementException.class,
                    new TestBlock() {
                        public void run() {
                            e.nextElement();
                        }
                    });
        }
    }

    public static class TestBlindIteration extends TestCase {
        public void run() throws Exception {
            final Enumeration e = new ZZ(ABC(), DEF(), GHI());
            Enumeration abcdefghi = ABCDEFGHI();

            for (int i = 0; i < 9; i++) {
                assertSame("nextElement() should work if hasMoreElements() is never called",
                        abcdefghi.nextElement(),
                        e.nextElement());
            }
        }
    }

    public static class TestOneMember extends TestCase {
        public void run() throws Exception {
            assertEquals(ABC(), new ZZ(ABC()));
        }
    }

    public static class TestEmptyMembers extends TestCase {
        public void run() throws Exception {
            assertEquals("empty members should collapse",
                    ABCDEFGHI(),
                    new ZZ(new Enumeration[]{EMPTY(),
                            ABC(), EMPTY(), DEF(), EMPTY(), GHI(), EMPTY()}));
            assertEquals("repeated empty members should collapse",
                    ABC(),
                    new ZZ(new Enumeration[]{EMPTY(),
                            EMPTY(), EMPTY(), ABC(), EMPTY(), EMPTY(), EMPTY()}));
            assertEquals("should collapse to empty if all members are empty",
                    EMPTY(),
                    new ZZ(EMPTY(), EMPTY(), EMPTY()));
        }
    }

    public static class TestNesting extends TestCase {
        public void run() throws Exception {
            assertEquals("4th order nested enum should work fine",
                    new ObjectEnumerator(new Object[]{A, B, C, D, E, F, G, H, I,
                            A, B, C, D, E, F, G, H, I,
                            A, B, C, D, E, F, G, H, I,
                            A, B, C, D, E, F, G, H, I,
                            A, B, C, D, E, F, G, H, I,
                            A, B, C, D, E, F, G, H, I,
                            A, B, C, D, E, F, G, H, I,
                            A, B, C, D, E, F, G, H, I,
                            A, B, C, D, E, F, G, H, I}),
                    new ZZ(new ZZ(new ZZ(ABC(), DEF(), GHI()),
                            new ZZ(ABC(), DEF(), GHI()),
                            new ZZ(ABC(), DEF(), GHI())),
                            new ZZ(new ZZ(ABC(), DEF(), GHI()),
                                    new ZZ(ABC(), DEF(), GHI()),
                                    new ZZ(ABC(), DEF(), GHI())),
                            new ZZ(new ZZ(ABC(), DEF(), GHI()),
                                    new ZZ(ABC(), DEF(), GHI()),
                                    new ZZ(ABC(), DEF(), GHI()))));
        }
    }
}

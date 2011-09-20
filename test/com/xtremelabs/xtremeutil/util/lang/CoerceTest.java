package com.xtremelabs.xtremeutil.util.lang;

import rimunit.TestCase;

public class CoerceTest {
    public static class TestBooleanCoercion extends TestCase {
        public void run() throws Exception {
            assertFalse("false should coerce to false",
                    Coerce.toBoolean(false));
            assertFalse("null should coerce to false",
                    Coerce.toBoolean(null));
            assertFalse("Boolean.FALSE should coerce to false",
                    Coerce.toBoolean(Boolean.FALSE));
            assertFalse("new Boolean(false) should coerce to false",
                    Coerce.toBoolean(new Boolean(false)));

            assertTrue("a byte should coerce to true",
                    Coerce.toBoolean((byte) 123));
            assertTrue("a char should coerce to true",
                    Coerce.toBoolean('x'));
            assertTrue("a short should coerce to true",
                    Coerce.toBoolean((short) 123));
            assertTrue("an int should coerce to true",
                    Coerce.toBoolean(123));
            assertTrue("a long should coerce to true",
                    Coerce.toBoolean(123L));
            assertTrue("a float should coerce to true",
                    Coerce.toBoolean(1.23F));
            assertTrue("a double should coerce to true",
                    Coerce.toBoolean(1.23D));
            assertTrue("an Object should coerce to true",
                    Coerce.toBoolean(new Object()));
        }
    }

    public static class TestSafeUnbox extends TestCase {
        public void run() throws Exception {
            assertEquals("unboxing primitive type should return primitive value",
                    123,
                    Coerce.toInteger(new Integer(123), 456));

            assertEquals("attempt unbox incorrect type should return default",
                    456L,
                    Coerce.toLong(new Integer(123), 456L));

            assertEquals("attempt to unbox null should return default",
                    'x',
                    Coerce.toChar(null, 'x'));
        }
    }

    public static class TestCoercePrimitiveToString extends TestCase {
        public void run() throws Exception {
            assertEquals("primitive string coercion failed", "true", Coerce.toString(true));
            assertEquals("primitive string coercion failed", "false", Coerce.toString(false));
            assertEquals("primitive string coercion failed", "123", Coerce.toString((byte) 123));
            assertEquals("primitive string coercion failed", "x", Coerce.toString('x'));
            assertEquals("primitive string coercion failed", "456", Coerce.toString((short) 456));
            assertEquals("primitive string coercion failed", "789", Coerce.toString(789));
            assertEquals("primitive string coercion failed", "101112", Coerce.toString(101112L));
            assertEquals("primitive string coercion failed", "1.23", Coerce.toString(1.23F));
            assertEquals("primitive string coercion failed", "4.56", Coerce.toString(4.56));
        }
    }

    public static class TestCoerceObjectToString extends TestCase {
        public void run() throws Exception {
            String aString = "a String";

            try {
                assertSame("coercing a string to a string should return the string",
                        aString,
                        Coerce.toString(aString));
            } catch (Exception ex) {
                fail("coercing a string to a string should not throw a " + ex.getClass().getName());
            }

            try {
                assertEquals("coercing an object to a string should call Object.toString()",
                        "123",
                        Coerce.toString(new Integer(123)));
            } catch (Exception ex) {
                fail("coercing an object to a string should not throw a " + ex.getClass().getName());
            }

            try {
                assertSame("coercing null to a string with a default value should return the default",
                        aString,
                        Coerce.toString(null, aString));
            } catch (Exception ex) {
                fail("coercing null to a string with a default value should not throw a " + ex.getClass().getName());
            }

            try {
                assertEquals("coercing null to a string without a default should return the empty string",
                        "",
                        Coerce.toString(null));
            } catch (Exception ex) {
                fail("coercing null to a string without a default should not throw a " + ex.getClass().getName());
            }
        }
    }

    public static class BaseClass {
    }

    public static class SubClass extends BaseClass {
    }

    public static class DifferentClass {
    }

    public static class TestCoerceToClassWithDefaultValue extends TestCase {

        private BaseClass anObject = new BaseClass();
        private SubClass aSubObject = new SubClass();
        private SubClass anotherSubObject = new SubClass();
        private DifferentClass aDifferentObject = new DifferentClass();

        public void run() throws Exception {

            assertSame("coercing an object to its own class should return the object",
                    anObject,
                    Coerce.to(BaseClass.class, anObject, anotherSubObject));

            assertSame("coercing an object to an ancestor class should return the object",
                    aSubObject,
                    Coerce.to(BaseClass.class, aSubObject, anotherSubObject));

            assertSame("coercing an object to a different class should return the default value",
                    aDifferentObject,
                    Coerce.to(DifferentClass.class, anObject, aDifferentObject));

            assertSame("coercing an object to a subclass should return the default value",
                    aDifferentObject,
                    Coerce.to(SubClass.class, anObject, aDifferentObject));

            assertSame("coercing null to a class should return the default value",
                    aDifferentObject,
                    Coerce.to(DifferentClass.class, null, aDifferentObject));
        }
    }

    public static class TestCoerceToClassWithDefaultInstantiation extends TestCase {

        private static class NotDefaultConstructible {
            private NotDefaultConstructible() {
            }
        }

        private BaseClass anObject = new BaseClass();
        private SubClass aSubObject = new SubClass();

        public void run() throws Exception {

            assertSame("coercing an object to its own class should return the object",
                    anObject,
                    Coerce.to(BaseClass.class, anObject));

            assertSame("coercing an object to an ancestor class should return the object",
                    aSubObject,
                    Coerce.to(BaseClass.class, aSubObject));

            Object newObject;

            newObject = Coerce.to(DifferentClass.class, anObject);
            assertNotNull("coercing an object to a different class should not return null",
                    newObject);
            assertNotSame("coercing an object to a different class should not return the object",
                    anObject,
                    newObject);
//            assertHasSameClassAs("coercing an object to a different class should return a new instance of the class",
//                             DifferentClass.class,
//                             newObject);

            newObject = Coerce.to(SubClass.class, anObject);
            assertNotNull("coercing an object to a subclass should not return null",
                    newObject);
            assertNotSame("coercing an object to a subclass should not return the object",
                    anObject,
                    newObject);
//           assertHasSameClassAs("coercing an object to a subclass should return a new instance of the subclass",
//                             SubClass.class,
//                             newObject);

            newObject = Coerce.to(BaseClass.class, null);
            assertNotNull("coercing null to a class should not return null",
                    newObject);
//            assertHasSameClassAs("coercing null to a class should return a new instance of the class",
//                             BaseClass.class,
//                             newObject);

            assertNull("coercing an object to a different class that is not default constructible should return null",
                    Coerce.to(NotDefaultConstructible.class, anObject));
        }
    }
}

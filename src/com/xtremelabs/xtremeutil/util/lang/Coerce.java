package com.xtremelabs.xtremeutil.util.lang;

/**
 * User: Jedediah Smith
 * Date: Oct 29, 2009
 * Time: 8:40:21 PM
 * <p/>
 * A variety of static methods to safely coerce unknown objects to primitive and almost primitive values.
 */
public class Coerce {

    // Safe Boolean Coercion
    //
    //   if `x` is null, false or Boolean.FALSE, return false, otherwise return true

    public static boolean toBoolean(boolean x) {
        return x;
    }

    public static boolean toBoolean(byte x) {
        return true;
    }

    public static boolean toBoolean(char x) {
        return true;
    }

    public static boolean toBoolean(short x) {
        return true;
    }

    public static boolean toBoolean(int x) {
        return true;
    }

    public static boolean toBoolean(long x) {
        return true;
    }

    public static boolean toBoolean(float x) {
        return true;
    }

    public static boolean toBoolean(double x) {
        return true;
    }

    public static boolean toBoolean(Object x) {
        return Coerce.toBooleanWeak(x);
    }

    public static boolean toBooleanWeak(Object x) {
        return x != null && !Boolean.FALSE.equals(x);
    }

    public static boolean toBooleanStrong(Object x) {
        return x != null && Boolean.TRUE.equals(x);
    }

    // Safe Unboxing (with default value)
    //
    //   If `x` is a boxed instance of the respective primitive, return the primitive value.
    //   Otherwise, return the default value `d`.
    //
    //   TODO: These should probably convert between numeric types when possible
    //   TODO: Implement the non-default versions (which return 0)
    //   TODO: Implement versions that parse from string

    public static byte toByte(Object x, byte d) {
        if (x instanceof Byte) {
            return ((Byte) x).byteValue();
        } else {
            return d;
        }
    }

    public static char toChar(Object x, char d) {
        if (x instanceof Character) {
            return ((Character) x).charValue();
        } else {
            return d;
        }
    }

    public static short toShort(Object x, short d) {
        if (x instanceof Short) {
            return ((Short) x).shortValue();
        } else {
            return d;
        }
    }

    public static int toInteger(Object x, int d) {
        if (x instanceof Integer) {
            return ((Integer) x).intValue();
        } else {
            return d;
        }
    }

    public static long toLong(Object x, long d) {
        if (x instanceof Long) {
            return ((Long) x).longValue();
        } else {
            return d;
        }
    }

    public static float toFloat(Object x, float d) {
        if (x instanceof Float) {
            return ((Float) x).floatValue();
        } else {
            return d;
        }
    }

    public static double toDouble(Object x, double d) {
        if (x instanceof Double) {
            return ((Double) x).doubleValue();
        } else {
            return d;
        }
    }

    // String Coercion
    //
    //   These methods are similar to the String.valueOf series, except an optional default value can
    //   be passed as the second argument. The default value is returned only if the first argument is null.
    //   (this is not possible for the primitive versions but the default parameter is present to make the
    //   interface consistent). If the first argument is null and there is no default, the empty string is
    //   returned. Without the default argument, these methods are guaranteed to always return a String.

    public static String toString(boolean x, String d) {
        return String.valueOf(x);
    }

    public static String toString(byte x, String d) {
        return String.valueOf(x);
    }

    public static String toString(char x, String d) {
        return String.valueOf(x);
    }

    public static String toString(short x, String d) {
        return String.valueOf(x);
    }

    public static String toString(int x, String d) {
        return String.valueOf(x);
    }

    public static String toString(long x, String d) {
        return String.valueOf(x);
    }

    public static String toString(float x, String d) {
        return String.valueOf(x);
    }

    public static String toString(double x, String d) {
        return String.valueOf(x);
    }

    public static String toString(boolean x) {
        return String.valueOf(x);
    }

    public static String toString(byte x) {
        return String.valueOf(x);
    }

    public static String toString(char x) {
        return String.valueOf(x);
    }

    public static String toString(short x) {
        return String.valueOf(x);
    }

    public static String toString(int x) {
        return String.valueOf(x);
    }

    public static String toString(long x) {
        return String.valueOf(x);
    }

    public static String toString(float x) {
        return String.valueOf(x);
    }

    public static String toString(double x) {
        return String.valueOf(x);
    }

    public static String toString(String x, String d) {
        if (x == null) {
            return d;
        } else {
            return x;
        }
    }

    public static String toString(String x) {
        if (x == null) {
            return "";
        } else {
            return x;
        }
    }

    public static String toString(Object x, String d) {
        if (x == null) {
            return d;
        } else {
            return x.toString();
        }
    }

    public static String toString(Object x) {
        if (x == null) {
            return "";
        } else {
            return x.toString();
        }
    }

    // Coerce To Arbitrary Class (with default value)
    //
    //   Return `x` if it is assignment compatible with class `c` (i.e. it is an instance
    //   of `c` or one of its subclasses), otherwise return the default value `d`.

    public static Object to(Class c, Object x, Object d) {
        if (x != null && c.isAssignableFrom(x.getClass())) {
            return x;
        } else {
            return d;
        }
    }

    // Coerce To Arbitrary Class (or create new instance)
    //
    //   Return `x` if it is assignment compatible with class `c` (i.e. an instance of `c`
    //   or one of its subclasses), otherwise return a new default-constructed instance of `c`.
    //   In the latter case, if `c` cannot be default-constructed (because of an
    //   InstantiationException or an IllegalAccessException), return null.

    public static Object to(Class c, Object x) {
        if (x != null && c.isAssignableFrom(x.getClass())) {
            return x;
        } else {
            try {
                return c.newInstance();
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }
    }
}

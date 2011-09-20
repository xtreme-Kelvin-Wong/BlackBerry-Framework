package com.xtremelabs.xtremeutil.util.lang;

import net.rim.device.api.util.Arrays;

public class ArrayUtils {
    public static boolean contains(int[] a, int i) {
        for (int j = 0; j < a.length; j++) {
            if (a[j] == i) {
                return true;
            }
        }
        return false;
    }

    public static Object[] concat(Object[] a1, Object[] a2) {
        Object[] x = new Object[a1.length + a2.length];
        System.arraycopy(a1, 0, x, 0, a1.length);
        System.arraycopy(a2, 0, x, a1.length, a2.length);
        return x;
    }

    // For some reason, Arrays.equals is not implemented for float and double
    public static boolean equals(float[] a, float[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(double[] a, double[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    // Element-by-element equality test for ANY two arrays.
    // Proudly crafted by Jed's Lovely Functions Inc. 
    public static boolean equals(Object a, Object b) {
        return (a instanceof byte[] && b instanceof byte[] && Arrays.equals((byte[]) a, (byte[]) b)) ||
                (a instanceof char[] && b instanceof char[] && Arrays.equals((char[]) a, (char[]) b)) ||
                (a instanceof short[] && b instanceof short[] && Arrays.equals((short[]) a, (short[]) b)) ||
                (a instanceof int[] && b instanceof int[] && Arrays.equals((int[]) a, (int[]) b)) ||
                (a instanceof long[] && b instanceof long[] && Arrays.equals((long[]) a, (long[]) b)) ||
                (a instanceof float[] && b instanceof float[] && equals((float[]) a, (float[]) b)) ||
                (a instanceof double[] && b instanceof double[] && equals((double[]) a, (double[]) b)) ||
                (a instanceof Object[] && b instanceof Object[] && Arrays.equals((Object[]) a, (Object[]) b));
    }


    public static String stringValue(int[] a) {
        if (a == null) {
            return null;
        }
        if (a.length < 1) {
            return "int[]";
        }

        StringBuffer buffer = new StringBuffer("int[");
        for (int i = 0; i < a.length; i++) {
            buffer.append(a[i]).append(",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append("]");

        return buffer.toString();
    }

    public static String stringValue(char[] a) {
        if (a == null) {
            return null;
        }
        if (a.length < 1) {
            return "char[]";
        }

        StringBuffer buffer = new StringBuffer("char[");
        for (int i = 0; i < a.length; i++) {
            buffer.append(a[i]).append(",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append("]");

        return buffer.toString();
    }

    public static String stringValue(Object[] a) {
        if (a == null) {
            return null;
        }

// TODO: add the name of the device to the        
//        String className = a.getClass().getName();
        if (a.length < 1) {
            return "[]";
        }

        StringBuffer buffer = new StringBuffer(a.getClass().getName()).append("[");
        for (int i = 0; i < a.length; i++) {
            buffer.append(a[i]).append(", ");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append("]");

        return buffer.toString();
    }
}

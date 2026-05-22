package com.neocoretechs.relatrix.client.json.util;

import java.util.Arrays;

public final class ByteUtils {
    private ByteUtils() {}

    public static int unsignedCompare(byte[] a, byte[] b) {
        if (a == b) return 0;
        if (a == null) return (b == null) ? 0 : -1;
        if (b == null) return 1;
        int la = a.length, lb = b.length;
        int min = Math.min(la, lb);
        int res = 0;
        for (int i = 0; i < min; i++) {
            res = Byte.compareUnsigned(a[i],b[i]);
            if(res != 0)
            	return res;
        }
        return res;
    }
    public static boolean equalsBoolean(byte[] a, byte[] b) {
        return Arrays.equals(a, b);
    }
    public static int unsignedCompareLong(Long a, Long b) {
    	return Long.compareUnsigned(a, b);
    }
}


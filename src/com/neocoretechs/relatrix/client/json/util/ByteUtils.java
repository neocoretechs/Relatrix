package com.neocoretechs.relatrix.client.json.util;

public final class ByteUtils {
    private ByteUtils() {}

    public static int unsignedCompare(byte[] a, byte[] b) {
        if (a == b) return 0;
        if (a == null) return (b == null) ? 0 : -1;
        if (b == null) return 1;
        int la = a.length, lb = b.length;
        int min = Math.min(la, lb);
        for (int i = 0; i < min; i++) {
            int va = a[i] & 0xFF;
            int vb = b[i] & 0xFF;
            if (va != vb) return Integer.compare(va, vb);
        }
        return Integer.compare(la, lb);
    }
}


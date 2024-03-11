package com.anity.healthpunch.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Md5 {
    public Md5() {
    }
    public static String getMD5(String x) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(x.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder();
            for (byte value : b) {
                i = value;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            x = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if ( x.length()> 5) {
            x = x.substring(0, 5) + "a" + x.substring(5);
        }
        if (x.length() > 10) {
            x = x.substring(0, 10) + "b" + x.substring(10);
        }
        return x.substring(0, x.length()-2 );
    }
}

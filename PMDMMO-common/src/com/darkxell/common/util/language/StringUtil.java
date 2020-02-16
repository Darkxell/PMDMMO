package com.darkxell.common.util.language;

public class StringUtil {

    public static boolean equals(String s1, String s2) {
        return s1 == s2 || (s1 != null && s1.equals(s2));
    }

    public static String insert(String string, String insert, int pos) {
        return string.substring(0, pos) + insert + string.substring(pos);
    }

    public static String remove(String string, int pos, int length) {
        return string.substring(0, pos) + string.substring(pos + length, string.length());
    }

}

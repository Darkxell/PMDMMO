package com.darkxell.common.util.language;

public class StringUtil {

    public static String insert(String string, String insert, int pos) {
        return string.substring(0, pos) + insert + string.substring(pos);
    }

    public static String remove(String string, int pos, int length) {
        return string.substring(0, pos) + string.substring(pos + length, string.length());
    }

}

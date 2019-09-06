package com.darkxell.common.testutils;

import java.util.ArrayList;

public class AssertUtils {

    /** Returns true if the input array contains an object of the input class. */
    public static boolean containsObjectOfClass(ArrayList<?> result, Class<?> clazz) {
        for (Object e : result)
            if (clazz.isInstance(e))
                return true;
        return false;
    }

    /** Returns true if the input object extends the input class. */
    public static boolean isOfClass(Object received, Class<?> clazz) {
        return clazz.isInstance(received);
    }

}

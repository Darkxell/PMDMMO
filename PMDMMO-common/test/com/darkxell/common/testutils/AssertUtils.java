package com.darkxell.common.testutils;

import java.util.ArrayList;

import com.darkxell.common.event.Event;

public class AssertUtils {

    /** Returns true if the input event array contains an event of the input class. */
    public static boolean containsEvent(ArrayList<Event> result, Class<? extends Event> eventClass) {
        for (Event e : result)
            if (e.getClass() == eventClass)
                return true;
        return false;
    }

}

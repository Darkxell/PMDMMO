package com.darkxell.common.event;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.Communicable.JsonReadingException;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.JsonObject;

public final class EventCommunication {

    private static final String classBase;
    static {
        String base = DungeonEvent.class.getName();
        classBase = base.substring(0, base.indexOf("DungeonEvent"));
    }

    /**
     * @param  event - A DungeonEvent.
     * @return       The Json value of the input DungeonEvent, with the event Class name. Class name is required to be
     *               communicated to load the correct Class when reading.
     */
    public static JsonObject prepareToSend(DungeonEvent event) {
        if (!(event instanceof Communicable))
            return null;
        JsonObject object = ((Communicable) event).toJson();
        String className = event.getClass().getName().substring(classBase.length());
        object.add("event", className);
        return object;
    }

    /**
     * @param  json  - The Json data to read.
     * @param  floor - The Floor the Event should trigger on.
     * @return       A DungeonEvent loaded from the input Json data. May return null if the Json data is incomplete or
     *               flawed.
     */
    public static DungeonEvent read(JsonObject json, Floor floor) {
        if (json.get("event") == null) {
            Logger.e("Event type is missing.");
            return null;
        }
        if (!json.get("event").isString()) {
            Logger.e("Wrong value for Event type: " + json.get("event"));
            return null;
        }

        String className = json.getString("event", null);
        Class<?> c = null;
        try {
            c = Class.forName(classBase + className);
        } catch (ClassNotFoundException e) {
            Logger.e("No Event class with name: " + className);
            return null;
        }

        DungeonEvent e = null;
        try {
            e = (DungeonEvent) c.getConstructor(Floor.class).newInstance(floor);

        } catch (Exception ex) {
            Logger.e("Error while instanciating event class: " + ex.getMessage());
            return null;
        }

        if (!(e instanceof Communicable)) {
            Logger.e("DungeonEvent " + className + " doesn't implement Communicable! Can't read the Json object!");
            return null;
        }

        try {
            ((Communicable) e).read(json);
        } catch (JsonReadingException ex) {
            Logger.e("Error reading Json for " + className.substring(className.lastIndexOf('.') + 1) + ": "
                    + ex.getMessage());
            return null;
        }

        return e;
    }

    private EventCommunication() {
    }

}

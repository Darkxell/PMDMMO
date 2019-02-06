package com.darkxell.common.util;

import com.eclipsesource.json.JsonObject;

/**
 * Any objects that would be sent between clients and server can implement this interface. <br/>
 * Implements generic methods for conversion from/to Json.
 */
public interface Communicable {

    public static class JsonReadingException extends Exception {
        private static final long serialVersionUID = 2731314154564321754L;

        public JsonReadingException(String message) {
            super(message);
        }
    }

    /**
     * Reads the Json object and sets this Object's data to the read data.
     *
     * @param value - The value to use.
     */
    public void read(JsonObject value) throws JsonReadingException;

    /**
     * Translates this Object to a Json object.
     *
     * @return The created Json object.
     */
    public JsonObject toJson();

}

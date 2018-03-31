package com.darkxell.common.util;

import com.eclipsesource.json.JsonObject;

/** Any objects that would be sent between clients and server should implement this interface. <br/>
 * Implements generic methods for conversion for/to Json. */
public interface Communicable
{

	/** Reads the Json object and sets this Object's data to the read data.
	 * 
	 * @param value - The value to use. */
	public void read(JsonObject value);

	/** Translates this Object to a Json object.
	 * 
	 * @return The created Json object. */
	public JsonObject toJson();

}

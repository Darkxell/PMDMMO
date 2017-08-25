package com.darkxell.common.util;

import java.util.ArrayList;

import org.jdom2.Element;

/** Utility methods for XML. */
public final class XMLUtils
{

	/** Reads an array of Integers in an XML element and returns it. <br />
	 * e.g. 1,2,4,5,-1 */
	public static ArrayList<Integer> readIntArray(Element element)
	{
		ArrayList<Integer> array = new ArrayList<Integer>();
		if (element != null)
		{
			String value = element.getText();
			if (value != null) for (String floor : value.split(","))
				array.add(Integer.parseInt(floor));
		}
		return array;
	}

	/** Exports an array of Integers to an XML element and returns it. <br />
	 * e.g. 1,2,4,5,-1
	 * 
	 * @param id - The Element name. */
	public static Element toXML(String id, ArrayList<Integer> array)
	{
		String value = "";
		for (Integer floor : array)
			if (value.equals("")) value += floor;
			else value += "," + floor;
		return new Element(id).setText(value);
	}

}

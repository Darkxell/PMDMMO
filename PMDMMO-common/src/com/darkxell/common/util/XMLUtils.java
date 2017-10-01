package com.darkxell.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/** Utility methods for XML. */
public final class XMLUtils
{

	public static boolean getAttribute(Element element, String id, boolean defaultValue)
	{
		return element.getAttribute(id) == null ? defaultValue : Boolean.parseBoolean(element.getAttributeValue(id));
	}

	public static byte getAttribute(Element element, String id, byte defaultValue)
	{
		return element.getAttribute(id) == null ? defaultValue : Byte.parseByte(element.getAttributeValue(id));
	}

	public static char getAttribute(Element element, String id, char defaultValue)
	{
		return element.getAttribute(id) == null ? defaultValue : element.getAttributeValue(id).charAt(0);
	}

	public static double getAttribute(Element element, String id, double defaultValue)
	{
		return element.getAttribute(id) == null ? defaultValue : Double.parseDouble(element.getAttributeValue(id));
	}

	public static float getAttribute(Element element, String id, float defaultValue)
	{
		return element.getAttribute(id) == null ? defaultValue : Float.parseFloat(element.getAttributeValue(id));
	}

	public static int getAttribute(Element element, String id, int defaultValue)
	{
		return element.getAttribute(id) == null ? defaultValue : Integer.parseInt(element.getAttributeValue(id));
	}

	public static long getAttribute(Element element, String id, long defaultValue)
	{
		return element.getAttribute(id) == null ? defaultValue : Long.parseLong(element.getAttributeValue(id));
	}

	public static short getAttribute(Element element, String id, short defaultValue)
	{
		return element.getAttribute(id) == null ? defaultValue : Short.parseShort(element.getAttributeValue(id));
	}

	public static String getAttribute(Element element, String id, String defaultValue)
	{
		return element.getAttribute(id) == null ? defaultValue : element.getAttributeValue(id);
	}

	/** Reads an XML File and returns its root Element. */
	public static Element readFile(File file)
	{
		if (file.exists()) try
		{
			return new SAXBuilder().build(file).getRootElement();
		} catch (JDOMException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/** Reads an array of Integers in an XML element and returns it. <br />
	 * e.g. 1,2,4,5,-1 */
	public static int[] readIntArray(Element element)
	{
		ArrayList<Integer> list = readIntArrayAsList(element);
		int[] array = new int[list.size()];
		for (int i = 0; i < array.length; ++i)
			array[i] = list.get(i);
		return array;
	}

	/** Reads an array of Integers in an XML element and returns it. <br />
	 * e.g. 1,2,4,5,-1 */
	public static ArrayList<Integer> readIntArrayAsList(Element element)
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

	public static short[] readShortArray(String value)
	{
		String[] values = value.split(",");
		short[] array = new short[values.length];
		for (int i = 0; i < array.length; ++i)
			array[i] = Short.parseShort(values[i]);
		return array;
	}

	/** Saves the input XML Element into the input file. */
	public static void saveFile(File file, Element element)
	{
		try
		{
			if (!file.exists()) file.createNewFile();
			new XMLOutputter(Format.getPrettyFormat()).output(new Document(element), new FileOutputStream(file));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void setAttribute(Element element, String id, boolean value, boolean defaultValue)
	{
		if (value != defaultValue) element.setAttribute(id, Boolean.toString(value));
	}

	public static void setAttribute(Element element, String id, byte value, byte defaultValue)
	{
		if (value != defaultValue) element.setAttribute(id, Byte.toString(value));
	}

	public static void setAttribute(Element element, String id, char value, char defaultValue)
	{
		if (value != defaultValue) element.setAttribute(id, Character.toString(value));
	}

	public static void setAttribute(Element element, String id, double value, double defaultValue)
	{
		if (value != defaultValue) element.setAttribute(id, Double.toString(value));
	}

	public static void setAttribute(Element element, String id, float value, float defaultValue)
	{
		if (value != defaultValue) element.setAttribute(id, Float.toString(value));
	}

	public static void setAttribute(Element element, String id, int value, int defaultValue)
	{
		if (value != defaultValue) element.setAttribute(id, Integer.toString(value));
	}

	public static void setAttribute(Element element, String id, long value, long defaultValue)
	{
		if (value != defaultValue) element.setAttribute(id, Long.toString(value));
	}

	public static void setAttribute(Element element, String id, short value, short defaultValue)
	{
		if (value != defaultValue) element.setAttribute(id, Short.toString(value));
	}

	public static void setAttribute(Element element, String id, String value, String defaultValue)
	{
		if (!value.equals(defaultValue)) element.setAttribute(id, value);
	}

	public static String toXML(short[] array)
	{
		String value = "";
		for (short floor : array)
			if (value.equals("")) value += floor;
			else value += "," + floor;
		return value;
	}

	/** Exports an array of Integers to an XML element and returns it. <br />
	 * e.g. 1,2,4,5,-1
	 * 
	 * @param id - The Element name. */
	public static Element toXML(String id, int[] array)
	{
		String value = "";
		for (int floor : array)
			if (value.equals("")) value += floor;
			else value += "," + floor;
		return new Element(id).setText(value);
	}

	/** Exports an array of Integers to an XML element and returns it. <br />
	 * e.g. 1,2,4,5,-1
	 * 
	 * @param id - The Element name. */
	public static Element toXML(String id, Iterable<Integer> array)
	{
		String value = "";
		for (Integer floor : array)
			if (value.equals("")) value += floor;
			else value += "," + floor;
		return new Element(id).setText(value);
	}

}

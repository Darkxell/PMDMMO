package com.darkxell.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

	private static String find(Element element, String id)
	{
		return element.getAttribute(id) != null ? element.getAttributeValue(id)
				: element.getAttribute(id, element.getNamespace()) != null ? element.getAttributeValue(id, element.getNamespace()) : null;
	}

	public static boolean getAttribute(Element element, String id, boolean defaultValue)
	{
		String value = find(element, id);
		return value == null ? defaultValue : Boolean.parseBoolean(value);
	}

	public static byte getAttribute(Element element, String id, byte defaultValue)
	{
		String value = find(element, id);
		return value == null ? defaultValue : Byte.parseByte(value);
	}

	public static char getAttribute(Element element, String id, char defaultValue)
	{
		String value = find(element, id);
		return value == null ? defaultValue : value.charAt(0);
	}

	public static double getAttribute(Element element, String id, double defaultValue)
	{
		String value = find(element, id);
		return value == null ? defaultValue : Double.parseDouble(value);
	}

	public static float getAttribute(Element element, String id, float defaultValue)
	{
		String value = find(element, id);
		return value == null ? defaultValue : Float.parseFloat(value);
	}

	public static int getAttribute(Element element, String id, int defaultValue)
	{
		String value = find(element, id);
		return value == null ? defaultValue : Double.valueOf(value).intValue();
	}

	public static long getAttribute(Element element, String id, long defaultValue)
	{
		String value = find(element, id);
		return value == null ? defaultValue : Long.parseLong(value);
	}

	public static short getAttribute(Element element, String id, short defaultValue)
	{
		String value = find(element, id);
		return value == null ? defaultValue : Short.parseShort(value);
	}

	public static String getAttribute(Element element, String id, String defaultValue)
	{
		String value = find(element, id);
		return value == null ? defaultValue : value;
	}

	/** Reads an XML File and returns its root Element. */
	public static Element read(InputStream input)
	{
		if (input != null) try
		{
			return new SAXBuilder().build(input).getRootElement();
		} catch (JDOMException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/** Reads a double array of Integers in an XML element and returns it. <br />
	 * e.g. 1,2,4,5,-1;5,1,3 */
	public static int[][] readDoubleIntArray(Element element)
	{
		String[] rows = element.getValue().split(";");
		int[][] array = new int[rows.length][];
		for (int i = 0; i < array.length; ++i)
		{
			String[] cells = rows[i].split(",");
			array[i] = new int[cells.length];
			for (int j = 0; j < cells.length; ++j)
				array[i][j] = Integer.parseInt(cells[j]);
		}
		return array;
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
			if (value != null && !value.equals("")) for (String floor : value.split(","))
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
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream s = new FileOutputStream(file);
			outputter.output(new Document(element), s);
			s.close();
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
		if (value != null && !value.equals(defaultValue)) element.setAttribute(id, value);
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

	/** Exports a double array of Integers to an XML element and returns it. <br />
	 * e.g. 1,2,4,5,-1;5,1,3
	 * 
	 * @param id - The Element name. */
	public static Element toXML(String id, int[][] array)
	{
		String value = "";
		for (int[] row : array)
		{
			if (!value.equals("")) value += ";";
			for (int cell : row)
				if (value.equals("") || value.endsWith(";")) value += cell;
				else value += "," + cell;
		}
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

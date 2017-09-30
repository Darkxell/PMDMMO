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

	/** Exports an array of Integers to an XML element and returns it. <br />
	 * e.g. 1,2,4,5,-1
	 * 
	 * @param id - The Element name. */
	public static Element toXML(String id, int[] array)
	{
		String value = "";
		for (Integer floor : array)
			if (value.equals("")) value += floor;
			else value += "," + floor;
		return new Element(id).setText(value);
	}

}

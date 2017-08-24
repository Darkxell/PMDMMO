package com.darkxell.common.pokemon;

import org.jdom2.Element;

public class Evolution
{
	/** Evolution methods.<br />
	 * <ul>
	 * <li>LEVEL = 0</li>
	 * <li>ITEM = 1</li>
	 * <li>IQ = 2</li>
	 * </ul> */
	public static final byte LEVEL = 0, ITEM = 1, IQ = 2;

	/** @return An Evolution created from the input XML data. */
	public static Evolution fromXML(Element root)
	{
		return new Evolution(Integer.parseInt(root.getAttributeValue("pokemon")), Integer.parseInt(root.getAttributeValue("form")), Byte.parseByte(root
				.getAttributeValue("method")), Integer.parseInt(root.getAttributeValue("value")));
	}

	/** How this Evolution occurs. See {@link Evolution#LEVEL}. */
	public final byte method;
	/** The ID of the species this Evolution leads to. */
	public final int species;
	/** The ID of the species form this Evolution leads to. */
	public final int speciesForm;
	/** If method is LEVEL, this is the minimum level of the Pok�mon.<br />
	 * If method is ITEM, this is the ID of the Item to be used on.<br />
	 * If method is IQ, this is the minimum IQ of the Pok�mon. */
	public final int value;

	public Evolution(Element xml)
	{
		this.species = Integer.parseInt(xml.getAttributeValue("pokemon"));
		this.speciesForm = Integer.parseInt(xml.getAttributeValue("form"));
		this.method = Byte.parseByte(xml.getAttributeValue("method"));
		this.value = Integer.parseInt(xml.getAttributeValue("value"));
	}

	public Evolution(int species, int speciesForm, byte method, int value)
	{
		this.species = species;
		this.speciesForm = speciesForm;
		this.method = method;
		this.value = value;
	}

	public Element toXML()
	{
		return new Element("e").setAttribute("pokemon", Integer.toString(this.species)).setAttribute("form", Integer.toString(this.speciesForm))
				.setAttribute("method", Integer.toString(this.method)).setAttribute("value", Integer.toString(this.value));
	}

}

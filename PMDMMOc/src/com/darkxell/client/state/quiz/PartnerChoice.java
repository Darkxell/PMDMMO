package com.darkxell.client.state.quiz;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;

public class PartnerChoice
{

	public final int id;
	/** This Partner will be prevented from appearing if one of these IDs is the player's Pokemon. */
	private ArrayList<Integer> invalidators;

	public PartnerChoice(Element xml)
	{
		this.id = XMLUtils.getAttribute(xml, "pokemon", 1);
		this.invalidators = new ArrayList<>();
		for (Element invalidates : xml.getChildren("invalidates", xml.getNamespace()))
			this.invalidators.add(Integer.parseInt(invalidates.getText()));
	}

	/** @return True if this Partner is valid for the input leader choice. */
	public boolean isValid(int choice)
	{
		return this.id != choice && !this.invalidators.contains(choice);
	}

}

package com.darkxell.common.pokemon;

import org.jdom2.Element;

import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;

public class LearnedMove
{

	/** This move's ID. */
	public final int id;
	/** True if this move is Linked. */
	private boolean isLinked;
	/** This move's maximum Power Points. */
	private int maxPP;
	/** This move's current Power Points. */
	private int pp;
	/** The position of this move in the Pokémon's move set. */
	private int slot;

	public LearnedMove(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.maxPP = xml.getAttribute("pp-max") == null ? this.move().pp : Integer.parseInt(xml.getAttributeValue("pp-max"));
		this.pp = xml.getAttribute("pp") == null ? this.maxPP : Integer.parseInt(xml.getAttributeValue("pp"));
		this.isLinked = xml.getAttribute("linked") != null;
		this.slot = Integer.parseInt(xml.getAttributeValue("slot"));
	}

	public LearnedMove(int id)
	{
		this.id = id;
		this.pp = this.maxPP = this.move().pp;
		this.isLinked = false;
		this.slot = 4;
	}

	public int getMaxPP()
	{
		return this.maxPP;
	}

	public int getPP()
	{
		return this.pp;
	}

	public int getSlot()
	{
		return this.slot;
	}

	public boolean isLinked()
	{
		return this.isLinked;
	}

	public Move move()
	{
		return MoveRegistry.find(this.id);
	}

	public void setLinked(boolean isLinked)
	{
		this.isLinked = isLinked;
	}

	public void setMaxPP(int maxPP)
	{
		this.maxPP = maxPP;
	}

	public void setPP(int pp)
	{
		this.pp = pp;
		if (this.pp < 0) this.pp = 0;
		if (this.pp > this.getMaxPP()) this.pp = this.getMaxPP();
	}

	public void setSlot(int slot)
	{
		this.slot = slot;
	}

	public Element toXML()
	{
		Element root = new Element("move");
		root.setAttribute("id", Integer.toString(this.id));
		if (this.maxPP != this.move().pp) root.setAttribute("pp-max", Integer.toString(this.maxPP));
		if (this.pp != this.maxPP) root.setAttribute("pp", Integer.toString(this.pp));
		if (this.isLinked) root.setAttribute("linked", "true");
		root.setAttribute("slot", Integer.toString(this.slot));
		return root;
	}

}

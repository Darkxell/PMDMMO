package com.darkxell.common.pokemon;

import org.jdom2.Element;

import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.util.XMLUtils;

public class LearnedMove
{

	/** This move's ID. */
	public final int id;
	/** True if this Move is enabled (for allies). */
	public boolean isEnabled = true;
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
		this.maxPP = XMLUtils.getAttribute(xml, "pp-max", this.move().pp);
		this.pp = XMLUtils.getAttribute(xml, "pp", this.maxPP);
		this.isLinked = XMLUtils.getAttribute(xml, "linked", false);
		this.slot = Integer.parseInt(xml.getAttributeValue("slot"));
		this.isEnabled = XMLUtils.getAttribute(xml, "enabled", true);
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
		XMLUtils.setAttribute(root, "pp-max", this.maxPP, this.move().pp);
		XMLUtils.setAttribute(root, "pp", this.pp, this.move().pp);
		XMLUtils.setAttribute(root, "linked", this.isLinked, false);
		root.setAttribute("slot", Integer.toString(this.slot));
		XMLUtils.setAttribute(root, "enabled", this.isEnabled, true);
		return root;
	}

}

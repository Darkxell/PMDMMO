package com.darkxell.common.pokemon;

import org.jdom2.Element;

import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.XMLUtils;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class LearnedMove implements Communicable
{

	/** This move's ID. */
	private int id;
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

	public LearnedMove()
	{
		this(-1);
	}

	public LearnedMove(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.maxPP = XMLUtils.getAttribute(xml, "pp-max", this.move().pp);
		this.pp = XMLUtils.getAttribute(xml, "pp", this.maxPP);
		this.isLinked = XMLUtils.getAttribute(xml, "linked", false);
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

	@Override
	public void read(JsonObject value)
	{
		this.id = value.getInt("id", -1);
		this.maxPP = value.getInt("pp-max", this.move().pp);
		this.pp = value.getInt("pp", this.maxPP);
		this.isLinked = value.getBoolean("linked", false);
		this.isEnabled = value.getBoolean("enabled", true);
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

	@Override
	public JsonObject toJson()
	{
		JsonObject root = Json.object();
		root.add("id", this.id);
		if (this.maxPP != this.move().pp) root.add("pp-max", this.maxPP);
		if (this.pp != this.maxPP) root.add("pp", this.pp);
		if (!this.isLinked) root.add("linked", this.isLinked);
		if (!this.isEnabled) root.add("enabled", this.isEnabled);
		return root;
	}

	public Element toXML()
	{
		Element root = new Element("move");
		root.setAttribute("id", Integer.toString(this.id));
		XMLUtils.setAttribute(root, "pp-max", this.maxPP, this.move().pp);
		XMLUtils.setAttribute(root, "pp", this.pp, this.move().pp);
		XMLUtils.setAttribute(root, "linked", this.isLinked, false);
		XMLUtils.setAttribute(root, "enabled", this.isEnabled, true);
		return root;
	}

}

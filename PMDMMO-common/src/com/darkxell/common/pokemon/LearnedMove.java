package com.darkxell.common.pokemon;

import org.jdom2.Element;

import com.darkxell.common.dbobject.DBLearnedmove;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.util.XMLUtils;

public class LearnedMove
{
	private DBLearnedmove data;

	private Move move;

	/** This move's current Power Points. */
	private int pp;

	public LearnedMove(DBLearnedmove data)
	{
		this.setData(data);
	}

	public LearnedMove(int moveid)
	{
		this(new DBLearnedmove(0, 4, moveid, 1, true, false, 0));
	}

	public int getAddedLevel()
	{
		return this.data.addedlevel;
	}

	public DBLearnedmove getData()
	{
		return this.data;
	}

	public boolean isEnabled()
	{
		return this.data.isenabled;
	}

	public boolean isLinked()
	{
		return this.data.islinked;
	}

	public int maxPP()
	{
		return this.data.ppmax;
	}

	public Move move()
	{
		return this.move;
	}

	public int moveId()
	{
		return this.data.moveid;
	}

	public int pp()
	{
		return this.pp;
	}

	public void setAddedLevel(int addedLevel)
	{
		this.data.addedlevel = addedLevel;
	}

	public void setData(DBLearnedmove data)
	{
		this.data = data;
		this.move = MoveRegistry.find(this.data.moveid);
		if (this.pp >= this.data.ppmax) this.pp = this.data.ppmax;
	}

	public void setEnabled(boolean isEnabled)
	{
		this.data.isenabled = isEnabled;
	}

	public void setLinked(boolean isLinked)
	{
		this.data.islinked = isLinked;
	}

	public void setMaxPP(int maxPP)
	{
		this.data.ppmax = maxPP;
	}

	public void setPP(int pp)
	{
		this.pp = pp;
		if (this.pp < 0) this.pp = 0;
		if (this.pp > this.maxPP()) this.pp = this.maxPP();
	}

	public void setSlot(int slot)
	{
		this.data.slot = slot;
	}

	public int slot()
	{
		return this.data.slot;
	}

	public Element toXML()
	{
		Element root = new Element("move");
		root.setAttribute("id", Integer.toString(this.moveId()));
		XMLUtils.setAttribute(root, "pp-max", this.maxPP(), this.move().pp);
		XMLUtils.setAttribute(root, "pp", this.pp, this.maxPP());
		XMLUtils.setAttribute(root, "linked", this.isLinked(), false);
		XMLUtils.setAttribute(root, "enabled", this.isEnabled(), true);
		return root;
	}

}

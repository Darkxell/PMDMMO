package com.darkxell.common.item;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.BellyChangedEvent;
import com.darkxell.common.event.pokemon.BellySizeChangedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

/** An Item that restores belly when eaten. */
public class ItemFood extends Item
{

	/** The increase of belly size given by this Food when eaten. */
	public final int belly;
	/** The increase of belly size given by this Food when eaten while the belly is full. */
	public final int bellyIfFull;
	/** The amount of food given by this Food when eaten. */
	public final int food;

	public ItemFood(Element xml)
	{
		super(xml);
		this.food = XMLUtils.getAttribute(xml, "food", 0);
		this.bellyIfFull = XMLUtils.getAttribute(xml, "full", 0);
		this.belly = XMLUtils.getAttribute(xml, "belly", 0);
	}

	public ItemFood(int id, int price, int sell, int sprite, boolean isStackable, int food, int bellyIfFull, int belly)
	{
		super(id, price, sell, sprite, isStackable);
		this.food = food;
		this.bellyIfFull = bellyIfFull;
		this.belly = belly;
	}

	public ItemCategory category()
	{
		return ItemCategory.FOOD;
	}

	@Override
	protected String getUseID()
	{
		return "item.eaten";
	}

	@Override
	public Message getUseName()
	{
		return new Message("item.eat");
	}

	public Element toXML()
	{
		Element root = super.toXML();
		XMLUtils.setAttribute(root, "food", this.food, 0);
		XMLUtils.setAttribute(root, "full", this.bellyIfFull, 0);
		XMLUtils.setAttribute(root, "belly", this.belly, 0);
		return root;
	}

	@Override
	public ArrayList<DungeonEvent> use(Floor floor, DungeonPokemon pokemon, DungeonPokemon target)
	{
		ArrayList<DungeonEvent> events = super.use(floor, pokemon, target);

		int increase = this.belly;
		if (target.getBelly() < target.getBellySize()) events.add(new BellyChangedEvent(floor, target, this.belly));
		else increase += this.bellyIfFull;

		if (increase != 0) events.add(new BellySizeChangedEvent(floor, target, increase));

		return events;
	}

	@Override
	public boolean usedOnTeamMember()
	{
		return true;
	}

}

package com.darkxell.common.item;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.IncreasedIQEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

/** A Gummi restores belly, increases stats and IQ, depending on the Pokémon's type. */
public class ItemGummi extends Item
{

	/** The type of the gummy. */
	public final PokemonType type;

	public ItemGummi(Element xml)
	{
		super(xml);
		this.type = PokemonType.valueOf(XMLUtils.getAttribute(xml, "g-type", "Normal"));
	}

	public ItemGummi(int id, int price, int sell, int sprite, boolean isStackable, PokemonType type)
	{
		super(id, price, sell, sprite, isStackable);
		this.type = type;
	}

	public ItemCategory category()
	{
		return ItemCategory.GUMMIS;
	}

	@Override
	public int getSpriteID()
	{
		return 32 + this.type.id;
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

	private int iqIncrease(DungeonPokemon pokemon)
	{
		return iqIncrease(pokemon.species().type1) + iqIncrease(pokemon.species().type2);
	}

	private int iqIncrease(PokemonType type)
	{
		if (type == null) return 0;
		if (type == this.type) return 7;
		double effectiveness = this.type.effectivenessOn(type);
		if (effectiveness == PokemonType.NO_EFFECT) return 1;
		if (effectiveness == PokemonType.NOT_VERY_EFFECTIVE) return 2;
		if (effectiveness == PokemonType.SUPER_EFFECTIVE) return 4;
		return 3;
	}

	public Element toXML()
	{
		Element root = super.toXML();
		root.setAttribute("g-type", this.type.name());
		return root;
	}

	@Override
	public void use(Floor floor, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events)
	{
		events.add(new IncreasedIQEvent(floor, pokemon, this.iqIncrease(target)));
	}

	@Override
	public boolean usedOnTeamMember()
	{
		return true;
	}

}

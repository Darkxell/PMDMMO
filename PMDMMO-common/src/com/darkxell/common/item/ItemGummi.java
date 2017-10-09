package com.darkxell.common.item;

import org.jdom2.Element;

import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.Message;

/** A Gummi restores belly, increases stats and IQ, depending on the Pokémon's type. */
public class ItemGummi extends Item
{

	/** The type of the gummy. */
	public final short type;

	public ItemGummi(Element xml)
	{
		super(xml);
		this.type = Short.parseShort(xml.getAttributeValue("g-type"));
	}

	public ItemGummi(int id, int price, int sell, int sprite, boolean isStackable, short type)
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
		return 32 + this.type;
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
		root.setAttribute("g-type", Short.toString(this.type));
		return root;
	}

	public PokemonType type()
	{
		return PokemonType.find(this.type);
	}
	
	@Override
	public boolean usedOnTeamMember()
	{
		return true;
	}

}

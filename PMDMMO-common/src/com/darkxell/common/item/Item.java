package com.darkxell.common.item;

import java.util.ArrayList;
import java.util.Comparator;

import org.jdom2.Element;

import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

/** Represents an Item type. */
public class Item
{
	/** Possible actions to be executed on an Item. */
	public static enum ItemAction
	{
		/** Removing the Item as shortcut. */
		DESELECT(7, "item.deselect"),
		/** Moving the Item in the inventory. */
		GET(4, "item.get"),
		/** Giving the Item to an ally. */
		GIVE(1, "item.give"),
		/** Viewing the Item description. */
		INFO(10, "item.info"),
		/** Placing the Item on the ground. */
		PLACE(6, "item.place"),
		/** Setting the Item as shortcut. */
		SET(8, "item.set"),
		/** Swapping the Item for another Item in the Inventory. */
		SWAP(3, "item.swap"),
		/** Swapping the Item for the Item on the ground. */
		SWITCH(5, "item.switch"),
		/** Taking the Item from an ally. */
		TAKE(2, "item.take"),
		/** Throwing the Item. */
		THROW(9, "item.throw"),
		/** Using the Item. */
		USE(0, "item.use");

		public static void sort(ArrayList<ItemAction> actions)
		{
			actions.sort(new Comparator<ItemAction>()
			{

				@Override
				public int compare(ItemAction o1, ItemAction o2)
				{
					return Integer.compare(o1.order, o2.order);
				}
			});
		}

		public final String name;
		public final int order;

		private ItemAction(int order, String name)
		{
			this.order = order;
			this.name = name;
		}

		public Message getName(ItemStack item)
		{
			if (this == USE && item != null) return item.item().getUseName();
			return new Message(this.name);
		}

	}

	public static enum ItemCategory
	{
		BERRIES(4),
		DRINKS(5),
		EQUIPABLE(0),
		EVOLUTIONARY(12),
		FOOD(3),
		GUMMIS(6),
		HMS(11),
		ORBS(9),
		OTHERS(8),
		SEEDS(7),
		THROWABLE(2),
		TMS(10);

		public final int order;

		private ItemCategory(int order)
		{
			this.order = order;
		}
	}

	public static final int POKE = 0, USED_TM = 205;
	public static final String XML_ROOT = "item";

	/** This Item's ID. */
	public final int id;
	/** True if this Item can be stacked. */
	public final boolean isStackable;
	/** This Item's price to buy. */
	public final int price;
	/** This Item's price to sell. */
	public final int sell;
	/** The ID of the Item's sprite. */
	public final int spriteID;

	public Item(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.price = Integer.parseInt(xml.getAttributeValue("price"));
		this.sell = Integer.parseInt(xml.getAttributeValue("sell"));
		this.spriteID = Integer.parseInt(xml.getAttributeValue("sprite"));
		this.isStackable = "true".equals(xml.getAttributeValue("stackable"));
	}

	public Item(int id, int price, int sell, int spriteID, boolean stackable)
	{
		this.id = id;
		this.price = price;
		this.sell = sell;
		this.spriteID = spriteID;
		this.isStackable = stackable;
	}

	public ItemCategory category()
	{
		return ItemCategory.OTHERS;
	}

	public ArrayList<ItemAction> getLegalActions(boolean inDungeon)
	{
		ArrayList<ItemAction> actions = new ArrayList<Item.ItemAction>();
		if (inDungeon) actions.add(ItemAction.USE);
		if (inDungeon) actions.add(ItemAction.GIVE);
		if (inDungeon) actions.add(ItemAction.THROW);
		actions.add(ItemAction.INFO);
		return actions;
	}

	/** @param pokemon - The Pokémon using the Item.
	 * @return The message to display when using this Item. */
	public Message getUseMessage(DungeonPokemon pokemon)
	{
		return new Message("item.used").addReplacement("<pokemon>", pokemon.pokemon.getNickname()).addReplacement("<item>", this.name());
	}

	/** @return The name of the "Use" option for this Item. */
	public Message getUseName()
	{
		return new Message("item.use");
	}

	public Message name()
	{
		return new Message("item." + this.id);
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("type", this.getClass().getName().substring(Item.class.getName().length()));
		root.setAttribute("price", Integer.toString(this.price));
		root.setAttribute("sell", Integer.toString(this.sell));
		root.setAttribute("sprite", Integer.toString(this.spriteID));
		if (this.isStackable) root.setAttribute("stackable", "true");
		return root;
	}

	/** Called when this Item is used.
	 * 
	 * @param pokemon - The Pokémon using the Item.
	 * @return The messages that were created while using the Item. */
	public ArrayList<Message> use(DungeonPokemon pokemon)
	{
		ArrayList<Message> messages = new ArrayList<Message>();
		messages.add(new Message("item.no_effect"));
		return messages;
	}

}

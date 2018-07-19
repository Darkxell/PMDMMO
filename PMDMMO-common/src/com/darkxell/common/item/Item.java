package com.darkxell.common.item;

import java.util.ArrayList;
import java.util.Comparator;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.item.ItemSelectionEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

/** Represents an Item type. */
public class Item
{
	/** Possible actions to be executed on an Item. */
	public static enum ItemAction
	{
		/** Removing the Item as shortcut. */
		DESELECT(7, "item.deselect"),
		/** Moving the Item in the inventory. */
		GET(0, "item.get"),
		/** Giving the Item to an ally. */
		GIVE(2, "item.give"),
		/** Viewing the Item description. */
		INFO(11, "item.info"),
		/** Placing the Item on the ground. */
		PLACE(6, "item.place"),
		/** Setting the Item as shortcut. */
		SET(8, "item.set"),
		/** Swapping the Item for another Item in the Inventory. */
		SWAP(4, "item.swap"),
		/** Swapping the Item for the Item on the ground. */
		SWITCH(5, "item.switch"),
		/** Taking the Item from an ally. */
		TAKE(3, "item.take"),
		/** Throwing the Item. */
		THROW(9, "item.throw"),
		/** Deleting the Item (in freezones). */
		TRASH(10, "item.trash"),
		/** Using the Item. */
		USE(1, "item.use");

		public static void sort(ArrayList<ItemAction> actions)
		{
			actions.sort(new Comparator<ItemAction>() {

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
	private final int spriteID;

	public Item(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.price = Integer.parseInt(xml.getAttributeValue("price"));
		this.sell = Integer.parseInt(xml.getAttributeValue("sell"));
		this.spriteID = XMLUtils.getAttribute(xml, "sprite", 255);
		this.isStackable = XMLUtils.getAttribute(xml, "stackable", false);
	}

	public Item(int id, int price, int sell, int spriteID, boolean stackable)
	{
		this.id = id;
		this.price = price;
		this.sell = sell;
		this.spriteID = spriteID;
		this.isStackable = stackable;
	}

	/** Called when a Pokémon uses a damaging move. Modifies the attack stat.
	 * 
	 * @param isUser - true if this Item is held by Move's user. */
	public int applyAttackModifications(int attack, Move move, DungeonPokemon user, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return attack;
	}

	/** Called when a Pokémon uses a damaging move. Modifies the attack stat stage.
	 * 
	 * @param isUser - true if this Item is held by the Move's user. */
	public int applyAttackStageModifications(int atkStage, Move move, DungeonPokemon user, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return atkStage;
	}

	/** Called when a Pokémon uses a damaging move. Modifies the defense stat.
	 * 
	 * @param isUser - true if this Ability belongs to the Move's user. */
	public int applyDefenseModifications(int defense, Move move, DungeonPokemon user, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return defense;
	}

	/** Called when a Pokémon uses a damaging move. Modifies the defense stat stage.
	 * 
	 * @param isUser - true if this Ability belongs to the Move's user. */
	public int applyDefenseStageModifications(int defStage, Move move, DungeonPokemon user, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return defStage;
	}

	public ItemCategory category()
	{
		return ItemCategory.OTHERS;
	}

	public ArrayList<ItemAction> getLegalActions(boolean inDungeon)
	{
		ArrayList<ItemAction> actions = new ArrayList<Item.ItemAction>();
		if (inDungeon)
		{
			actions.add(ItemAction.USE);
			actions.add(ItemAction.THROW);
		}
		actions.add(ItemAction.INFO);
		return actions;
	}

	public int getSpriteID()
	{
		return this.spriteID;
	}

	/** The ID of the translation to use for the message to display when using this Item. */
	protected String getUseID()
	{
		return "item.used";
	}

	/** @param event - The Pokémon using the Item.
	 * @return The message to display when using this Item. */
	public Message getUseMessage(ItemSelectionEvent event)
	{
		return new Message(this.getUseID()).addReplacement("<user>", event.user().getNickname())
				.addReplacement("<target>", event.target() == null ? new Message("?", false) : event.target().getNickname())
				.addReplacement("<item>", this.name());
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

	@Override
	public String toString()
	{
		return this.name().toString();
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("type", this.getClass().getName().substring(Item.class.getName().length()));
		root.setAttribute("price", Integer.toString(this.price));
		root.setAttribute("sell", Integer.toString(this.sell));
		XMLUtils.setAttribute(root, "sprite", this.spriteID, 255);
		XMLUtils.setAttribute(root, "stackable", this.isStackable, false);
		return root;
	}

	/** Called when this Item is used.
	 * 
	 * @param floor - The current Floor.
	 * @param pokemon - The Pokémon using the Item.
	 * @param target - The Pokémon the Item is being used on. May be null if there is no target.
	 * @return The messages that were created while using the Item. */
	public ArrayList<DungeonEvent> use(Floor floor, DungeonPokemon pokemon, DungeonPokemon target)
	{
		return new ArrayList<DungeonEvent>();
	}

	/** @return True if the user has to select a Team member as target for this Item. */
	public boolean usedOnTeamMember()
	{
		return false;
	}

}

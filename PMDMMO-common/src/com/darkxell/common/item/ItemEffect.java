package com.darkxell.common.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.item.ItemSelectionEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;

public class ItemEffect
{
	public final int id;

	public ItemEffect(int id)
	{
		this.id = id;
		ItemEffects.effects.put(this.id, this);
	}

	public Message description(Item item)
	{
		return new Message("item.info." + this.id).addReplacement("<item>", item.name());
	}

	/** @return The Tile an Item will land at if it's thrown by the input Pokemon. */
	public Tile findDestinationStraight(Floor floor, Item item, DungeonPokemon pokemon)
	{
		Direction direction = pokemon.facing();
		Tile current = pokemon.tile().adjacentTile(direction);

		while (current.getPokemon() == null || pokemon.isAlliedWith(current.getPokemon()))
		{
			current = current.adjacentTile(direction);
			if (current.isWall()) return current;
		}

		return current;
	}

	/** The ID of the translation to use for the message to display when using this Item. */
	protected String getUseEffectID()
	{
		return "item.used";
	}

	/** @param event - The Pokemon using the Item.
	 * @return The message to display when using this Item. */
	public Message getUseEffectMessage(ItemSelectionEvent event)
	{
		return new Message(this.getUseEffectID()).addReplacement("<user>", event.user().getNickname())
				.addReplacement("<target>", event.target() == null ? new Message("?", false) : event.target().getNickname())
				.addReplacement("<item>", event.item().name());
	}

	/** @return The name of the "Use" option for this Item. */
	public Message getUseName()
	{
		return new Message("item.use");
	}

	/** @return True if the Item disappears after use. */
	public boolean isConsummable()
	{
		return true;
	}

	public boolean isThrowable()
	{
		return true;
	}

	/** @return True if the Item can be used. */
	public boolean isUsable()
	{
		return false;
	}

	/** @return True if the user has to select a Team member as target for this Item. */
	public boolean isUsedOnTeamMember()
	{
		return false;
	}

	/** @return The name of an Item with this Effect. */
	public Message name(Item item)
	{
		return new Message("item." + item.id);
	}

	/** Called when an Item with this Effect is used.
	 * 
	 * @param floor - The current Floor.
	 * @param item - The used Item.
	 * @param pokemon - The Pokemon using the Item.
	 * @param target - The Pokemon the Item is being used on. May be null if there is no target. */
	public void use(Floor floor, Item item, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events)
	{}

}

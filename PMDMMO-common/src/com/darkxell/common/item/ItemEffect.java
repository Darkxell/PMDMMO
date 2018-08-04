package com.darkxell.common.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.item.ItemSelectionEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class ItemEffect
{
	public final int id;

	public ItemEffect(int id)
	{
		this.id = id;
		ItemEffects.effects.put(this.id, this);
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

	/** Called when an Item with this Effect is used.
	 * 
	 * @param floor - The current Floor.
	 * @param item - The used Item.
	 * @param pokemon - The Pokemon using the Item.
	 * @param target - The Pokemon the Item is being used on. May be null if there is no target. */
	public void use(Floor floor, Item item, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events)
	{
		events.add(new MessageEvent(floor, new Message("move.no_effect")));
	}

}

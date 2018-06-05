package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.player.ItemContainer.ItemContainerType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/** Describes the events occurring before using an Item. */
public class ItemSelectionEvent extends DungeonEvent implements Communicable
{

	/** The Item that was used. */
	protected Item item;
	/** The Container the Item was from. */
	protected ItemContainer source;
	/** The index of the Item in the source Container. */
	protected int sourceIndex;
	/** The Pokémon that the Item was used on. null if there was no target. */
	protected DungeonPokemon target;
	/** The Pokémon that used the Item. */
	protected DungeonPokemon user;

	public ItemSelectionEvent(Floor floor)
	{
		super(floor);
	}

	public ItemSelectionEvent(Floor floor, Item item, DungeonPokemon user, DungeonPokemon target, ItemContainer source, int sourceIndex)
	{
		super(floor, user);
		this.item = item;
		this.user = user;
		this.target = target;
		this.source = source;
		this.sourceIndex = sourceIndex;
	}

	public Item item()
	{
		return this.item;
	}

	@Override
	public String loggerMessage()
	{
		return this.user + " selected the " + this.item.name();
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.messages.add(this.item.getUseMessage(this));
		ItemStack stack = this.source.getItem(this.sourceIndex);
		stack.setQuantity(stack.quantity() - 1);
		if (stack.quantity() <= 0) this.source.deleteItem(this.sourceIndex);

		this.resultingEvents.add(new ItemUseEvent(this.floor, this.item, user, this.target));
		return super.processServer();
	}

	@Override
	public void read(JsonObject value) throws JsonReadingException
	{
		if (value.get("user") == null) throw new JsonReadingException("No user value!");
		if (!value.get("user").isNumber()) throw new JsonReadingException("Wrong value for user ID: " + value.get("user"));
		Pokemon pokemon = this.floor.dungeon.communication.pokemonIDs.get(value.getLong("user", 0));
		if (pokemon == null) throw new JsonReadingException("No pokemon with ID " + value.getLong("user", 0));
		this.user = this.actor = pokemon.getDungeonPokemon();

		try
		{
			ItemContainerType type = ItemContainerType.valueOf(value.getString("sourcetype", "null"));
			long id = value.getLong("sourceid", 0);
			this.source = this.floor.dungeon.communication.identifyContainer(this.floor, type, id);
		} catch (JsonReadingException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new JsonReadingException("Wrong values for source container: type=" + value.get("sourcetype") + ", id=" + value.get("sourceid"));
		}

		this.item = this.source.getItem(this.sourceIndex).item();

		try
		{
			this.sourceIndex = value.getInt("sourceindex", 0);
		} catch (Exception e)
		{
			throw new JsonReadingException("Wrong value for source index: " + value.get("sourceindex"));
		}

		if (this.item.usedOnTeamMember())
		{
			if (value.get("target") == null) throw new JsonReadingException("No target value!");
			if (!value.get("target").isNumber()) throw new JsonReadingException("Wrong value for target ID: " + value.get("target"));
			pokemon = this.floor.dungeon.communication.pokemonIDs.get(value.getLong("target", 0));
			if (pokemon == null) throw new JsonReadingException("No pokemon with ID " + value.getLong("target", 0));
			this.target = pokemon.getDungeonPokemon();
		}

	}

	public DungeonPokemon target()
	{
		return this.target;
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject root = Json.object();
		root.add("user", this.user.id());
		if (this.target != null) root.add("target", this.target.id());
		root.add("sourcetype", this.source.containerType().name());
		root.add("sourceid", this.source.containerID());
		root.add("sourceindex", this.sourceIndex);
		return root;
	}

	public DungeonPokemon user()
	{
		return this.user;
	}

}

package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Inventory;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.player.ItemContainer.ItemContainerType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class ItemMovedEvent extends DungeonEvent implements Communicable
{

	protected ItemAction action;
	protected DungeonPokemon mover;
	protected ItemContainer source, destination;
	protected int sourceIndex, destinationIndex;

	public ItemMovedEvent(Floor floor)
	{
		super(floor);
	}

	public ItemMovedEvent(Floor floor, ItemAction action, DungeonPokemon mover, ItemContainer source, int sourceIndex, ItemContainer destination,
			int destinationIndex)
	{
		super(floor, mover);
		this.mover = mover;
		this.action = action;
		this.source = source;
		this.sourceIndex = sourceIndex;
		this.destination = destination;
		this.destinationIndex = destinationIndex;
	}

	public ItemContainer destination()
	{
		return this.destination;
	}

	@Override
	public String loggerMessage()
	{
		return this.mover + " moved an item.";
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		String message = "This shouldn't happen...";
		if (this.action == ItemAction.GIVE) message = "inventory.give";
		else if (this.action == ItemAction.GET)
		{
			if (destination instanceof Inventory) message = "ground.inventory";
			else message = "ground.pickup";
		} else if (this.action == ItemAction.PLACE) message = "ground.place";
		else if (this.action == ItemAction.TAKE) message = "inventory.taken";
		this.messages.add(
				new Message(message).addReplacement("<pokemon>", mover.getNickname()).addReplacement("<item>", this.source.getItem(this.sourceIndex).name()));

		ItemStack i = this.source.getItem(this.sourceIndex);
		this.source.deleteItem(this.sourceIndex);
		if (this.destinationIndex >= this.destination.size()) this.destination.addItem(i);
		else this.destination.setItem(this.destinationIndex, i);
		return super.processServer();
	}

	@Override
	public void read(JsonObject value) throws JsonReadingException
	{
		if (value.get("actor") == null) throw new JsonReadingException("No mover value!");
		if (!value.get("actor").isNumber()) throw new JsonReadingException("Wrong value for mover ID: " + value.get("actor"));
		Pokemon pokemon = this.floor.dungeon.communication.pokemonIDs.get(value.getLong("actor", 0));
		if (pokemon == null) throw new JsonReadingException("No pokemon with ID " + value.getLong("actor", 0));

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

		try
		{
			this.sourceIndex = value.getInt("sourceindex", 0);
		} catch (Exception e)
		{
			throw new JsonReadingException("Wrong value for source index: " + value.get("sourceindex"));
		}

		try
		{
			ItemContainerType type = ItemContainerType.valueOf(value.getString("destinationtype", "null"));
			long id = value.getLong("destinationid", 0);
			this.destination = this.floor.dungeon.communication.identifyContainer(this.floor, type, id);
		} catch (JsonReadingException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new JsonReadingException(
					"Wrong values for destination container: type=" + value.get("destinationtype") + ", id=" + value.get("destinationid"));
		}

		try
		{
			this.destinationIndex = value.getInt("destinationindex", 0);
		} catch (Exception e)
		{
			throw new JsonReadingException("Wrong value for destination index: " + value.get("destinationindex"));
		}

		try
		{
			this.action = ItemAction.valueOf(value.getString("action", this.action.name()));
		} catch (Exception e)
		{
			throw new JsonReadingException("Wrong value for item action: " + value.get("action"));
		}

		this.mover = this.actor = pokemon.getDungeonPokemon();
	}

	public ItemContainer source()
	{
		return this.source;
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject root = Json.object();
		root.add("actor", this.mover.id());
		root.add("sourcetype", this.source.containerType().name());
		root.add("sourceid", this.source.containerID());
		root.add("sourceindex", this.sourceIndex);
		root.add("destinationtype", this.destination.containerType().name());
		root.add("destinationid", this.destination.containerID());
		root.add("destinationindex", this.destinationIndex);
		root.add("action", this.action.name());
		return root;
	}

}

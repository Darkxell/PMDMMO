package com.darkxell.common.dungeon;

import com.darkxell.common.dungeon.TempIDRegistry.ItemsTempIDRegistry;
import com.darkxell.common.dungeon.TempIDRegistry.MovesTempIDRegistry;
import com.darkxell.common.dungeon.TempIDRegistry.PokemonTempIDRegistry;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.EventCommunication;
import com.darkxell.common.event.GameTurn;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.player.ItemContainer.ItemContainerType;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable.JsonReadingException;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class CommunicationUtils
{
	public final DungeonInstance dungeon;
	public final ItemsTempIDRegistry itemIDs = new ItemsTempIDRegistry();
	public final MovesTempIDRegistry moveIDs = new MovesTempIDRegistry();
	public final PokemonTempIDRegistry pokemonIDs = new PokemonTempIDRegistry();

	public CommunicationUtils(DungeonInstance dungeon)
	{
		this.dungeon = dungeon;
	}

	/** @param onlyPAE - True if only Player Action Events should be returned.
	 * @return The list of Events in this exploration, with Dungeon ID and Seed. */
	public JsonObject explorationSummary(boolean onlyPAE)
	{
		JsonObject root = Json.object();
		root.add("dungeon", this.dungeon.id);
		root.add("seed", this.dungeon.seed);

		JsonArray array = (JsonArray) Json.array();

		for (GameTurn turn : this.dungeon.listTurns())
			for (DungeonEvent e : turn.events())
				if (!onlyPAE || e.isPAE()) array.add(EventCommunication.prepareToSend(e));

		root.add("events", array);

		return root;
	}

	public ItemContainer identifyContainer(Floor floor, ItemContainerType type, long id) throws JsonReadingException
	{
		try
		{
			switch (type)
			{
				case TILE:
					int y = (int) (id / floor.getWidth());
					int x = (int) (id - y * floor.getWidth());
					return floor.tileAt(x, y);

				case INVENTORY:
					for (Player p : floor.dungeon.exploringPlayers())
						if (p.inventory().containerID() == id) return p.inventory();
					throw new JsonReadingException("Inventory with id " + id + " doesn't match any known players inventory.");

				case DUNGEON_POKEMON:
				case POKEMON:
					Pokemon p = this.pokemonIDs.get(id);
					if (p == null) throw new JsonReadingException("No Pokémon with id " + id);
					return p;

				default:
					throw new JsonReadingException("ItemContainerType " + type + " CANNOT EXIST!!! WTF??");
			}
		} catch (JsonReadingException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new JsonReadingException("Couldn't find inventory with type " + type + " and id " + id);
		}
	}

}
